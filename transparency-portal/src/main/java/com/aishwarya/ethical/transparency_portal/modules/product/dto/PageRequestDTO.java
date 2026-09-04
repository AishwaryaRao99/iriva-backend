package com.aishwarya.ethical.transparency_portal.modules.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDTO {
	
	@Min(value = 0, message = "Page number must be greater than or equal to 0")
	private int page = 0;
	
	@Min(value = 1, message = "Page size must be at least 1")
	@Max(value = 100, message = "Page size cannot exceed 100 records per page")
	private int size = 10;
	
	@NotBlank(message = "Sort field is required")
	@Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Sort field must contain only alphanumeric characters and underscores")
	private String sortBy = "productName";
	
	@NotBlank(message = "Sort direction is required")
	@Pattern(regexp = "^(ASC|DESC)$", message = "Sort direction must be either ASC or DESC")
	private String sortDirection = "ASC";
}
