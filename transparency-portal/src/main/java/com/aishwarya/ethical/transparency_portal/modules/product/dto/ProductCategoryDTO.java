package com.aishwarya.ethical.transparency_portal.modules.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for transferring product category and its icon.
 */
public class ProductCategoryDTO {
    
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String category;
    
    @NotBlank(message = "Category icon is required")
    @Size(max = 255, message = "Icon URL cannot exceed 255 characters")
    private String icon;

    public ProductCategoryDTO(String category, String icon) {
        this.category = category;
        this.icon = icon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
