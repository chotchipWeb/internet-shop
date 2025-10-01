package com.chotchip.manager.client;

import com.chotchip.manager.dto.ProductCreateDTO;
import com.chotchip.manager.dto.ProductUpdateDTO;
import com.chotchip.manager.entity.Product;

import java.util.Optional;

public interface RestClientProduct {
    Iterable<Product> findAllProducts();

    Product createProduct(ProductCreateDTO productCreateDTO);

    Optional<Product> findById(Integer id);

    void updateProduct(Integer id , ProductUpdateDTO productUpdateDTO);

    void deleteProduct(Integer id);
}
