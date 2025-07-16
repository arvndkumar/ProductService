package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import com.ecommerce.productservice.exception.ProductAlreadyExistsException;
import com.ecommerce.productservice.exception.ProductNotFoundException;
import com.ecommerce.productservice.model.Category;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public ProductResponseDTO getProductByID(Long id)
    {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with id " + id));


        return product.toResponseDTO();
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {

        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO updatedDto){
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));

        existingProduct.setName(updatedDto.getName());
        existingProduct.setPrice(updatedDto.getPrice());
        existingProduct.setDescription(updatedDto.getDescription());
        existingProduct.setQuantity(updatedDto.getQuantity());
        existingProduct.setImageUrl(updatedDto.getImage_url());

        Category category = categoryRepository.findByName(updatedDto.getCategoryName())
                .orElseGet(()-> {
                    Category newCategory = new Category(updatedDto.getCategoryName());
                    categoryRepository.save(newCategory);
                    return newCategory;
                });

        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.save(existingProduct);
        return updatedProduct.toResponseDTO();

    }

}
