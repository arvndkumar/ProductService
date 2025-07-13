package com.ecommerce.productservice.dto;

import com.ecommerce.productservice.model.Category;
import com.ecommerce.productservice.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    private String name;
    private String description;
    private double price;
    private int quantity;
    private String image_url;
    private Long categoryId;
    private String categoryName;


    public Product toProduct()
    {
        Product product = new Product();

        product.setName(this.name);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setQuantity(this.quantity);
        product.setImageUrl(this.image_url);

        Category category = new Category();
        category.setId(this.categoryId);
        category.setName(this.categoryName);
        product.setCategory(category);
        return product;
    }
}
