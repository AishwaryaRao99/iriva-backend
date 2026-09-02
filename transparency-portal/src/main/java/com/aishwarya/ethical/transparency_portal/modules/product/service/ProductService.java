package com.aishwarya.ethical.transparency_portal.modules.product.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.aishwarya.ethical.transparency_portal.exception_handling.ProductNotFoundException;
import com.aishwarya.ethical.transparency_portal.modules.product.dto.FilterCriteria;
import com.aishwarya.ethical.transparency_portal.modules.product.dto.PageResponse;
import com.aishwarya.ethical.transparency_portal.modules.product.dto.ProductCategoryDTO;
import com.aishwarya.ethical.transparency_portal.modules.product.dto.ProductDTO;
import com.aishwarya.ethical.transparency_portal.modules.product.model.EthicalItem;
import com.aishwarya.ethical.transparency_portal.modules.product.model.EthicalItemEntity;
import com.aishwarya.ethical.transparency_portal.modules.product.model.IngredientItem;
import com.aishwarya.ethical.transparency_portal.modules.product.model.IngredientItemEntity;
import com.aishwarya.ethical.transparency_portal.modules.product.model.ProductCategory;
import com.aishwarya.ethical.transparency_portal.modules.product.model.ProductModel;
import com.aishwarya.ethical.transparency_portal.modules.product.model.ScoreBreakdown;
import com.aishwarya.ethical.transparency_portal.modules.product.model.TransparencyAnalysis;
import com.aishwarya.ethical.transparency_portal.modules.product.model.TransparencyAnalysisEntity;
import com.aishwarya.ethical.transparency_portal.modules.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductService {
	private static final List<String> REVIEW_TAGS = List.of(
			"Good Quality", "Transparent", "Ethical",
			"Eco Friendly", "Poor Quality",
			"Misleading", "Expensive", "Packaging",
			"Effective", "Good Value");
	private final ProductRepository productRepository;
	private final ObjectMapper objectMapper;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
		this.objectMapper = new ObjectMapper();
	}
	
	/**
	 * Returns all available product categories.
	 */
	public List<ProductCategory> getAllCategories() {
		return Arrays.asList(ProductCategory.values());
	}

	/**
	 * Returns all available product categories with their icons.
	 */
	public List<ProductCategoryDTO> getAllCategoriesWithIcons() {
        return ProductCategory.getAllWithIcons();
    }

	/**
	 * Returns all products for a given category.
	 * @param category Category name (case-insensitive)
	 */
	public List<ProductDTO> getProductsByCategory(String category) {
		if (category == null || category.trim().isEmpty()) {
			throw new IllegalArgumentException("Category must not be empty.");
		}
		ProductCategory cat;
		try {
			cat = ProductCategory.valueOf(category.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new ProductNotFoundException("Invalid category: " + category);
		}
		List<ProductModel> products = productRepository.findByCategory(cat);
		if (products == null || products.isEmpty()) {
			throw new ProductNotFoundException("No products found for category: " + category);
		}
		return products.stream().map(this::toDTO).toList();
	}

	/**
	 * Maps ProductModel to ProductDTO with dynamic data from database entities.
	 */
	private ProductDTO toDTO(ProductModel model) {
		if (model == null) return null;
		return new ProductDTO(
				model.getId(),
				model.getProductName(),
				model.getDescription(),
				model.getImageUrl(),
				model.getBrand(),
				model.getEthicalScore(),
				model.getTransparencyScore(),
				model.getCategory(),
				convertEthicalItemsEntityToDTO(model.getEthicalSummary()),
				convertIngredientsEntityToDTO(model.getIngredients()),
				convertTransparencyAnalysisEntityToDTO(model.getTransparencyAnalysis())
		);
	}
	
	/**
	 * Converts EthicalItemEntity list to EthicalItem list (DTO format).
	 */
	private List<EthicalItem> convertEthicalItemsEntityToDTO(List<EthicalItemEntity> entities) {
		if (entities == null || entities.isEmpty()) {
			return new java.util.ArrayList<>();
		}
		return entities.stream().map(entity -> {
			EthicalItem item = new EthicalItem();
			item.setTitle(entity.getTitle());
			item.setDescription(entity.getDescription());
			item.setIcon(entity.getIcon());
			return item;
		}).toList();
	}
	
	/**
	 * Converts IngredientItemEntity list to IngredientItem list (DTO format).
	 */
	private List<IngredientItem> convertIngredientsEntityToDTO(List<IngredientItemEntity> entities) {
		if (entities == null || entities.isEmpty()) {
			return new java.util.ArrayList<>();
		}
		return entities.stream().map(entity -> {
			IngredientItem item = new IngredientItem();
			item.setName(entity.getName());
			item.setDescription(entity.getDescription());
			item.setSafetyStatus(entity.getSafetyStatus());
			return item;
		}).toList();
	}
	
	/**
	 * Converts TransparencyAnalysisEntity to TransparencyAnalysis (DTO format).
	 */
	private TransparencyAnalysis convertTransparencyAnalysisEntityToDTO(TransparencyAnalysisEntity entity) {
		if (entity == null) {
			return null;
		}
		TransparencyAnalysis analysis = new TransparencyAnalysis();
		
		// Parse JSON strings back to lists
		try {
			if (entity.getScoreHighReasonsJson() != null) {
				List<String> reasons = objectMapper.readValue(
					entity.getScoreHighReasonsJson(),
					objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
				);
				analysis.setScoreHighReasons(reasons);
			}
			if (entity.getImprovementAreasJson() != null) {
				List<String> areas = objectMapper.readValue(
					entity.getImprovementAreasJson(),
					objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
				);
				analysis.setImprovementAreas(areas);
			}
		} catch (Exception e) {
			// If parsing fails, set empty lists
			analysis.setScoreHighReasons(new java.util.ArrayList<>());
			analysis.setImprovementAreas(new java.util.ArrayList<>());
		}
		
		// Convert ScoreBreakdownEntity to ScoreBreakdown
		if (entity.getScoreBreakdown() != null) {
			ScoreBreakdown breakdown = new ScoreBreakdown();
			breakdown.setIngredientTransparency(entity.getScoreBreakdown().getIngredientTransparency());
			breakdown.setEthicalCertifications(entity.getScoreBreakdown().getEthicalCertifications());
			breakdown.setManufacturingInfo(entity.getScoreBreakdown().getManufacturingInfo());
			breakdown.setSourcingTransparency(entity.getScoreBreakdown().getSourcingTransparency());
			analysis.setScoreBreakdown(breakdown);
		}
		
		return analysis;
	}



	/**
	 * Returns all products in the database.
	 * 
	 * @return List of ProductModel
	 */
	// @PreAuthorize("isAuthenticated()")
	public List<ProductDTO> getAllProducts() {
		List<ProductModel> products = productRepository.findAll();
		if (products.isEmpty()) {
			throw new ProductNotFoundException("No products found.");
		}
		return products.stream().map(this::toDTO).toList();
	}

	/**
	 * Searches for products by product name (case-insensitive, partial match).
	 * 
	 * @param name Product name or part of it
	 * @return List of ProductModel
	 */
	// @PreAuthorize("isAuthenticated()")
	public List<ProductDTO> searchProductsByName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Product name must not be empty.");
		}
		List<ProductModel> products = productRepository.findByProductNameContainingIgnoreCase(name);
		if (products == null || products.isEmpty()) {
			throw new ProductNotFoundException("No products found matching: " + name);
		}
		return products.stream().map(this::toDTO).toList();
	}

	/**
	 * Gets a product by its ID.
	 * 
	 * @param id Product ID
	 * @return ProductModel
	 */
	// @PreAuthorize("isAuthenticated()")
	public ProductDTO getProductById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Product ID not found.");
		}
		ProductModel model = productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
		return toDTO(model);
	}

	public List<String> getProductReviewTags(Long productId) {
		if (productId == null) {
			throw new IllegalArgumentException("Product ID not found.");
		}
		if (!productRepository.existsById(productId)) {
			throw new ProductNotFoundException("Product not found with id: " + productId);
		}
		return REVIEW_TAGS;

	}

	// ==================== PAGINATION METHODS ====================

	/**
	 * Get all products with pagination and sorting.
	 * 
	 * @param pageNumber Page number (0-indexed)
	 * @param pageSize Number of products per page
	 * @param sortBy Field to sort by (productName, ethicalScore, transparencyScore)
	 * @param sortDirection Sort direction (ASC or DESC)
	 * @return PageResponse of ProductDTO
	 */
	public PageResponse<ProductDTO> getAllProductsPaginated(int pageNumber, int pageSize, String sortBy, String sortDirection) {
		validatePaginationParams(pageNumber, pageSize);
		Sort.Direction direction = parseSortDirection(sortDirection);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, normalizeSortField(sortBy)));
		
		Page<ProductModel> page = productRepository.findAll(pageable);
		if (page.isEmpty()) {
			throw new ProductNotFoundException("No products found.");
		}
		
		return convertPageToResponse(page, sortBy, sortDirection);
	}

	/**
	 * Get all products with pagination only (default sorting).
	 */
	public PageResponse<ProductDTO> getAllProductsPaginated(int pageNumber, int pageSize) {
		return getAllProductsPaginated(pageNumber, pageSize, "productName", "ASC");
	}

	/**
	 * Get products by category with pagination and sorting.
	 * 
	 */
	public PageResponse<ProductDTO> getProductsByCategoryPaginated(String category, int pageNumber, int pageSize, String sortBy, String sortDirection) {
		if (category == null || category.trim().isEmpty()) {
			throw new IllegalArgumentException("Category must not be empty.");
		}
		validatePaginationParams(pageNumber, pageSize);
		
		ProductCategory cat;
		try {
			cat = ProductCategory.valueOf(category.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new ProductNotFoundException("Invalid category: " + category);
		}
		
		Sort.Direction direction = parseSortDirection(sortDirection);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, normalizeSortField(sortBy)));
		
		Page<ProductModel> page = productRepository.findByCategory(cat, pageable);
		if (page.isEmpty()) {
			throw new ProductNotFoundException("No products found for category: " + category);
		}
		
		return convertPageToResponse(page, sortBy, sortDirection);
	}

	/**
	 * Get products by category with pagination only (default sorting).
	 */
	public PageResponse<ProductDTO> getProductsByCategoryPaginated(String category, int pageNumber, int pageSize) {
		return getProductsByCategoryPaginated(category, pageNumber, pageSize, "productName", "ASC");
	}

	/**
	 * Search products by name with pagination and sorting.
	 */
	public PageResponse<ProductDTO> searchProductsByNamePaginated(String name, int pageNumber, int pageSize, String sortBy, String sortDirection) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Product name must not be empty.");
		}
		validatePaginationParams(pageNumber, pageSize);
		
		Sort.Direction direction = parseSortDirection(sortDirection);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, normalizeSortField(sortBy)));
		
		Page<ProductModel> page = productRepository.findByProductNameContainingIgnoreCase(name, pageable);
		if (page.isEmpty()) {
			throw new ProductNotFoundException("No products found matching: " + name);
		}
		
		return convertPageToResponse(page, sortBy, sortDirection);
	}

	/**
	 * Search products by name with pagination only (default sorting).
	 */
	public PageResponse<ProductDTO> searchProductsByNamePaginated(String name, int pageNumber, int pageSize) {
		return searchProductsByNamePaginated(name, pageNumber, pageSize, "productName", "ASC");
	}

	// ==================== FILTER BY SCORES ====================

	/**
	 * Get products filtered by ethical score range with pagination.
	 */
	public PageResponse<ProductDTO> getProductsByEthicalScoreRange(Double minScore, Double maxScore, int pageNumber, int pageSize) {
		if (minScore == null || maxScore == null || minScore < 0 || maxScore > 100 || minScore > maxScore) {
			throw new IllegalArgumentException("Invalid score range. Min and Max must be between 0-100 and Min <= Max.");
		}
		validatePaginationParams(pageNumber, pageSize);
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "ethicalScore"));
		Page<ProductModel> page = productRepository.findByEthicalScoreRange(minScore, maxScore, pageable);
		
		if (page.isEmpty()) {
			throw new ProductNotFoundException("No products found with ethical score between " + minScore + " and " + maxScore);
		}
		
		return convertPageToResponse(page, "ethicalScore", "DESC");
	}

	/**
	 * Get products filtered by transparency score range with pagination.
	 */
	public PageResponse<ProductDTO> getProductsByTransparencyScoreRange(Double minScore, Double maxScore, int pageNumber, int pageSize) {
		if (minScore == null || maxScore == null || minScore < 0 || maxScore > 100 || minScore > maxScore) {
			throw new IllegalArgumentException("Invalid score range. Min and Max must be between 0-100 and Min <= Max.");
		}
		validatePaginationParams(pageNumber, pageSize);
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "transparencyScore"));
		Page<ProductModel> page = productRepository.findByTransparencyScoreRange(minScore, maxScore, pageable);
		
		if (page.isEmpty()) {
			throw new ProductNotFoundException("No products found with transparency score between " + minScore + " and " + maxScore);
		}
		
		return convertPageToResponse(page, "transparencyScore", "DESC");
	}

	/**
	 * Get products with high ethical score (above threshold).
	 */
	public PageResponse<ProductDTO> getHighEthicalScoreProducts(Double minScore, int pageNumber, int pageSize) {
		if (minScore == null || minScore < 0 || minScore > 100) {
			throw new IllegalArgumentException("Score must be between 0-100.");
		}
		validatePaginationParams(pageNumber, pageSize);
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "ethicalScore"));
		Page<ProductModel> page = productRepository.findByEthicalScoreGreaterThanEqual(minScore, pageable);
		
		if (page.isEmpty()) {
			throw new ProductNotFoundException("No products found with ethical score >= " + minScore);
		}
		
		return convertPageToResponse(page, "ethicalScore", "DESC");
	}

	/**
	 * Get products with high transparency score (above threshold).
	 */
	public PageResponse<ProductDTO> getHighTransparencyScoreProducts(Double minScore, int pageNumber, int pageSize) {
		if (minScore == null || minScore < 0 || minScore > 100) {
			throw new IllegalArgumentException("Score must be between 0-100.");
		}
		validatePaginationParams(pageNumber, pageSize);
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "transparencyScore"));
		Page<ProductModel> page = productRepository.findByTransparencyScoreGreaterThanEqual(minScore, pageable);
		
		if (page.isEmpty()) {
			throw new ProductNotFoundException("No products found with transparency score >= " + minScore);
		}
		
		return convertPageToResponse(page, "transparencyScore", "DESC");
	}

	/**
	 * Get products filtered by both ethical and transparency score ranges.
	 */
	public PageResponse<ProductDTO> getProductsByScoreRange(Double minEthicalScore, Double maxEthicalScore, 
															Double minTransparencyScore, Double maxTransparencyScore,
															int pageNumber, int pageSize) {
		if (minEthicalScore == null || maxEthicalScore == null || minTransparencyScore == null || maxTransparencyScore == null) {
			throw new IllegalArgumentException("All score parameters must not be null.");
		}
		if (minEthicalScore < 0 || maxEthicalScore > 100 || minEthicalScore > maxEthicalScore ||
			minTransparencyScore < 0 || maxTransparencyScore > 100 || minTransparencyScore > maxTransparencyScore) {
			throw new IllegalArgumentException("Invalid score ranges.");
		}
		validatePaginationParams(pageNumber, pageSize);
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "ethicalScore"));
		Page<ProductModel> page = productRepository.findByScoreRange(minEthicalScore, maxEthicalScore, 
																	  minTransparencyScore, maxTransparencyScore, pageable);
		
		if (page.isEmpty()) {
			throw new ProductNotFoundException("No products found matching the score criteria.");
		}
		
		return convertPageToResponse(page, "ethicalScore", "DESC");
	}

	// ==================== FILTER BY BRAND ====================

	/**
	 * Search products by brand with pagination and sorting.
	 */
	public PageResponse<ProductDTO> searchProductsByBrand(String brand, int pageNumber, int pageSize, String sortBy, String sortDirection) {
		if (brand == null || brand.trim().isEmpty()) {
			throw new IllegalArgumentException("Brand name must not be empty.");
		}
		validatePaginationParams(pageNumber, pageSize);
		
		Sort.Direction direction = parseSortDirection(sortDirection);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, normalizeSortField(sortBy)));
		
		Page<ProductModel> page = productRepository.findByBrandContainingIgnoreCase(brand, pageable);
		if (page.isEmpty()) {
			throw new ProductNotFoundException("No products found for brand: " + brand);
		}
		
		return convertPageToResponse(page, sortBy, sortDirection);
	}

	// ==================== ADVANCED FILTERING ====================

	/**
	 * Advanced search with multiple filter criteria.
	 */
	public PageResponse<ProductDTO> advancedSearch(FilterCriteria criteria) {
		if (criteria == null) {
			throw new IllegalArgumentException("Filter criteria must not be null.");
		}
		
		int pageNumber = criteria.getPageNumber() >= 0 ? criteria.getPageNumber() : 0;
		int pageSize = criteria.getPageSize() > 0 ? criteria.getPageSize() : 10;
		validatePaginationParams(pageNumber, pageSize);
		
		Sort.Direction direction = parseSortDirection(criteria.getSortDirection() != null ? criteria.getSortDirection() : "ASC");
		String sortField = normalizeSortField(criteria.getSortBy() != null ? criteria.getSortBy() : "productName");
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));
		
		// Build filter logic based on provided criteria
		Page<ProductModel> page = buildAdvancedFilterQuery(criteria, pageable);
		
		if (page.isEmpty()) {
			throw new ProductNotFoundException("No products found matching the applied filters.");
		}
		
		return convertPageToResponse(page, sortField, direction.toString());
	}

	/**
	 * Get products by category and minimum ethical score.
	 */
	public PageResponse<ProductDTO> getProductsByCategoryAndEthicalScore(String category, Double minEthicalScore, int pageNumber, int pageSize) {
		if (category == null || category.trim().isEmpty()) {
			throw new IllegalArgumentException("Category must not be empty.");
		}
		if (minEthicalScore == null || minEthicalScore < 0 || minEthicalScore > 100) {
			throw new IllegalArgumentException("Ethical score must be between 0-100.");
		}
		validatePaginationParams(pageNumber, pageSize);
		
		ProductCategory cat;
		try {
			cat = ProductCategory.valueOf(category.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new ProductNotFoundException("Invalid category: " + category);
		}
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "ethicalScore"));
		Page<ProductModel> page = productRepository.findByCategoryAndEthicalScoreAbove(cat, minEthicalScore, pageable);
		
		if (page.isEmpty()) {
			throw new ProductNotFoundException("No products found for category: " + category + " with ethical score >= " + minEthicalScore);
		}
		
		return convertPageToResponse(page, "ethicalScore", "DESC");
	}

	// ==================== HELPER METHODS ====================

	/**
	 * Validates pagination parameters.
	 */
	private void validatePaginationParams(int pageNumber, int pageSize) {
		if (pageNumber < 0) {
			throw new IllegalArgumentException("Page number must be >= 0.");
		}
		if (pageSize <= 0 || pageSize > 100) {
			throw new IllegalArgumentException("Page size must be between 1 and 100.");
		}
	}

	/**
	 * Parses sort direction string to Sort.Direction enum.
	 */
	private Sort.Direction parseSortDirection(String direction) {
		if (direction == null || direction.trim().isEmpty()) {
			return Sort.Direction.ASC;
		}
		try {
			return Sort.Direction.fromString(direction.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid sort direction. Use ASC or DESC.");
		}
	}

	/**
	 * Normalizes sort field names to match entity field names.
	 */
	private String normalizeSortField(String sortBy) {
		if (sortBy == null || sortBy.trim().isEmpty()) {
			return "productName";
		}
		
		String field = sortBy.trim().toLowerCase();
		switch (field) {
			case "name":
				return "name";
			case "productname":
				return "productName";
			case "ethicalscore":
				return "ethicalScore";
			case "transparencyscore":
				return "transparencyScore";
			case "brand":
				return "brand";
			case "category":
				return "category";
			default:
				return "productName";
		}
	}

	/**
	 * Converts Spring Page to PageResponse DTO.
	 */
	private PageResponse<ProductDTO> convertPageToResponse(Page<ProductModel> page, String sortBy, String sortDirection) {
		List<ProductDTO> dtos = page.getContent().stream()
			.map(this::toDTO)
			.toList();
		
		PageResponse<ProductDTO> response = new PageResponse<>();
		response.setContent(dtos);
		response.setTotalElements(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());
		response.setCurrentPage(page.getNumber());
		response.setPageSize(page.getSize());
		response.setHasNext(page.hasNext());
		response.setHasPrevious(page.hasPrevious());
		response.setSortedBy(sortBy != null ? sortBy : "productName");
		response.setSortDirection(sortDirection != null ? sortDirection : "ASC");
		
		return response;
	}

	/**
	 * Builds advanced filter query based on FilterCriteria.
	 * This method implements the core filtering logic for advanced search.
	 */
	private Page<ProductModel> buildAdvancedFilterQuery(FilterCriteria criteria, Pageable pageable) {
		// Start with fetching all products with pagination
		Page<ProductModel> page = productRepository.findAll(pageable);
		
		// Apply filters in memory if needed (for complex filtering)
		// Alternatively, custom @Query methods can be created for specific filter combinations
		
		// For now, applying basic filters through existing repository methods
		if (criteria.getMinEthicalScore() != null && criteria.getMaxEthicalScore() != null) {
			page = productRepository.findByEthicalScoreRange(
				criteria.getMinEthicalScore(),
				criteria.getMaxEthicalScore(),
				pageable
			);
		}
		
		if (criteria.getBrand() != null && !criteria.getBrand().isEmpty()) {
			page = productRepository.findByBrandContainingIgnoreCase(criteria.getBrand(), pageable);
		}
		
		if (criteria.getCategory() != null && !criteria.getCategory().isEmpty()) {
			try {
				ProductCategory cat = ProductCategory.valueOf(criteria.getCategory().toUpperCase());
				page = productRepository.findByCategory(cat, pageable);
			} catch (IllegalArgumentException e) {
				// Invalid category, continue with current page
			}
		}
		
		return page;
	}

}
