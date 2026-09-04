package com.aishwarya.ethical.transparency_portal.modules.product.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productName;

	private String description;

	private String imageUrl;

	private String brand;

	private double ethicalScore;

	private double transparencyScore;

	@Enumerated(EnumType.STRING)
	private ProductCategory category;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EthicalItemEntity> ethicalSummary;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IngredientItemEntity> ingredients;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "transparency_analysis_id", foreignKey = @ForeignKey(name = "fk_product_transparency_analysis"))
	private TransparencyAnalysisEntity transparencyAnalysis;    

}