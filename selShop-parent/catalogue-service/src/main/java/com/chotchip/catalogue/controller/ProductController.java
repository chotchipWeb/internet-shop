package com.chotchip.catalogue.controller;

import com.chotchip.catalogue.dto.ProductUpdateDTO;
import com.chotchip.catalogue.entity.Product;
import com.chotchip.catalogue.exception.NotFoundProductException;
import com.chotchip.catalogue.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("catalogue-api/products/{productId:\\d+}")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final MessageSource messageSource;

    private static final String ProductById = "PRODUCT_ID";

    @ModelAttribute("product")
    @Cacheable(value = ProductById, key = "#id")
    public Product getProduct(@PathVariable("productId") int id) {
        return this.productService.findById(id)
                .orElseThrow(NotFoundProductException::new);
    }

    @GetMapping
    public Product findById(@ModelAttribute("product") Product product) {
        return product;
    }

    @PutMapping
    @CachePut(value = ProductById, key = "#id")
    public ResponseEntity<Void> updateProduct(@PathVariable("productId") int id,
                                              @Valid @RequestBody ProductUpdateDTO productUpdateDTO,
                                              BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) throw exception;
            else throw new BindException(bindingResult);
        } else {
            productService.updateProduct(id, productUpdateDTO);
            return ResponseEntity
                    .noContent()
                    .build();
        }
    }

    @DeleteMapping
    @CacheEvict(value = ProductById, key = "#id")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") int id) {
        productService.deleteProduct(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @ExceptionHandler(NotFoundProductException.class)
    public ResponseEntity<ProblemDetail> handlerNotFoundProductException(NotFoundProductException e, Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                        this.messageSource.getMessage(e.getMessage(), new Object[0], "Not found Product", locale)));
    }
}
