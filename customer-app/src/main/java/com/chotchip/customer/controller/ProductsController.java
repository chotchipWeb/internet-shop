package com.chotchip.customer.controller;

import com.chotchip.customer.client.ProductsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer/products")
public class ProductsController {

    private final ProductsClient productsClient;


    @GetMapping("/list")
    public Mono<String> getProductListPage(Model model) {
        return this.productsClient.findAllProducts()
                .collectList()
                .doOnNext(products -> model.addAttribute("products", products))
                .thenReturn("customer/products/list");
    }


}
