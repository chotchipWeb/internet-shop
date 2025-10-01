package com.chotchip.manager.controller;

import com.chotchip.manager.client.RestClientProduct;
import com.chotchip.manager.dto.ProductCreateDTO;
import com.chotchip.manager.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {
    private final RestClientProduct restClientProduct;

    @GetMapping("/list")
    public String getProductList(Model model) {
        model.addAttribute("products", this.restClientProduct.findAllProducts());
        return "catalogue/products/list";
    }

    @GetMapping("/create")
    public String createProductPage() {
        return "catalogue/products/create";
    }


    @PostMapping("/create")
    public String createProduct(ProductCreateDTO productCreateDTO, Model model) {
        try {
            this.restClientProduct.createProduct(productCreateDTO);
            return "redirect:/catalogue/products/list";
        } catch (BadRequestException e) {
            model.addAttribute("dto", productCreateDTO);
            model.addAttribute("errors", e.getErrors());
            return "/catalogue/products/create";
        }
    }


}
