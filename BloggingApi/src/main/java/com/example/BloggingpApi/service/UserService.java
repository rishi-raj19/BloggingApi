package com.example.BloggingpApi.service;

import com.example.BloggingpApi.model.*;
import com.example.BloggingpApi.model.dto.SignInInput;
import com.example.BloggingpApi.model.dto.SignUpOutput;
import com.example.BloggingpApi.repository.IUserRepo;
import com.example.BloggingpApi.service.utility.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    IUserRepo userRepo;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikerService likerService;

    @Autowired
    FollowService followService;

    public SignUpOutput signUpUser(User user) throws NoSuchAlgorithmException {
        boolean signUpStatus = true;
        String signUpStatusMessage = null;

        String newEmail = user.getUserEmail();

        if(newEmail == null){
            signUpStatusMessage = "Invalid email";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus, signUpStatusMessage);
        }

        //check if this user email is already exist

        User existingUser = userRepo.findFirstByUserEmail(newEmail);
        if(existingUser != null){
            signUpStatusMessage = "Email already registerd";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus, signUpStatusMessage);
        }


        //hash the password : encrypt the password
        String encryptedPassword = PasswordEncrypter.encryptPassword(user.getUserPassword());

        //save user with encrypted password
        user.setUserPassword(encryptedPassword);
        userRepo.save(user);

        return new SignUpOutput(true, "User registerd successfully");
    }

    public String signInUser(SignInInput signInInput) {
        String signInStatusMessage = null;
        boolean signInStatus = true;

        String signInEmail = signInInput.getEmail();

        if(signInEmail == null){
            signInStatusMessage = "Invalid email";
            signInStatus = false;
        }

        //check if this user email already exist
        User existingUser = userRepo.findFirstByUserEmail(signInEmail);

        if(existingUser == null){
            signInStatusMessage = "Email not registered";
            signInStatus = false;
        }

        //match password
        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(signInInput.getPassword());
            if (existingUser.getUserPassword().equals(encryptedPassword)) {
                //session should be created since password matched and user id is valid
                AuthenticationToken authToken = new AuthenticationToken(existingUser);
                authenticationService.saveAuthToken(authToken);

                //EmailHandler.sendEmail("guddukumar032002@gmail.com", "email testing", authToken.getTokenValue());
                return "Token sent to your email "+ authToken.getTokenValue();
            } else {
                signInStatusMessage = "Invalid credentials!!!";
                return signInStatusMessage;
            }
        } catch (Exception e) {
            signInStatusMessage = "Internal error occurred during sign in";
            return signInStatusMessage;
        }

    }

    public String signOutUser(String email) {
        User user = userRepo.findFirstByUserEmail(email);
        AuthenticationToken authenticationToken = authenticationService.findFirstByUser(user);
        authenticationService.removeToken(authenticationToken);

        return "User signOut successfully";
    }

    public String addBlog(Post post, String email) {
        User postOwner = userRepo.findFirstByUserEmail(email);
        post.setPostOwner(postOwner);
        return postService.addBlog(post);
    }

    public String deleteBlogPost(Long postId, String email) {
        User blogOwner = userRepo.findFirstByUserEmail(email);
        return postService.deletBlogPost(postId, blogOwner);
    }


    public List<Post> getAllBlogPost() {
        return postService.getAllBlogPost();
    }

    public String addComment(Comment comment, String commenterEmail) {
        boolean isPostValid = postService.validatePost(comment.getPost());
        if(isPostValid){
            User commenter = userRepo.findFirstByUserEmail(commenterEmail);
            comment.setCommenter(commenter);
            return commentService.addComment(comment);
        }else {
            return "Cannot comment on invalid post";
        }
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }


    private boolean authorizerCommentRemover(String email, Comment comment) {
        String commentOwnerEmail = comment.getCommenter().getUserEmail();
        String postOwnerEmail =  comment.getPost().getPostOwner().getUserEmail();

        return (postOwnerEmail.equals(email) || commentOwnerEmail.equals(email));
    }
    public String deleteBlogPostComment(Long commentId, String email) {
        Comment comment = commentService.findComment(commentId);
        if(comment != null){
            if(authorizerCommentRemover(email, comment)){
                commentService.deleteComment(comment);
                return "Comment deleted successfully";
            }else {
                return "Un-authorized delete not allowed";
            }
        }else {
            return "Invalid commenter";
        }
    }


    public String addLike(Like like, String likerEmail) {
        Post blogPost = like.getPost();
        boolean isPostValid = postService.validatePost(blogPost);

        if(isPostValid){
            User liker = userRepo.findFirstByUserEmail(likerEmail);
            if(likerService.isLikeAllowedOnThisPost(blogPost, liker)){
                like.setLiker(liker);
                return likerService.addLike(like);
            }else {
                return "already liker";
            }

        }else {
            return "Cannot liked on invalid Post";
        }


    }

    public String getLikeCountByPostId(Long postId, String userEmail) {

        Post validPost = postService.getPostById(postId);

        if(validPost != null){
            Integer  likeCountForPost = likerService.getLikeCountForPost(validPost);
            return String.valueOf(likeCountForPost);
        }else {
            return "Cannot count like on Invalid post";
        }
    }

    private boolean authorizerLikeRemover(String email, Like like) {

        String likeOwnerEmail = like.getLiker().getUserEmail();
        return email.equals(likeOwnerEmail);
    }
    public String removeBlogPostLike(Long likeId, String email) {
        Like like = likerService.findById(likeId);
        if (like != null) {
            if (authorizerLikeRemover(email, like)) {
                return "Liked removed successfully";
            } else {
                return "Un-authorized like removal not allowed";
            }
        }else{
            return  "Invalid like";
        }
    }


    public String followBlogger(Follow follow, String followerEmail) {
        User followBlogger =  userRepo.findById(follow.getCurrentUser().getUserId()).orElse(null);

        User follower = userRepo.findFirstByUserEmail(followerEmail);

        if(followBlogger != null){
            if(followService.isFollowAllowed(followBlogger, follower)){
                followService.startFollowing(follow);

                return follower.getUserName() + " is now following "+ followBlogger.getUserName();
            }else{
                return  follower.getUserName()+ " already follow "+ followBlogger.getUserName();
            }
        }else {
            return "User to be followed is Invalid";
        }
    }

    private boolean authorizeUnfollow(String email, Follow follow) {
        String unfollowBloggerEmail = follow.getCurrentUser().getUserEmail();
        String followerEmail = follow.getFollower().getUserEmail();

        return unfollowBloggerEmail.equals(email) || followerEmail.equals(email);
    }

    public String unfollowBlogger(Long followingId, String followerEmail) {
        Follow follow = followService.findFollow(followingId);
        if(follow != null){
            if(authorizeUnfollow(followerEmail, follow)){
                followService.UnfollowBlogger(follow);
                return follow.getCurrentUser().getUserName()+ " not follow anymore by "+ followerEmail;
            }else{
                return "Un-authorized follow not allowed";
            }
        }else {
            return "Invalid follow mapping";
        }
    }




    public String postBlogs(List<Post> blogs, String email) {
        User blogOwner = userRepo.findFirstByUserEmail(email);
        if(blogOwner != null){
            return postService.postBlog(blogs);
        }else {
            return "Invalid Blogger for post Blogs";
        }
    }

    public String updateBlogPost(Long postId, String bloggerEmail, String blogContent) {
        Post blogPost = postService.findPostById(postId);
        if(blogPost == null){
            return "BlogPost doesn't exist";
        }
        User blogOwner = userRepo.findFirstByUserEmail(bloggerEmail);

        boolean isValidBlogger = postService.iValidBloggerToUpdate(blogPost, blogOwner);
        if(isValidBlogger){
            return postService.updateBlogContent(blogPost, blogContent);
        }else{
            return "Invalid Blogger to update post";
        }
    }

    public List<Comment> getComments(Long postId, String email) {
        return  postService.getAllComment(postId);
    }

    public Post getPostById(Long postId) {
        return postService.getPostById(postId);
    }

    public String updateComment(Long commentId, String commenterEmail, String commentBody) {
        Comment comment = commentService.findCommentById(commentId);

        if(comment == null){
            return "comment not exist with comment id "+ commentId;
        }

        User commenter = userRepo.findFirstByUserEmail(commenterEmail);

        boolean isRightCommenter = commentService.rightCommenter(comment, commenter);

        if(isRightCommenter){
            return commentService.updateComment(comment, commentBody);
        }else {
            return "Incorrect comment owner mapping";
        }
    }
}