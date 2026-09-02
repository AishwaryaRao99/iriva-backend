package com.aishwarya.ethical.transparency_portal.exception_handling;

public class ReviewNotFoundException extends ApiException {
    public ReviewNotFoundException(String message) {
        super(ErrorCode.REVIEW_NOT_FOUND, message);
    }
}