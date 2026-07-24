package com.aishwarya.ethical.transparency_portal.modules.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDTO {
	private int page = 0;
    private int size = 10;
    private String sortBy = "productName";
    private String sortDirection = "ASC";
}
