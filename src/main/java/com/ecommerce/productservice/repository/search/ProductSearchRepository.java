package com.ecommerce.productservice.repository.search;

import com.ecommerce.productservice.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String>
{
    List<ProductDocument> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

}
