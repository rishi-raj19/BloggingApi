package com.example.BloggingpApi.model;

import com.example.BloggingpApi.model.Enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long userId;
    @NotBlank
    private String userName;
    @Email
    @Column(unique = true)
    private  String userEmail;
    @Size(min = 10)
    @Size(max = 100)
    private  String userBio;
    @Size(min = 8)
    @NotBlank
    private String userPassword;
    private LocalDateTime userDob;
    @Enumerated(EnumType.STRING)
    private Gender gender;

}