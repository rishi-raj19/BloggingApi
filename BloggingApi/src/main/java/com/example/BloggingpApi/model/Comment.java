package com.example.BloggingpApi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long commentId;
    @NotBlank
    @Size(max = 200)
    private String commentBody;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime commentCreationTimeStamp;

    @ManyToOne
    @JoinColumn(name = "fk_comment_post_id")
    private Post post;


    @ManyToOne
    @JoinColumn(name = "fk_commenter-id")
    private User commenter;

}