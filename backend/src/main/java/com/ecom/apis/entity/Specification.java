package com.ecom.apis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Specification {
    @NotBlank(message = "provide the color of the product")
    private String color;
    @NotBlank(message = "Provide the size of product")
    private String size;
    @NotBlank(message = "Provide details related to provide")
    private String details;
}
