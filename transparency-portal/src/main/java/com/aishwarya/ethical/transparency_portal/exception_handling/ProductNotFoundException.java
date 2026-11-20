package com.aishwarya.ethical.transparency_portal.exception_handling;

public class ProductNotFoundException extends ApiException {
    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(String message) {
        super(ErrorCode.PRODUCT_NOT_FOUND, message);
    }
}
