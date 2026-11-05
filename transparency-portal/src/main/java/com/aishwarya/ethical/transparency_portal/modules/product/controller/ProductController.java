package com.aishwarya.ethical.transparency_portal.modules.product.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aishwarya.ethical.transparency_portal.modules.product.dto.FilterCriteria;
import com.aishwarya.ethical.transparency_portal.modules.product.dto.PageRequestDTO;
import com.aishwarya.ethical.transparency_portal.modules.product.dto.PageResponse;
import com.aishwarya.ethical.transparency_portal.modules.product.dto.ProductCategoryDTO;
import com.aishwarya.ethical.transparency_portal.modules.product.dto.ProductDTO;
import com.aishwarya.ethical.transparency_portal.modules.product.service.ProductService;
import com.aishwarya.ethical.transparency_portal.modules.profile.dto.ReviewDTO;
import com.aishwarya.ethical.transparency_portal.modules.profile.service.ProfileService;

@RestController
@RequestMapping("/api/v1/productsapi")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
	private final ProductService productService;
	private final ProfileService profileService;

	public ProductController(ProductService productService, ProfileService profileService) {
		this.productService = productService;
		this.profileService = profileService;
	}


	/**
	 * Get all product categories for UI Home screen, with their icons.
	 * @return List of ProductCategoryDTO
	 */
	@GetMapping("/categories")
	public List<ProductCategoryDTO> getAllCategories() {
		return productService.getAllCategoriesWithIcons();
	}

	/**
	 * Get all products by category (DTO, secure, for UI category click).
	 */
	@GetMapping("/by-category")
	public List<ProductDTO> getProductsByCategory(@RequestParam String category) {
		return productService.getProductsByCategory(category);
	}

	/**
	 * Get all products (DTO, secure, authenticated users only).
	 */
	@GetMapping("/get-all-products")
	// @PreAuthorize("isAuthenticated()")
	public List<ProductDTO> getAllProducts() {
		return productService.getAllProducts();
	}

	/**
	 * Search products by name (DTO, secure, authenticated users only).
	 */
	@GetMapping("/search")
	// @PreAuthorize("isAuthenticated()")
	public List<ProductDTO> searchProductsByName(@RequestParam String name) {
		return productService.searchProductsByName(name);
	}

	/**
	 * Get product by ID (DTO, secure, authenticated users only).
	 */
	@GetMapping("/{id}")
	// @PreAuthorize("isAuthenticated()")
	public ProductDTO getProductById(@PathVariable Long id) {
		return productService.getProductById(id);
	}

	/**
	 * Get reviews submitted by all users for a product.
	 */
	@GetMapping("/{id}/reviews")
	public List<ReviewDTO> getProductReviews(Authentication authentication, @PathVariable Long id) {
		return profileService.getProductReviews(authentication.getName(), id);
	}

	@GetMapping("/{id}/review-tags")
	public List<String> getProductReviewTags(@PathVariable Long id) {
		return productService.getProductReviewTags(id);
	}

	// ==================== PAGINATION ENDPOINTS ====================

	/**
	 * Get all products with pagination and sorting.
	 */
	@GetMapping("/paginated/all")
	public PageResponse<ProductDTO> getAllProductsPaginated(PageRequestDTO request) {
		return productService.getAllProductsPaginated(request.getPage(), request.getSize(), request.getSortBy() ,  request.getSortDirection());
	}

	/**
	 * Get products by category with pagination and sorting.
	 */
	@GetMapping("/paginated/by-category")
	public PageResponse<ProductDTO> getProductsByCategoryPaginated(
			@RequestParam String category,
			PageRequestDTO request) {
		return productService.getProductsByCategoryPaginated(category, request.getPage(), request.getSize(), request.getSortBy() ,  request.getSortDirection());
	}

	/**
	 * Search products by name with pagination and sorting.
	 */
	@GetMapping("/paginated/search")
	public PageResponse<ProductDTO> searchProductsByNamePaginated(
			@RequestParam String name,
			PageRequestDTO request) {
		return productService.searchProductsByNamePaginated(name, request.getPage(), request.getSize(), request.getSortBy() ,  request.getSortDirection());
	}

	/**
	 * Search products by brand with pagination and sorting.
	 */
	@GetMapping("/paginated/search-brand")
	public PageResponse<ProductDTO> searchProductsByBrand(
			@RequestParam String brand,
			PageRequestDTO request) {
		return productService.searchProductsByBrand(brand, request.getPage(), request.getSize(), request.getSortBy() ,  request.getSortDirection());
	}

	// ==================== ADVANCED FILTERING ====================

	/**
	 * Advanced search with multiple filter criteria.
	 * Accepts a POST request with complex filter parameters.
	 */
	@PostMapping("/filters/advanced-search")
	public PageResponse<ProductDTO> advancedSearch(@RequestBody FilterCriteria criteria) {
		return productService.advancedSearch(criteria);
	}

}
