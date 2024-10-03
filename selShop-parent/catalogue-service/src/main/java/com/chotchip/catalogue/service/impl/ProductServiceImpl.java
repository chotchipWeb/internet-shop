package com.chotchip.catalogue.service.impl;

import com.chotchip.catalogue.repositroy.ProductRepository;
import com.chotchip.catalogue.service.ProductService;
import com.chotchip.catalogue.dto.ProductCreateDTO;
import com.chotchip.catalogue.dto.ProductUpdateDTO;
import com.chotchip.catalogue.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;


    @Override
    @Transactional(readOnly = true)
    public Iterable<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional()
    public Product createProduct(ProductCreateDTO productCreateDTO) {

        return productRepository.save(new Product(null, productCreateDTO.getTitle(), productCreateDTO.getDetails()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional()
    public void updateProduct(Integer id, ProductUpdateDTO productUpdateDTO) {
        this.productRepository.findById(id)
                .ifPresentOrElse(product -> {
                    product.setTitle(productUpdateDTO.getTitle());
                    product.setDetails(productUpdateDTO.getTitle());
                }, () -> {
                    throw new NoSuchElementException();
                });
    }

    @Override
    @Transactional()
    public void deleteProduct(Integer id) {
        productRepository.delete(findById(id).get());
    }
}
