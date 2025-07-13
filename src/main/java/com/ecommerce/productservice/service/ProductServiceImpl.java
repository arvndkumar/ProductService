package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import com.ecommerce.productservice.exception.ProductAlreadyExistsException;
import com.ecommerce.productservice.model.Category;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService
{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository)
    {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO)
    {
        Optional<Product> productOptional= productRepository.findByName(productRequestDTO.getName());

        if(productOptional.isPresent()){
            throw new ProductAlreadyExistsException("Product with name \"" + productRequestDTO.getName() + "\" already exists");
        }

        Optional<Category> categoryOptional = categoryRepository.findByName(productRequestDTO.getCategoryName());

        Category category = categoryOptional.orElseGet(()-> categoryRepository.save(new Category(productRequestDTO.getCategoryName())));

        Product product = productRequestDTO.toProduct();
        product.setCategory(category);
        Product savedProduct  = productRepository.save(product);
        return savedProduct.toResponseDTO();
    }
}
