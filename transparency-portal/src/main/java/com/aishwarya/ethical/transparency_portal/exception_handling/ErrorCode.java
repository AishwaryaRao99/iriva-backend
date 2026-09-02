package com.aishwarya.ethical.transparency_portal.exception_handling;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	    USER_NOT_FOUND("USR_001", "User not found", HttpStatus.NOT_FOUND),
	    INVALID_USER_INPUT("USR_002", "Invalid user input", HttpStatus.BAD_REQUEST),
	    INTERNAL_ERROR("GEN_001", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
	    UNAUTHORIZED("AUTH_001", "Unauthorized access", HttpStatus.UNAUTHORIZED),
	    CONFLICT("GEN_002", "Conflict occurred", HttpStatus.CONFLICT), 
	    PRODUCT_NOT_FOUND("PRD_001", "Product not found", HttpStatus.NOT_FOUND),
	    REVIEW_NOT_FOUND("REV_001", "Review not found", HttpStatus.NOT_FOUND);

	    private final String code;     
	    private final String message;
	    private final HttpStatus status;

	    ErrorCode(String code, String message, HttpStatus status) {
	        this.code = code;
	        this.message = message;
	        this.status = status;
	    }
	

}
