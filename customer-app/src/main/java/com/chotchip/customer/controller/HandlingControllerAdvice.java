package com.chotchip.customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;
import java.util.NoSuchElementException;

@ControllerAdvice
@RequiredArgsConstructor
public class HandlingControllerAdvice {
    private final MessageSource messageSource;

    @ExceptionHandler(NoSuchElementException.class)
    public String handlerNoSuchElementException(NoSuchElementException e, Model model, Locale locale) {
        model.addAttribute("errors", this.messageSource.getMessage(e.getMessage(), new Object[0], locale));
        return "customer/products/error/404";
    }
}
