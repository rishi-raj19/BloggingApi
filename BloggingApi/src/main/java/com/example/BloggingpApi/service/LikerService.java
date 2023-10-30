package com.example.BloggingpApi.service;

import com.example.BloggingpApi.model.Like;
import com.example.BloggingpApi.model.Post;
import com.example.BloggingpApi.model.User;
import com.example.BloggingpApi.repository.ILikerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikerService {

    @Autowired
    ILikerRepo likeRepo;

    public boolean isLikeAllowedOnThisPost(Post blogPost, User liker) {
        List<Like> likeList = likeRepo.findByPostAndLiker(blogPost, liker);
        return likeList != null && likeList.isEmpty();

    }

    public String addLike(Like like) {
        likeRepo.save(like);
        return "Blog post liked successfully";
    }

    public Integer getLikeCountForPost(Post validPost) {
        return likeRepo.findByPost(validPost).size();
    }

    public Like findById(Long likeId) {
        return likeRepo.findById(likeId).orElse(null);
    }
}