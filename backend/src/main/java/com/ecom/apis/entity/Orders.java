package com.ecom.apis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @ManyToOne
    private UserEntity user;
    @OneToMany
    List<Products> productsList;
    private  Double price;
    private String transactionId;
    private String time;
}
