package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{
    Optional<Product> findByNameAndDeletedFalse(String name);

    @Query("SELECT p FROM Product p WHERE p.deleted = false")
    Page<Product> findAllByDeletedFalse(Pageable pageable);

    Optional<Product> findByIdAndDeletedFalse(Long id);
}
