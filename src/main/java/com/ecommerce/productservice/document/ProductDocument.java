package com.ecommerce.productservice.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName ="products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDocument {
    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String imageUrl;
    private String categoryName;

}
