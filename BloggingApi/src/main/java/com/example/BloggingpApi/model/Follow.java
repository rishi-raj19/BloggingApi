package com.example.BloggingpApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Follow {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long followId;

    @ManyToOne
    @JoinColumn(name = "fk_current_user_id")
    private User currentUser;

    @ManyToOne
    @JoinColumn(name = "fk_follower_of_current_user")
    private User follower;
}