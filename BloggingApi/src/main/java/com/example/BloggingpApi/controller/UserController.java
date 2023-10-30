package com.example.BloggingpApi.controller;

import com.example.BloggingpApi.model.*;
import com.example.BloggingpApi.model.dto.SignInInput;
import com.example.BloggingpApi.model.dto.SignUpOutput;
import com.example.BloggingpApi.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
public class UserController  {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;

    @Autowired
    LikerService likerService;

    @Autowired
    PostService postService;


    //signUp user
    @PostMapping("user/signup")
    public SignUpOutput signUpUser(@RequestBody @Valid User user) throws NoSuchAlgorithmException {
        return userService.signUpUser(user);
    }

    @PostMapping("user/signIn")
    public String signInUser(@RequestBody  SignInInput signInInput){
        return userService.signInUser(signInInput);
    }

    @DeleteMapping("user/signOut")
    public String signOutUser(String email, String token){
        if(authenticationService.authenticate(email, token)){
            return userService.signOutUser(email);
        }else {
            return "SignOut not allowed for unauthorized user";
        }
    }




    @GetMapping("users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("blog/post/create")
    public String createBlogPost(@RequestBody @Valid Post post, @RequestParam  String email, @RequestParam String token){
        if(authenticationService.authenticate(email, token)){
            return userService.addBlog(post, email);
        }else {
            return "Not an authenticate user activity";
        }
    }

    @PostMapping("blogs")
    public String postBlogs(@RequestBody @Valid List<Post> blogs, @RequestParam String email, @RequestParam String token){
        if(authenticationService.authenticate(email, token)){
            return userService.postBlogs(blogs, email);
        }else{
            return "Not an authenticated user activity";
        }
    }

    @PutMapping("update/blog/{postId}")
    public String updateBlogPost(@PathVariable Long postId,  @RequestParam @Valid String bloggerEmail, @RequestParam String token, @RequestParam String blogContent){
        if(authenticationService.authenticate(bloggerEmail, token)){
            return userService.updateBlogPost(postId, bloggerEmail, blogContent);
        }else {
            return "Not an authenticated user activity";
        }
    }

    @DeleteMapping("blog/post/delete")
    public String deleteBlogPost(@RequestParam Long postId, @RequestParam String email, @RequestParam String token){
        if(authenticationService.authenticate(email, token)){
            return userService.deleteBlogPost(postId, email);
        }else {
            return "Not an authenticated user activity";
        }
    }

    @GetMapping("get/blog/posts")
    public List<Post> getAllBlogPost(){
        return userService.getAllBlogPost();
    }

    @GetMapping("post/{postId}")
    public Post getPostById(@PathVariable Long postId){
        return userService.getPostById(postId);
    }


    //commenting on blog post
    @PostMapping("comment")
    public String addComment(@RequestBody @Valid Comment comment, @RequestParam @Valid String commenterEmail, @RequestParam String commenterToken){
        if(authenticationService.authenticate(commenterEmail, commenterToken)){
            return userService.addComment(comment, commenterEmail);
        }else{
            return "Not an authenticated user activity";
        }
    }

    @GetMapping("comments/{postId}")
    public List<Comment> getAllComments(@PathVariable Long postId, @RequestParam @Valid String email, @RequestParam String token){
        if(authenticationService.authenticate(email, token)){
            return userService.getComments(postId, email);
        }else{
            return null;
        }
    }

    @PutMapping("comment/{commentId}")
    public String updateComment(@PathVariable Long commentId, @RequestParam @Valid String commenterEmail, @RequestParam String token, @RequestParam String commentBody){
        if(authenticationService.authenticate(commenterEmail, token)){
            return userService.updateComment(commentId, commenterEmail, commentBody);
        }else {
            return "Not an authenticated user activity!!!";
        }
    }

    @DeleteMapping("comment")
    public String deleteBlogPostComment(@RequestParam Long commentId, @RequestParam @Valid String email , @RequestParam String token){
        if(authenticationService.authenticate(email, token)){
            return userService.deleteBlogPostComment(commentId, email);
        }else{
            return "Not an authenticated user activity";
        }
    }

    //adding like functionality
    @PostMapping("like")
    public String addLike(@RequestBody Like like, @RequestParam @Valid String likerEmail, @RequestParam String likerAuthToken){
        if(authenticationService.authenticate(likerEmail, likerAuthToken)){
            return userService.addLike(like, likerEmail);
        }else {
            return "Not an authenticated user activity";
        }
    }

    @GetMapping("like/count/blogPost/{postId}")
    public String getLikeCountByPostId(@PathVariable Long postId, @RequestParam @Valid String userEmail, @RequestParam String userToken){
        if(authenticationService.authenticate(userEmail, userToken)){
            return userService.getLikeCountByPostId(postId, userEmail);
        }else{
            return "Not an authenticatedUser activity";
        }
    }


    @DeleteMapping("like")
    public String removeBlogPostLike(@RequestParam Long likeId, @RequestParam @Valid String email, @RequestParam String token){
        if(authenticationService.authenticate(email, token)){
            return userService.removeBlogPostLike(likeId, email);
        }else {
            return "Not an authenticated user activity!!!";
        }
    }

    //follow functionality in blogging api
    @PostMapping("follow")
    public String followBlogger(@RequestBody Follow follow,@RequestParam @Valid String followerEmail, @RequestParam String token){
        if(authenticationService.authenticate(followerEmail, token)){
            return userService.followBlogger(follow, followerEmail);
        }else {
            return "Not an authenticated user activity";
        }
    }

    @DeleteMapping("unfollow/bologger/{followingId}")
    public String unfollowBlogger(@PathVariable Long followingId, @RequestParam @Valid String followerEmail, @RequestParam String token){
        if(authenticationService.authenticate(followerEmail, token)){
            return userService.unfollowBlogger(followingId, followerEmail);
        }else {
            return "Not an authenticate user activity";
        }
    }



}