package com.ecommerce.productservice.service.search;

import com.ecommerce.productservice.document.ProductDocument;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import com.ecommerce.productservice.repository.search.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductSearchServiceImpl implements ProductSearchService {


    private final ProductSearchRepository productSearchRepository;

    @Override
    public List<ProductResponseDTO> search(String query, int page, int size, String sortParam) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortParam).descending());

        Page<ProductDocument> productPage  =
                productSearchRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, pageable);

        return productPage.getContent()
                .stream()
                .map(this::toProductResponseDTO)
                .toList();
    }

    @Override
    public void save(ProductDocument productDocument) {
        productSearchRepository.save(productDocument);
    }

    @Override
    public void deleteById(String id) {
        productSearchRepository.deleteById(id);
    }

    private ProductResponseDTO toProductResponseDTO(ProductDocument d) {
        ProductResponseDTO dto = new ProductResponseDTO();

        dto.setName(d.getName());
        dto.setDescription(d.getDescription());
        dto.setPrice(d.getPrice());
        dto.setQuantity(d.getQuantity());
        dto.setCategoryName(d.getCategoryName());
        dto.setImage_url(d.getImageUrl());
        return dto;
    }
}
