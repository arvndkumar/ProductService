package com.ecommerce.productservice.search;

import com.ecommerce.productservice.document.ProductDocument;
import com.ecommerce.productservice.model.Product;

import java.util.List;

public interface ProductSearchService {
    List<ProductDocument> search(String query);

    void save(ProductDocument productDocument);

    void deleteById(String id);
}
