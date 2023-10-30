package com.example.BloggingpApi.repository;

import com.example.BloggingpApi.model.Follow;
import com.example.BloggingpApi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFollowRepo extends JpaRepository<Follow, Long> {
    List<Follow> findByCurrentUserAndFollower(User followBlogger, User follower);
}