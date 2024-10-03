package com.chotchip.manager.controller;

import com.chotchip.manager.client.impl.RestClientProductImpl;
import com.chotchip.manager.dto.ProductCreateDTO;
import com.chotchip.manager.entity.Product;
import com.chotchip.manager.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)

class ProductsControllerTest {

    @Mock
    RestClientProductImpl restClientProduct;

    @InjectMocks
    ProductsController controller;

    @Test
    @DisplayName("createProduct создаст новый товар и перенаправит на страницу товаров")
    void createProduct_RequestIsValid_ReturnsRedirectionToProductListPage() {
        //given
        var productCreateDTO = new ProductCreateDTO("123", "123");
        var model = new ConcurrentModel();

        doReturn(new Product(1, "123", "123"))
                .when(this.restClientProduct)
                .createProduct(new ProductCreateDTO("123", "123"));

        //when
        var result = this.controller.createProduct(productCreateDTO, model);
        //then
        assertEquals("redirect:/catalogue/products/list", result);
        verify(this.restClientProduct).createProduct(new ProductCreateDTO("123", "123"));
        verifyNoMoreInteractions(this.restClientProduct);

    }

    @Test
    void createProduct_RequestIsInvalid_ReturnsToProductCreatePage() {
        //given
        var productCreateDTO = new ProductCreateDTO("", "123");
        var model = new ConcurrentModel();
        doThrow(new BadRequestException(List.of("Ошибка 1")))
                .when(this.restClientProduct)
                .createProduct(new ProductCreateDTO("", "123"));
        //when
        var result = this.controller.createProduct(productCreateDTO, model);
        //then
        assertEquals("/catalogue/products/create", result);
        assertEquals(model.getAttribute("dto"), productCreateDTO);
        assertEquals(model.getAttribute("errors"), List.of("Ошибка 1"));

        verify(this.restClientProduct).createProduct(new ProductCreateDTO("", "123"));
        verifyNoMoreInteractions(this.restClientProduct);

    }

    @Test
    void createProductPage_RequestGet_ReturnProductCreatePage() {

        // when
        var result = this.controller.createProductPage();
        //then
        assertEquals("catalogue/products/create", result);
    }

    @Test
    void getProductList_Request_ReturnProductListPage() {
        //given
        var model = new ConcurrentModel();
        doReturn(List.of())
                .when(this.restClientProduct)
                .findAllProducts();

        // when
        var result = this.controller.getProductList(model);

        //then
        assertEquals("catalogue/products/list", result);
    }
}