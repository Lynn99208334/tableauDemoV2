package com.example.novaledger.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.Email;

@Data
public class RegisterRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank
    @Size(min = 4, max = 50)
    private String username;
}
