package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductDTO;
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
    public ProductDTO createProduct(ProductDTO productDTO)
    {

        Optional<Category> optionalCategory = categoryRepository.findByName(productDTO.getCategoryName());

        Category category = optionalCategory.orElseGet(() -> categoryRepository.save(new Category(productDTO.getCategoryName())));

        Product product = productDTO.toProduct();
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return savedProduct.toDTO();

    }
}
