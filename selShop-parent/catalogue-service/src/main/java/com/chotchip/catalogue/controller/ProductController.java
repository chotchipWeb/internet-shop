package com.chotchip.catalogue.controller;

import com.chotchip.catalogue.dto.ProductUpdateDTO;
import com.chotchip.catalogue.entity.Product;
import com.chotchip.catalogue.exception.NotFoundProductException;
import com.chotchip.catalogue.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProductController {

    private final ProductService productService;

    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product getProduct(@PathVariable("productId") int id) {
        log.debug("Fetching product with id={}", id);
        Product product = this.productService.findById(id)
                .orElseThrow(NotFoundProductException::new);
        log.debug("Successfully fetched product: {}", product.getId());
        return product;
    }

    @GetMapping
    public Product findById(@ModelAttribute("product") Product product) {
        return product;
    }

    @PutMapping
    public ResponseEntity<Void> updateProduct(@PathVariable("productId") int id,
                                              @Valid @RequestBody ProductUpdateDTO productUpdateDTO,
                                              BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                log.warn("Validation failed: {}", bindingResult.getAllErrors());
                throw exception;
            }
            log.warn("Validation failed: {}", bindingResult.getAllErrors());
            throw new BindException(bindingResult);

        } else {
            productService.updateProduct(id, productUpdateDTO);
            log.debug("Update product with id: {}", id);
            return ResponseEntity
                    .noContent()
                    .build();
        }
    }

    @DeleteMapping

    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") int id) {
        log.info("Received request to delete product with id={}", id);
        productService.deleteProduct(id);
        log.info("Product with id={} deleted successfully", id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @ExceptionHandler(NotFoundProductException.class)
    public ResponseEntity<ProblemDetail> handlerNotFoundProductException(NotFoundProductException e, Locale locale) {
        log.warn("Attempted to delete non-existing product. Reason: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                        this.messageSource.getMessage(e.getMessage(), new Object[0], "Not found Product", locale)));
    }
}
