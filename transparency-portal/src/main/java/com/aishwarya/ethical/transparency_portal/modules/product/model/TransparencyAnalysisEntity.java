package com.aishwarya.ethical.transparency_portal.modules.product.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transparency_analyses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransparencyAnalysisEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	private String scoreHighReasonsJson;

	@Lob
	private String improvementAreasJson;

	@OneToOne
	@JoinColumn(name = "score_breakdown_id", nullable = false, foreignKey = @ForeignKey(name = "fk_transparency_analysis_score_breakdown"))
	private ScoreBreakdownEntity scoreBreakdown;

	@OneToOne(mappedBy = "transparencyAnalysis")
	private ProductModel product;

}
