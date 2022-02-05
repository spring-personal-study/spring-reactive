package com.company.springreactive.techie.utils;

import com.company.springreactive.techie.domain.dto.ProductDTO;
import com.company.springreactive.techie.domain.entity.Product;
import org.springframework.beans.BeanUtils;

public class AppUtils {

    public static ProductDTO entityToDto(Product product) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }

    public static Product dtoToEntity(ProductDTO productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }
}
