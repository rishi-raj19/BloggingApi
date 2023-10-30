package com.example.BloggingpApi.model;

import com.example.BloggingpApi.model.Enums.PostType;
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
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private Long postId;

    @NotBlank
    @Size(max = 10000)
    private String postContent;

    @Size(max = 100)
    private String postCaption;
    @NotBlank
    private String postLocation;
    private PostType postType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime postCreationTimeStamp;

    @ManyToOne
    @JoinColumn(name = "fk_post_owner_id")
    private User postOwner;
}
