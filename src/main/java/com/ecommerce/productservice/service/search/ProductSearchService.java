package com.ecommerce.productservice.service.search;

import com.ecommerce.productservice.document.ProductDocument;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductSearchService {
    List<ProductResponseDTO> search(String query, int pageNumber, int pageSize, String sortParam);

    void save(ProductDocument productDocument);

    void deleteById(String id);
}
