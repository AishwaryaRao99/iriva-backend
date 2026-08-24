package com.aishwarya.ethical.transparency_portal.modules.profile.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ReviewRequest(
        @NotNull @Min(1) @Max(5) Integer rating,
        @NotBlank @Size(max = 2000) String comment,
        @Size(max = 5) List<@NotBlank @Size(max = 50) String> tags) {
}