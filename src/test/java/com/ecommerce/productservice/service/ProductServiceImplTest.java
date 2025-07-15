package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import com.ecommerce.productservice.exception.ProductAlreadyExistsException;
import com.ecommerce.productservice.exception.ProductNotFoundException;
import com.ecommerce.productservice.model.Category;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
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

        ProductRequestDTO dto = new ProductRequestDTO();

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
        ProductResponseDTO response = productService.createProduct(dto);

        //Assert
        assertEquals("iPhone 16", response.getName());
        assertEquals("Phone", response.getCategoryName());
        assertNotNull(response.getId());

    }

    @Test
    void testCreateProduct_AlreadyExists() {
        //Arrange
        ProductRequestDTO dto = new ProductRequestDTO();
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

    @Test
    void testGetProductById_Success() {

        //Arrange
        Long id = 1L;
        Category category = new Category();
        category.setId(10L);
        category.setName("Phone");

        Product product = new Product();
        product.setId(id);
        product.setName("iPhone 16");
        product.setDescription("iPhone 16");
        product.setImageUrl("iPhone 16");
        product.setPrice(999.99);
        product.setCategory(category);
        product.setQuantity(100);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        //Act
        ProductResponseDTO dto = productService.getProductByID(id);
        assertEquals("iPhone 16", dto.getName());
        assertEquals("Phone", dto.getCategoryName());
        assertEquals(1L,dto.getId());
    }

    @Test
    void testGetProductById_NotFound() {
        //Arrange
        Long id = 999L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(ProductNotFoundException.class, () -> {productService.getProductByID(id);});

    }

    @Test
    void testGetAllProducts_Paginated(){
        //Arrange
        Pageable pageable = PageRequest.of(0, 2);
        Category category = new Category();
        category.setId(1L);
        category.setName("Phone");

        Product product = new Product();
        product.setId(1L);
        product.setName("iPhone 16");
        product.setDescription("iPhone 16");
        product.setImageUrl("iPhone 16");
        product.setPrice(999.99);
        product.setCategory(category);
        product.setQuantity(100);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("iPhone 16 - 2");
        product2.setDescription("iPhone 16 -2");
        product2.setImageUrl("iPhone 16");
        product2.setPrice(998.99);
        product2.setCategory(category);
        product2.setQuantity(100);

        List<Product> products = List.of(product, product2);
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        //Act
        Page<Product> resultPage  = productRepository.findAll(pageable);

        //Assert
        assertEquals(2, resultPage.getContent().size());
        assertEquals("iPhone 16", resultPage.getContent().get(0).getName());

    }
}