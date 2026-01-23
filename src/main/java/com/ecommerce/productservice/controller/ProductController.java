package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.document.ProductDocument;
import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.search.ProductSearchService;
import com.ecommerce.productservice.service.*;
import com.ecommerce.productservice.util.ApplicationCommons;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController
{
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ProductSearchService productSearchService;
    private final ApplicationCommons applicationCommons;


    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO)
    {
        return ResponseEntity.ok(productService.createProduct(productRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> searchProductByID(@PathVariable Long id)
    {

        return ResponseEntity.ok(productService.getProductByID(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String bearer)
    {
        applicationCommons.validateToken(bearer);
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage  = productRepository.findAllByDeletedFalse(pageable);

        List<ProductResponseDTO> response = productPage.getContent().stream()
                .map(Product::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id,
                                                            @RequestBody ProductRequestDTO productRequestDTO,
                                                            @RequestHeader("Authorization") String bearer)
    {
        applicationCommons.validateToken(bearer);
        return ResponseEntity.ok(productService.updateProduct(id, productRequestDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> patchProduct(
            @RequestHeader("Authorization") String bearer,
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates)
    {
        applicationCommons.validateToken(bearer);
        return ResponseEntity.ok(productService.patchProduct(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @RequestHeader("Authorization") String bearer,
            @PathVariable Long id)
    {
        applicationCommons.validateToken(bearer);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDocument>> searchProducts(
            @RequestParam String query)
    {
        List<ProductDocument> results = productSearchService.search(query);
        return ResponseEntity.ok(results);
    }
}
