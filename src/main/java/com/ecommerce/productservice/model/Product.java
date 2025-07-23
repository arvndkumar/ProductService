package com.ecommerce.productservice.model;

import com.ecommerce.productservice.document.ProductDocument;
import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Product extends Base
{
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public ProductResponseDTO toResponseDTO()
    {
        ProductResponseDTO dto  = new ProductResponseDTO();
        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setDescription(this.description);
        dto.setPrice(this.price);
        dto.setQuantity(this.quantity);
        dto.setImage_url(this.imageUrl);

        if (this.category != null)
        {
            dto.setCategoryId(this.category.getId());
            dto.setCategoryName(this.category.getName());
        }

        return dto;
    }

    public ProductDocument toDocument(){
        return ProductDocument.builder()
                .id(this.getId().toString())
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .imageUrl(this.imageUrl)
                .quantity(this.quantity)
                .categoryName(this.category != null ? this.category.getName():null)
                .build();
    }

}
