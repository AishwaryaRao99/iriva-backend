package com.aishwarya.ethical.transparency_portal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.aishwarya.ethical.transparency_portal.modules.product.model.ProductModel;
import com.aishwarya.ethical.transparency_portal.modules.product.model.ProductCategory;
import com.aishwarya.ethical.transparency_portal.modules.product.dto.ProductDTO;
import com.aishwarya.ethical.transparency_portal.modules.product.repository.ProductRepository;
import com.aishwarya.ethical.transparency_portal.modules.product.service.ProductService;

@SpringBootTest
class TransparencyPortalApplicationTests {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductService productService;

	@Test
	void contextLoads() {
	}

	@Test
	@Transactional
	void seededProductsHaveQualityLevelsInsightsAndInciNames() {
		var products = productRepository.findAll();
		Set<String> validLevels = Set.of("High", "Moderate", "Low");

		assertEquals(16, products.size());
		for (ProductModel product : products) {
			assertTrue(validLevels.contains(product.getIngredientSafety()));
			assertTrue(validLevels.contains(product.getInformationQuality()));
			assertTrue(validLevels.contains(product.getEvidenceQuality()));
			assertNotNull(product.getProductInsights());
			assertNotNull(product.getProductInsights().getFindingsJson());
			assertNotNull(product.getProductInsights().getSourcesJson());
			assertTrue(!product.getIngredients().isEmpty());
			if (product.getCategory() == ProductCategory.SKINCARE) {
				assertTrue(product.getIngredients().stream().anyMatch(
					ingredient -> ingredient.getInciName() != null && !ingredient.getInciName().isBlank()));
			}
		}

		ProductDTO product = productService.getProductById(1L);
		assertEquals("High", product.getIngredientSafety());
		assertEquals("High", product.getInformationQuality());
		assertEquals("Moderate", product.getEvidenceQuality());
		assertNotNull(product.getProductInsights());
		assertTrue(product.getProductInsights().getFindings().contains("Ingredient list available"));
		assertTrue(product.getIngredients().stream().anyMatch(
			ingredient -> "Punica Granatum Seed Oil".equals(ingredient.getInciName())));
	}

}
