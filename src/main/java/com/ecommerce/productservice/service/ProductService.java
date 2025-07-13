package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;

public interface ProductService
{
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
}
