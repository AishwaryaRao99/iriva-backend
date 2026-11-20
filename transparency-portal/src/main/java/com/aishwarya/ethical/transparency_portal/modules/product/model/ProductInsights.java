package com.aishwarya.ethical.transparency_portal.modules.product.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInsights {
	private String informationQualitySummary;
	private String evidenceQualitySummary;
	private List<String> findings;
	private List<String> sources;
}