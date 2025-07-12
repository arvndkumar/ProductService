package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductDTO;
import com.ecommerce.productservice.exception.ProductAlreadyExistsException;
import com.ecommerce.productservice.model.Category;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testCreateProduct_Success() {
        //Arrange

        ProductDTO dto = new ProductDTO();

        dto.setName("iPhone 16");
        dto.setCategoryName("Phone");


        when(productRepository.findByName("iPhone 16")).thenReturn(Optional.empty());
        when(categoryRepository.findByName("Phone")).thenReturn(Optional.empty());

        Category category = new Category();
        category.setId(1L);
        category.setName("Phone");

        when(categoryRepository.save(any())).thenReturn(category);

        Product productToSave = new Product();

        productToSave.setId(1L);
        productToSave.setName("iPhone 16");
        productToSave.setPrice(999.99);
        productToSave.setDescription("iPhone 16");
        productToSave.setImageUrl("iPhone 16");
        productToSave.setQuantity(100);
        productToSave.setCategory(category);

        when(productRepository.save(any())).thenReturn(productToSave);



        //Act
        ProductDTO response = productService.createProduct(dto);

        //Assert
        assertEquals("iPhone 16", response.getName());
        assertEquals("Phone", response.getCategoryName());
        assertNotNull(response.getId());

    }

    @Test
    void testCreateProduct_AlreadyExists() {
        //Arrange
        ProductDTO dto = new ProductDTO();
        dto.setName("iPhone 16");
        dto.setCategoryName("Phone");

        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("iPhone 16");

        when(productRepository.findByName("iPhone 16")).thenReturn(Optional.of(existingProduct));

        //Act and Assert
        assertThrows(ProductAlreadyExistsException.class, () -> {productService.createProduct(dto);
        });
    }
}