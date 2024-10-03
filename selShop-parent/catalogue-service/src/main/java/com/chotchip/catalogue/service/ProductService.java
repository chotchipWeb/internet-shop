package com.chotchip.catalogue.service;

import com.chotchip.catalogue.dto.ProductCreateDTO;
import com.chotchip.catalogue.dto.ProductUpdateDTO;
import com.chotchip.catalogue.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Iterable<Product> findAllProducts();

    Product createProduct(ProductCreateDTO productCreateDTO);

    Optional<Product> findById(Integer id);

    void updateProduct(Integer id, ProductUpdateDTO productUpdateDTO);

    void deleteProduct(Integer id);
}
