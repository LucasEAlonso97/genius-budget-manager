package com.genius.budgetmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Payload para crear una campaña")
public record CampaignRequest(
    @Schema(example = "66")
    Long id,

    @Schema(example = "campaign 12")
    String name,

    @Schema(example = "ss")
    String client,

    @Schema(example = "dk")
    String type,

    @Schema(example = "ACTIVE")
    String status,

    @Schema(example = "333")
    Double budget,

    @Schema(example = "3233")
    Double spent,

    @Schema(example = "ARS")
    String currency,

    @Schema(example = "2026-11-20")
    LocalDate startDate,

    @Schema(example = "2026-11-21")
    LocalDate endDate
) {}