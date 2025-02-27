package com.ecom.apis.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;
    @ManyToOne
    private Products products;
    @OneToOne
    private UserEntity user;
    @Min(value = 0,message = "Enter valid rating in range 0-5")
    @Max(value = 5,message = "Enter valid rating in range 0-5")
    private double rating;
}
