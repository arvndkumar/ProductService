package com.ecommerce.productservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String image_url;
    private Long categoryId;
    private String categoryName;
}
