package com.ecom.apis.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Builder
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @NotEmpty(message = "Product name is necessary")
    private String name;
    private Boolean sale = false;
    private int salePrice=0;
    @Min(value = 1,message = "Price can't be less than 1")
    private Double price;
    @Min(value = 1,message = "Minimum of one product is required")
    private int quantity;
    @Embedded
    private Specification specification;
    @Min(value = 0,message = "Enter a valid seller id")
    private Long sellerId;

}
