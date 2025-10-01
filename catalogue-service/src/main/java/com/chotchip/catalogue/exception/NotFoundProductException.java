package com.chotchip.catalogue.exception;

public class NotFoundProductException extends RuntimeException {
    public NotFoundProductException(){
        super("catalogue.errors.product");
    }
}
