package com.ecom.apis.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Login {
    @NotBlank(message = "please provide a valid email")
    private String email;
    @NotBlank(message = "please provide a valid email")
    private String password;
}
