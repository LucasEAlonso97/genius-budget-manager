package com.genius.budgetmanager.dto;

import com.genius.budgetmanager.model.Campaign;

public record CampaignResponse(
    Long id,
    String name,
    String client,
    String type,
    String status,
    Double budget,
    Double spent,
    String currency,
    String startDate,
    String endDate
) {
    public static CampaignResponse fromModel(Campaign campaign) {
        return new CampaignResponse(
            campaign.getId(),
            campaign.getName(),
            campaign.getClient(),
            campaign.getType(),
            campaign.getStatus(),
            campaign.getBudget(),
            campaign.getSpent(),
            campaign.getCurrency(),
            campaign.getStartDate(),
            campaign.getEndDate()
        );
    }
}