package com.example.BloggingpApi.repository;


import com.example.BloggingpApi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostRepo extends JpaRepository<Post, Long> {

}
