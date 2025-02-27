package com.ecom.apis.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OTP {
    @NotBlank(message = "provide the email of the user")
    private String email;
    @Min(value = 100000,message = "Enter a valid otp")
    @Max(value = 999999,message = "Enter a valid otp")
    private String otp;
}
