package com.example.BloggingpApi.repository;

import com.example.BloggingpApi.model.Like;
import com.example.BloggingpApi.model.Post;
import com.example.BloggingpApi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILikerRepo extends JpaRepository<Like, Long> {
    List<Like> findByPostAndLiker(Post blogPost, User liker);

    List<Like> findByPost(Post validPost);
}