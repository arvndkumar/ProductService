package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService
{
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    public ProductResponseDTO getProductByID(Long id);

    public List<ProductResponseDTO> getAllProducts();
}
