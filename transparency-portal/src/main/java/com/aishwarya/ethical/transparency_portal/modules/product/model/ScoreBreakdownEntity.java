package com.aishwarya.ethical.transparency_portal.modules.product.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "score_breakdowns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreBreakdownEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int ingredientTransparency;

	private int ethicalCertifications;

	private int manufacturingInfo;

	private int sourcingTransparency;

	@OneToOne(mappedBy = "scoreBreakdown")
	private TransparencyAnalysisEntity transparencyAnalysis;

}
