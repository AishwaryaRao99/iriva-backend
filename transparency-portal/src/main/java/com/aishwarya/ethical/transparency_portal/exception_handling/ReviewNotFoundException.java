package com.aishwarya.ethical.transparency_portal.exception_handling;

public class ReviewNotFoundException extends ApiException {
   
	private static final long serialVersionUID = 642532118832764282L;

	public ReviewNotFoundException(String message) {
        super(ErrorCode.REVIEW_NOT_FOUND, message);
    }
}