package com.aishwarya.ethical.transparency_portal.modules.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for advanced product filtering.
 * Allows filtering products by brand, category, and ingredient search.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterCriteria {
    private String brand;                 // Brand name (partial match)
    private String ingredientName;        // Ingredient name to search for (partial match)
    private String category;              // Product category
    private String sortBy;                // Field to sort by (productName, brand, category)
    private String sortDirection;         // Sort direction: ASC or DESC
    private int pageNumber;               // Page number (0-indexed)
    private int pageSize;                 // Page size
}
