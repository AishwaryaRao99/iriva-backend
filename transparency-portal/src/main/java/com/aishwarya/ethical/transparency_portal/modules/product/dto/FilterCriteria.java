package com.aishwarya.ethical.transparency_portal.modules.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for advanced product filtering.
 * Allows filtering products by various criteria like ethical score,
 * transparency score, brand, and ingredient search.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterCriteria {
    private Double minEthicalScore;       // Minimum ethical score (0-100)
    private Double maxEthicalScore;       // Maximum ethical score (0-100)
    private Double minTransparencyScore;  // Minimum transparency score (0-100)
    private Double maxTransparencyScore;  // Maximum transparency score (0-100)
    private String brand;                 // Brand name (partial match)
    private String ingredientName;        // Ingredient name to search for (partial match)
    private String category;              // Product category
    private String sortBy;                // Field to sort by (productName, ethicalScore, transparencyScore)
    private String sortDirection;         // Sort direction: ASC or DESC
    private int pageNumber;               // Page number (0-indexed)
    private int pageSize;                 // Page size
}
