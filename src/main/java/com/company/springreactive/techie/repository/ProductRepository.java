package com.company.springreactive.techie.repository;

import com.company.springreactive.techie.domain.dto.ProductDTO;
import com.company.springreactive.techie.domain.entity.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    Flux<ProductDTO> findByPriceBetween(Range<Double> priceRange);
}

