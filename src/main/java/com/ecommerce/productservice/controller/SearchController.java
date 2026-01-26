package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ProductResponseDTO;
import com.ecommerce.productservice.service.search.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class SearchController {
    private final ProductSearchService productSearchService;


    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name.keyword") String sort)
    {

        List<ProductResponseDTO> response = productSearchService.search(query, page, size, sort);

        return ResponseEntity.ok(response);
    }
}
