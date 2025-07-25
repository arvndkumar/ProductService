package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductService
{
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    public ProductResponseDTO getProductByID(Long id);

    public List<ProductResponseDTO> getAllProducts(Pageable pageable);

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);

    ProductResponseDTO patchProduct(Long id, Map<String, Object> updates);

    void deleteProduct(Long id);
}
