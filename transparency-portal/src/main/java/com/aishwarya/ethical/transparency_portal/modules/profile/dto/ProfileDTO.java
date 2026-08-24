package com.aishwarya.ethical.transparency_portal.modules.profile.dto;

import java.util.List;

public record ProfileDTO(UserProfileDTO user, ActivityDTO activity, List<ReviewDTO> reviews) {
}