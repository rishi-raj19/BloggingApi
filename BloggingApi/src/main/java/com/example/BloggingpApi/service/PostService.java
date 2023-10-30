package com.example.BloggingpApi.service;

import com.example.BloggingpApi.model.Comment;
import com.example.BloggingpApi.model.Post;
import com.example.BloggingpApi.model.User;
import com.example.BloggingpApi.repository.ICommentRepo;
import com.example.BloggingpApi.repository.IPostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    @Autowired
    IPostRepo postRepo;

    @Autowired
    ICommentRepo commentRepo;

    public String addBlog(Post post) {
        post.setPostCreationTimeStamp(LocalDateTime.now());
        postRepo.save(post);
        return "Post uploaded";
    }

    public String deletBlogPost(Long postId, User blogOwner) {
        Post post = postRepo.findById(postId).orElse(null);
        if(post != null && post.getPostOwner().equals(blogOwner)){
            postRepo.deleteById(postId);

            return "Blog post removed successfully ";
        }else if(post == null){
            return "post to be delete doesn't exist";
        }else {
            return "Un-Authorized user cant no delete post";
        }
    }


    public Post getPostById(Long postId) {
        return (postRepo.findById(postId)).orElse(null);
    }


    public List<Post> getAllBlogPost() {
        return postRepo.findAll();
    }

    public boolean validatePost(Post post) {
        return (post != null && postRepo.existsById(post.getPostId()));
    }

    public Post findPostById(Long postId) {
        return postRepo.findById(postId).orElse(null);
    }





    public String postBlog(List<Post> blogs) {
        postRepo.saveAll(blogs);
        return "Blogs posted";
    }

    public String updateBlogContent(Post blogPost, String blogContent) {
        blogPost.setPostContent(blogContent);
        postRepo.save(blogPost);

        return "BlogContent Updated";
    }

    public boolean iValidBloggerToUpdate(Post blogPost, User blogOwner) {
        return blogPost.getPostOwner().equals(blogOwner);
    }

    public List<Comment> getAllComment(Long postId) {
        Post post = postRepo.findById(postId).orElse(null);
        if (post == null){
            return null;
        }
        return commentRepo.findAll();

    }
}