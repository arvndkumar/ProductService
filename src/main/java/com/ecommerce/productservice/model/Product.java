package com.ecommerce.productservice.model;

import com.ecommerce.productservice.dto.ProductDTO;
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

    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public ProductDTO toDTO()
    {
        ProductDTO dto  = new ProductDTO();
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

}
