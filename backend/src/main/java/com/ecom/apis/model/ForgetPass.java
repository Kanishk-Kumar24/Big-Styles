package com.ecom.apis.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPass {
    @NotBlank(message = "Please provide the email of the user")
    private String email;
    @NotBlank(message = "Provide the otp")
    private String otp;
    @NotBlank(message = "Provide new password")
    private String password;
}
