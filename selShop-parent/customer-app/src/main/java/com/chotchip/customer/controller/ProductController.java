package com.chotchip.customer.controller;

import com.chotchip.customer.client.ProductsClient;
import com.chotchip.customer.dto.ProductReviewCreateDTO;
import com.chotchip.customer.entity.Product;
import com.chotchip.customer.service.FavouriteProductService;
import com.chotchip.customer.service.ProductReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer/product/{productId:\\d+}")
public class ProductController {

    private final ProductsClient productsClient;
    private final FavouriteProductService favouriteProductService;
    private final ProductReviewService productReviewService;


    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") int id) {
        return this.productsClient.findProductById(id);
    }

    @GetMapping
    public Mono<String> getProductPage(@PathVariable("productId") int id, Model model) {
        return productReviewService.findProductReviewById(id)
                .collectList()
                .doOnNext(productReviewDisplayDTO -> model.addAttribute("reviews", productReviewDisplayDTO))
                .then(this.favouriteProductService.findFavouriteProductById(id)
                        .doOnNext(favouriteProduct -> model.addAttribute("inFavourite", true)))
                .thenReturn("customer/products/product");
    }


    @PostMapping("/add-to-favourites")
    public Mono<String> addProductToFavourites(@ModelAttribute("product") Mono<Product> product) {
        return product
                .map(Product::getId)
                .flatMap(productId -> this.favouriteProductService.addProductToFavourite(productId)
                        .thenReturn("redirect:/customer/product/%s".formatted(product))
                );
    }

    @DeleteMapping("/delete-from-favourites")
    public Mono<String> deleteProductFavourites(@ModelAttribute("product") Mono<Product> product) {
        return product
                .map(Product::getId)
                .flatMap(productId -> this.favouriteProductService.removeProductToFavourite(productId)
                        .thenReturn("redirect:/customer/product/%s".formatted(product))
                );
    }

    @PostMapping("/add-review")
    public Mono<String> addProductReview(@PathVariable("productId") int productId,
                                         @Valid ProductReviewCreateDTO dto,
                                         BindingResult bindingResult,
                                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", dto);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList()
            );
            return Mono.just("/customer/products/%s".formatted(productId));
        } else {
            return this.productReviewService.createProductReview(productId, dto)
                    .thenReturn("redirect:/customer/products/%s".formatted(productId));
        }
    }

}
