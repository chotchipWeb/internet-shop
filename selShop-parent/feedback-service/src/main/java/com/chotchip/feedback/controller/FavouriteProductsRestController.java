package com.chotchip.feedback.controller;

import com.chotchip.feedback.entity.FavouriteProduct;
import com.chotchip.feedback.service.FavouriteProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/feedback-api/favourites-products/")
@RequiredArgsConstructor
public class FavouriteProductsRestController {
    private final FavouriteProductService favouriteProductService;

    @GetMapping
    public Flux<FavouriteProduct> findAllFavouriteProducts() {
        return this.favouriteProductService.findAllFavouriteProduct();
    }
}
