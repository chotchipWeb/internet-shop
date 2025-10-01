package com.chotchip.catalogue.controller;

import com.chotchip.catalogue.dto.ProductCreateDTO;
import com.chotchip.catalogue.entity.Product;
import com.chotchip.catalogue.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/catalogue-api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductsController {

    private final ProductService productService;

    @GetMapping
    public Iterable<Product> getAllProducts() {
        log.debug("Fetching all products");
        return this.productService.findAllProducts();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO,
                                                 UriComponentsBuilder uriComponentsBuilder,
                                                 BindingResult bindingResult) throws BindException {
        log.debug("Create product dto: {}", productCreateDTO);
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                log.warn("Validation failed: {}", bindingResult.getAllErrors());
                throw exception;
            }
            log.warn("Validation failed: {}", bindingResult.getAllErrors());
            throw new BindException(bindingResult);

        } else {
            Product product = this.productService.createProduct(productCreateDTO);
            log.debug("Successful create product with id: {}", product.getId());
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("/catalogue-api/products/{productId}")
                            .build(Map.of("productId", product.getId())))
                    .body(product);
        }

    }
}
