package com.aishwarya.ethical.transparency_portal.modules.product.dto;

import java.util.List;
import com.aishwarya.ethical.transparency_portal.modules.product.model.EthicalItem;
import com.aishwarya.ethical.transparency_portal.modules.product.model.IngredientItem;
import com.aishwarya.ethical.transparency_portal.modules.product.model.ProductCategory;
import com.aishwarya.ethical.transparency_portal.modules.product.model.ProductInsights;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    
    public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@NotBlank(message = "Brand name is required")
    @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
    private String brand;
    
    @NotBlank(message = "Ingredient safety is required")
    @Pattern(regexp = "High|Moderate|Low", message = "Ingredient safety must be High, Moderate, or Low")
    private String ingredientSafety;

    @NotBlank(message = "Information quality is required")
    @Pattern(regexp = "High|Moderate|Low", message = "Information quality must be High, Moderate, or Low")
    private String informationQuality;

    @NotBlank(message = "Evidence quality is required")
    @Pattern(regexp = "High|Moderate|Low", message = "Evidence quality must be High, Moderate, or Low")
    private String evidenceQuality;
    
    @NotNull(message = "Product category is required")
    private ProductCategory category;
    
    private List<EthicalItem> ethicalSummary;
    
    private List<IngredientItem> ingredients;
    
    private ProductInsights productInsights;
}
