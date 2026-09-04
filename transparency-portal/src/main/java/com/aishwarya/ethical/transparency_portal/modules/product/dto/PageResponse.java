package com.aishwarya.ethical.transparency_portal.modules.product.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic pagination response wrapper for paginated API responses.
 * Provides metadata about the page along with the content.
 * 
 * @param <T> Generic type for page content
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;           // The actual page content
    private long totalElements;        // Total number of elements across all pages
    private int totalPages;            // Total number of pages
    private int currentPage;           // Current page number (0-indexed)
    private int pageSize;              // Number of elements in this page
    private boolean hasNext;           // Whether there's a next page
    private boolean hasPrevious;       // Whether there's a previous page
    private String sortedBy;           // Field being sorted on
    private String sortDirection;      // Sort direction (ASC or DESC)
    
    /**
     * Constructor for creating a PageResponse from Spring Page object
     */
    public static <T> PageResponse<T> from(org.springframework.data.domain.Page<T> page, String sortedBy, String sortDirection) {
        PageResponse<T> response = new PageResponse<>();
        response.setContent(page.getContent());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setCurrentPage(page.getNumber());
        response.setPageSize(page.getSize());
        response.setHasNext(page.hasNext());
        response.setHasPrevious(page.hasPrevious());
        response.setSortedBy(sortedBy);
        response.setSortDirection(sortDirection);
        return response;
    }
}
