package com.company.springreactive.techie.controller;

import com.company.springreactive.techie.domain.dto.ProductDTO;
import com.company.springreactive.techie.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public Flux<ProductDTO> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDTO> getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @GetMapping("/product-range")
    public Flux<ProductDTO> getProductBetweenRange(@RequestParam("min") double min, @RequestParam("max") double max) {
        return productService.getProductInRange(min, max);
    }

    @PostMapping("/")
    public Mono<ProductDTO> saveProduct(@RequestBody Mono<ProductDTO> productDTOMono) {
        return productService.saveProduct(productDTOMono);
    }

    @PutMapping("/update/{id}")
    public Mono<ProductDTO> updateProduct(@RequestBody Mono<ProductDTO> productDTOMono, @PathVariable String id) {
        return productService.updateProduct(productDTOMono, id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }

}
