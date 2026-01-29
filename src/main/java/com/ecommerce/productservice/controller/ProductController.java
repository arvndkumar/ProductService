package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import com.ecommerce.productservice.service.*;
import com.ecommerce.productservice.util.ApplicationCommons;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController
{
    private final ProductService productService;
    private final ApplicationCommons applicationCommons;


    @PostMapping("/create")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO)
    {
        return ResponseEntity.ok(productService.createProduct(productRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> searchProductByID(@PathVariable Long id)
    {

        return ResponseEntity.ok(productService.getProductByID(id));
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

    @GetMapping("/browse")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name.keyword") String sort
    )
    {
        List<ProductResponseDTO> response = productService.findAllProducts(page, size, sort);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/browse/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name.keyword") String sort
    )
    {
        List<ProductResponseDTO> response = productService.findAllProductsByCategory(categoryId, page, size, sort);

        return ResponseEntity.ok(response);
    }

}
