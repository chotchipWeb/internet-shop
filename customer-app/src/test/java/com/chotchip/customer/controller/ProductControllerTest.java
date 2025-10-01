package com.chotchip.customer.controller;

import com.chotchip.customer.client.FavouriteProductClient;
import com.chotchip.customer.client.ProductReviewClient;
import com.chotchip.customer.client.ProductsClient;
import com.chotchip.customer.dto.ProductReviewDisplayDTO;
import com.chotchip.customer.entity.FavouriteProduct;
import com.chotchip.customer.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    ProductsClient client;

    @Mock
    FavouriteProductClient favouriteProductClient;
    @Mock
    ProductReviewClient productReviewClient;

    @InjectMocks
    ProductController controller;

    @Test
    void getProductPage_ResponseProductPage() {
        // given
        var model = new ConcurrentModel();
        var list = List.of(new ProductReviewDisplayDTO(2, "details 1"), new ProductReviewDisplayDTO(3, "details 2"));
        doReturn(Flux.fromIterable(list))
                .when(this.productReviewClient)
                .findAllProductReviewByProductId(1);
        var favouriteProduct = new FavouriteProduct(UUID.randomUUID(), 1);
        doReturn(Mono.just(favouriteProduct))
                .when(this.favouriteProductClient)
                .findFavouriteProductById(1);
        // when
        StepVerifier.create(this.controller.getProductPage(1, model))
                // then
                .expectNext("customer/products/product")
                .verifyComplete();
        assertEquals(model.getAttribute("reviews"), list);
        assertEquals(model.getAttribute("inFavourite"), true);

    }


}