package com.ecom.apis.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Products products;
    @Min(value = 1,message = "Cart product quantity should be minimum 1")
    private int quantity;

}
