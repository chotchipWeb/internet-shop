package com.chotchip.catalogue.service.impl;

import com.chotchip.catalogue.dto.ProductCreateDTO;
import com.chotchip.catalogue.dto.ProductUpdateDTO;
import com.chotchip.catalogue.entity.Product;
import com.chotchip.catalogue.repositroy.ProductRepository;
import com.chotchip.catalogue.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.chotchip.catalogue.constants.ConstantsProducts.ALL_PRODUCTS;
import static com.chotchip.catalogue.constants.ConstantsProducts.PRODUCT_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = ALL_PRODUCTS)
    public Iterable<Product> findAllProducts() {
        log.debug("Find all products");
        return productRepository.findAll();
    }

    @Override
    @Transactional()
    @CacheEvict(value = ALL_PRODUCTS, allEntries = true)
    public Product createProduct(ProductCreateDTO productCreateDTO) {
        log.debug("Save dto");
        Product save = productRepository.save(new Product(null, productCreateDTO.getTitle(), productCreateDTO.getDetails()));
        log.debug("Successful save products with id: {}", save.getId());
        return save;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = PRODUCT_ID, key = "#id")
    public Optional<Product> findById(Integer id) {
        log.debug("Find by id: {}", id);
        Optional<Product> byId = productRepository.findById(id);
        log.debug("Found products with id: {}", id);
        return byId;
    }

    @Override
    @Transactional()
    @CachePut(value = PRODUCT_ID, key = "#id")
    public void updateProduct(Integer id, ProductUpdateDTO productUpdateDTO) {
        log.debug("Update products with id: {}", id);
        this.productRepository.findById(id)
                .ifPresentOrElse(product -> {
                    product.setTitle(productUpdateDTO.getTitle());
                    product.setDetails(productUpdateDTO.getTitle());
                }, () -> {
                    log.warn("Fail update products with id: {}", id);
                    throw new NoSuchElementException();
                });
    }

    @Override
    @Transactional()
    @CacheEvict(value = PRODUCT_ID, key = "#id")
    public void deleteProduct(Integer id) {
        log.debug("Products with id: {} ready to remove", id);
        productRepository.delete(findById(id).get());
        log.debug("Successful delete products with id: {}", id);
    }
}
