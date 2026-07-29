package com.aishwarya.ethical.transparency_portal.modules.product.dto;

import java.util.List;
import com.aishwarya.ethical.transparency_portal.modules.product.model.EthicalItem;
import com.aishwarya.ethical.transparency_portal.modules.product.model.IngredientItem;
import com.aishwarya.ethical.transparency_portal.modules.product.model.ProductCategory;
import com.aishwarya.ethical.transparency_portal.modules.product.model.TransparencyAnalysis;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    private String productName;
    
    @NotBlank(message = "Product description is required")
    @Size(min = 10, max = 2000, message = "Product description must be between 10 and 2000 characters")
    private String description;
    
    @NotBlank(message = "Product image URL is required")
    private String imageUrl;
    
    @NotBlank(message = "Brand name is required")
    @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
    private String brand;
    
    @NotNull(message = "Ethical score is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Ethical score must be at least 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Ethical score cannot exceed 100")
    private double ethicalScore;
    
    @NotNull(message = "Transparency score is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Transparency score must be at least 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Transparency score cannot exceed 100")
    private double transparencyScore;
    
    @NotNull(message = "Product category is required")
    private ProductCategory category;
    
    private List<EthicalItem> ethicalSummary;
    
    private List<IngredientItem> ingredients;
    
    private TransparencyAnalysis transparencyAnalysis;
}
