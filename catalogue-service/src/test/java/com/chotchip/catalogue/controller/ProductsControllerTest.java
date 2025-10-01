package com.chotchip.catalogue.controller;

import com.chotchip.catalogue.dto.ProductCreateDTO;
import com.chotchip.catalogue.entity.Product;
import com.chotchip.catalogue.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Mock
    ProductService productService;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    ProductsController productsController;

    @Test
    void getProduct_RequestIsValid_ResponseAllProduct() {

        // given
        doReturn(List.of(new Product(1, "title", "details"), new Product(2, "title 2", "details 2")))
                .when(this.productService)
                .findAllProducts();

        // when
        Iterable<Product> result = productsController.getAllProducts();

        // then
        assertEquals(result, List.of(new Product(1, "title", "details"), new Product(2, "title 2", "details 2")));
    }

    @Test
    void createProduct_RequestIsValid_ResponseNewProduct() throws BindException {
        // given
        var productDTO = new ProductCreateDTO("title", "details");
        var uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost");
        var bindingResult = new MapBindingResult(Map.of(), "productCreateDTO");


        doReturn(new Product(1, "title", "details"))
                .when(this.productService)
                .createProduct(new ProductCreateDTO("title", "details"));

        // when

        var result = productsController.createProduct(productDTO, uriComponentsBuilder, bindingResult);

        // then

        assertEquals(result.getHeaders().getLocation(), URI.create("http://localhost/catalogue-api/products/1"));
        assertEquals(result.getStatusCode(), HttpStatus.CREATED);
        assertEquals(result.getBody(), new Product(1, "title", "details"));

    }

    @Test
    void createProduct_RequestIsInvalidAndBindResultIsBindException_ResponseNewProduct() throws BindException {
        var productDTO = new ProductCreateDTO("title", "details");
        var uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost");
        var bindingResult = new MapBindingResult(Map.of(), "productCreateDTO");
        bindingResult.addError(new FieldError("productCreateDTO", "title", "error"));

        // when
        var bindException = assertThrows(BindException.class, () ->
                this.productsController.createProduct(productDTO, uriComponentsBuilder, bindingResult));

        // then
        assertEquals(List.of(new FieldError("productCreateDTO", "title", "error")), bindException.getAllErrors());

    }


}