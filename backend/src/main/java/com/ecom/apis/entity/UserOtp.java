package com.ecom.apis.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otpId;
    @NotBlank(message = "Please provide the email of the user")
    private String email;
    @NotBlank(message = "Provide the otp")
    private String code;
    private Date expiration;
}
