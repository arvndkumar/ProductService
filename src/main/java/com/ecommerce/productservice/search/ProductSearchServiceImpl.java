package com.ecommerce.productservice.search;

import com.ecommerce.productservice.document.ProductDocument;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.repository.search.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchServiceImpl implements ProductSearchService {

    private final ProductRepository productRepository;
    private final ProductSearchRepository productSearchRepository;

    @Override
    public List<ProductDocument> search(String query) {
        return productSearchRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
    }

    @Override
    public void save(ProductDocument productDocument) {
        productSearchRepository.save(productDocument);
    }

    @Override
    public void deleteById(String id) {
        productSearchRepository.deleteById(id);
    }
}
