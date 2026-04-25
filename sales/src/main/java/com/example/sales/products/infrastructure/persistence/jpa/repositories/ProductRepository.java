package com.example.sales.products.infrastructure.persistence.jpa.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sales.products.domain.model.aggregates.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Optional<Product> findByName(String name);

  boolean existsByName(String name);

  boolean existsByIdNotAndName(Long id, String name);

}
