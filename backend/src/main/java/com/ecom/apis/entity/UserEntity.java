package com.ecom.apis.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.aspectj.bridge.IMessage;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "User",uniqueConstraints = {@UniqueConstraint(columnNames = {"userEmail","phoneNumber"})})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    @NotNull(message = "Valid username not provided")
    private String userName;
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty")
    @Column(unique = true)
    private String userEmail;
    @Min(value = 1000000000,message = "Mobile number can be less then 10 digits")
    private long phoneNumber;
    @NotBlank(message = "Please enter address")
    private String address;
    @Min(value = 10000,message = "Enter a valid pincode")
    private int pinCode;
    @NotBlank(message = "Password is needed")
    private String userPassword;
    @NotBlank(message = "Confirm Password is needed")
    private String confirmPassword;
    private int verified ;
    private String role = "USER";
}
