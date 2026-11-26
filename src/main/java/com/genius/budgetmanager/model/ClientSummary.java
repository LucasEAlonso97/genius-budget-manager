package com.genius.budgetmanager.model;

public class ClientSummary {

    private String client;
    private Double totalBudget;
    private Double totalSpent;
    private Double totalAvailable;
    public Integer activeCampaigns;

    public ClientSummary() {}

    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }

    public Double getTotalBudget() { return totalBudget; }
    public void setTotalBudget(Double totalBudget) { this.totalBudget = totalBudget; }

    public Double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(Double totalSpent) { this.totalSpent = totalSpent; }

    public Double getTotalAvailable() { return totalAvailable; }
    public void setTotalAvailable(Double totalAvailable) { this.totalAvailable = totalAvailable; }

    public Integer getActiveCampaigns() { return activeCampaigns; }
    public void setActiveCampaigns(Integer activeCampaigns) { this.activeCampaigns = activeCampaigns; }
}
