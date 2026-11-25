package com.genius.budgetmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Payload para crear una campaña")
public record CampaignRequest(
    @Schema(example = "66")
    Long id,

    @Schema(example = "BlackFriday2023")
    String name,

    @Schema(example = "SuenoSimple")
    String client,

    @Schema(example = "digital")
    String type,

    @Schema(example = "active")
    String status,

    @Schema(example = "2000.00")
    Double budget,

    @Schema(example = "3233.00")
    Double spent,

    @Schema(example = "ARS")
    String currency,

    @Schema(example = "2026-11-20")
    LocalDate startDate,

    @Schema(example = "2026-11-21")
    LocalDate endDate
) {}