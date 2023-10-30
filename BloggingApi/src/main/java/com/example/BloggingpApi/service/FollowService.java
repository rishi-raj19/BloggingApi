package com.example.BloggingpApi.service;

import com.example.BloggingpApi.model.Follow;
import com.example.BloggingpApi.model.User;
import com.example.BloggingpApi.repository.IFollowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {

    @Autowired
    IFollowRepo followRepo;

    public boolean isFollowAllowed(User followBlogger, User follower) {
        List<Follow> followList = followRepo.findByCurrentUserAndFollower(followBlogger, follower);
        return (followList != null && followList.isEmpty() && !followBlogger.equals(follower));
    }

    public void startFollowing(Follow follow) {
        followRepo.save(follow);
    }

    public Follow findFollow(Long followingId) {
        return followRepo.findById(followingId).orElse(null);
    }

    public void UnfollowBlogger(Follow follow) {
        followRepo.delete(follow);
    }
}