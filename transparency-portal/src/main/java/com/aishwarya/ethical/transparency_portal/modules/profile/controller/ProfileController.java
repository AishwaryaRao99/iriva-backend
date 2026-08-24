package com.aishwarya.ethical.transparency_portal.modules.profile.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aishwarya.ethical.transparency_portal.modules.product.dto.ProductDTO;
import com.aishwarya.ethical.transparency_portal.modules.product.service.ProductService;
import com.aishwarya.ethical.transparency_portal.modules.profile.dto.ProfileDTO;
import com.aishwarya.ethical.transparency_portal.modules.profile.dto.ReviewDTO;
import com.aishwarya.ethical.transparency_portal.modules.profile.dto.ReviewRequest;
import com.aishwarya.ethical.transparency_portal.modules.profile.dto.SaveProductResponse;
import com.aishwarya.ethical.transparency_portal.modules.profile.service.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/profile")
@Validated
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final ProductService productService;

    @GetMapping
    public ProfileDTO getProfile(Authentication authentication) {
        return profileService.getProfile(authentication.getName());
    }

    @GetMapping("/reviews")
    public List<ReviewDTO> getReviews(Authentication authentication) {
        return profileService.getReviews(authentication.getName());
    }

    @GetMapping("/saved-products")
    public List<ProductDTO> getSavedProducts(Authentication authentication) {
        return profileService.getSavedProducts(authentication.getName()).stream()
                .map(product -> productService.getProductById(product.getId())).toList();
    }

    @PostMapping("/saved-products/{productId}")
    public SaveProductResponse saveProduct(Authentication authentication, @PathVariable Long productId) {
        return new SaveProductResponse(productId, profileService.saveProduct(authentication.getName(), productId));
    }

    @DeleteMapping("/saved-products/{productId}")
    public ResponseEntity<Void> removeSavedProduct(Authentication authentication, @PathVariable Long productId) {
        profileService.removeSavedProduct(authentication.getName(), productId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reviews/{productId}")
    public ResponseEntity<ReviewDTO> addReview(Authentication authentication, @PathVariable Long productId,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.addReview(authentication.getName(), productId, request));
    }
}