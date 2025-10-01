package com.chotchip.manager.controller;

import com.chotchip.manager.client.RestClientProduct;
import com.chotchip.manager.dto.ProductUpdateDTO;
import com.chotchip.manager.entity.Product;
import com.chotchip.manager.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.ui.ConcurrentModel;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    RestClientProduct restClientProduct;
    @Mock
    MessageSource messageSource;

    @InjectMocks
    ProductController productController;

    @Test
    void product_RequestIsInValid_ResponseProduct() {
        // given
        int id = 1;
        doReturn(Optional.of(new Product(1, "title", "details")))
                .when(this.restClientProduct)
                .findById(id);
        // when
        var result = this.productController.product(id);
        // then
        assertEquals(new Product(1, "title", "details"), result);
    }

    @Test
    void product_RequestIsInInValid_ResponseErrorPage404() {
        // given
        int id = 1;
        // when
        var exception = assertThrows(NoSuchElementException.class,
                () -> this.productController.product(1));
        // then
        assertEquals("catalogue.errors.product", exception.getMessage());
    }

    @Test
    void getProduct_RequestIsValid_ResponseProductPage() {
        // when
        var result = this.productController.getProduct();
        // then
        assertEquals("catalogue/products/product", result);
    }

    @Test
    void getProductEdit_RequestIsValid_ResponseProductEditPage() {
        // when
        var result = this.productController.getProductEdit();
        // then
        assertEquals("catalogue/products/edit", result);
    }

    @Test
    void editProduct_RequestIvValid_ResponseProductListPage() {
        // given
        Product product = new Product(1, "title", "details");
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO("title", "details");
        var model = new ConcurrentModel();

        // when
        var result = this.productController.editProduct(product, productUpdateDTO, model);
        // then
        assertEquals("redirect:/catalogue/products/list", result);
    }

    @Test
    void editProduct_RequestIsInValid_ResponseProductCreatePage() {
        // given
        Product product = new Product(1, "title", "details");
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO("title", "details");
        var model = new ConcurrentModel();

        doThrow(new BadRequestException(List.of("Ошибка 1")))
                .when(this.restClientProduct)
                .updateProduct(product.getId(), productUpdateDTO);
        // when
        var result = this.productController.editProduct(product, productUpdateDTO, model);
        // then
        assertEquals("/catalogue/products/create", result);
        assertEquals(model.getAttribute("dto"), productUpdateDTO);
        assertEquals(model.getAttribute("errors"), List.of("Ошибка 1"));
    }

    @Test
    void deleteProduct_RequestIsValid_ResponseProductListPage() {
        // given
        var product = new Product(1, "title", "details");
        // when
        var result = this.productController.deleteProduct(product);
        // then
        assertEquals("redirect:/catalogue/products/list", result);
    }

}