package com.aishwarya.ethical.transparency_portal.security;

import java.util.function.Supplier;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
	private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();

	private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {

		// Use XOR protection for the request attribute
		this.xor.handle(request, response, csrfToken);

		// Force deferred token loading
		csrfToken.get();
	}

	@Override
	public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {

		String headerValue = request.getHeader(csrfToken.getHeaderName());

		// Resolve plain token from the request header
		if (StringUtils.hasText(headerValue)) {
			return this.plain.resolveCsrfTokenValue(request, csrfToken);
		}

		// Resolve XOR-protected token from request parameters
		return this.xor.resolveCsrfTokenValue(request, csrfToken);
	}
}
