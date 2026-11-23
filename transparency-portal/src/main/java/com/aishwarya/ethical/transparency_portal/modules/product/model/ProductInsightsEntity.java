package com.aishwarya.ethical.transparency_portal.modules.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_insights")
@Getter
@Setter
@NoArgsConstructor
public class ProductInsightsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "jsonb")
	private String informationQualitySummary;

	@Column(columnDefinition = "jsonb")
	private String evidenceQualitySummary;

	@Column(columnDefinition = "jsonb")
	private String findingsJson;

	@Column(columnDefinition = "jsonb")
	private String sourcesJson;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false, unique = true,
			foreignKey = @ForeignKey(name = "fk_product_insights_product"))
	private ProductModel product;
}