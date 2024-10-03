package com.chotchip.manager.controller;

import com.chotchip.manager.client.RestClientProduct;
import com.chotchip.manager.dto.ProductUpdateDTO;
import com.chotchip.manager.entity.Product;
import com.chotchip.manager.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("catalogue/products/{productId}")
@RequiredArgsConstructor
public class ProductController {
    private final RestClientProduct restClientProduct;
    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.restClientProduct.findById(productId).orElseThrow(() -> new NoSuchElementException("catalogue.errors.product"));
    }

    @GetMapping()
    public String getProduct() {
        return "catalogue/products/product";
    }

    @GetMapping("/edit")
    public String getProductEdit() {
        return "catalogue/products/edit";
    }

    @PostMapping("/edit")
    public String editProduct(@ModelAttribute("product") Product product, ProductUpdateDTO productUpdateDTO, Model model) {
        try {
            this.restClientProduct.updateProduct(product.getId(), productUpdateDTO);
            return "redirect:/catalogue/products/list";
        } catch (BadRequestException e) {
            model.addAttribute("dto", productUpdateDTO);
            model.addAttribute("errors", e.getErrors());
            return "/catalogue/products/create";
        }

    }

    @PostMapping("/delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        this.restClientProduct.deleteProduct(product.getId());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model, Locale locale) {
        model.addAttribute("error", this.messageSource.getMessage(exception.getMessage(), new Object[0], locale));
        return "catalogue/products/error/404";
    }
}
