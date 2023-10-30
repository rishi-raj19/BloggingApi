package com.example.BloggingpApi.repository;

import com.example.BloggingpApi.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepo extends JpaRepository<Comment, Long> {
}