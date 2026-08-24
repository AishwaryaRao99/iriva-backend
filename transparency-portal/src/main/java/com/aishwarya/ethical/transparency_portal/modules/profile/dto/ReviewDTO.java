package com.aishwarya.ethical.transparency_portal.modules.profile.dto;

import java.util.List;

public record ReviewDTO(Long id, String title, String brand, Integer rating, String time,
	String text, List<String> tags, String image) {
}