package com.genius.budgetmanager.service;

import com.genius.budgetmanager.model.BudgetSummary;
import com.genius.budgetmanager.model.Campaign;
import com.genius.budgetmanager.model.Expense;
import com.genius.budgetmanager.model.GlobalBudgetSummary;
import com.genius.budgetmanager.dto.CampaignRequest;
import com.genius.budgetmanager.dto.CampaignResponse;
import com.genius.budgetmanager.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository repository;

    public List<Campaign> getAllCampaigns() {
        return repository.findAll();
    }

    public List<Campaign> getCampaignsByStatus(String status) {
        return repository.findAll().stream()
                .filter(campaign -> campaign.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public List<Campaign> getCampaignsByClient(String client) {
        return repository.findAll().stream()
            .filter(campaign -> campaign.getClient().equalsIgnoreCase(client))
            .collect(Collectors.toList());
    }

    public Campaign getCampaignById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found: " + id));
    }

    public BudgetSummary getBudgetSummary(Long campaignId) {
        Campaign campaign = getCampaignById(campaignId);

        BudgetSummary summary = new BudgetSummary();
        summary.setCampaignId(campaign.getId());
        summary.setCampaignName(campaign.getName());
        summary.setClient(campaign.getClient());
        summary.setTotalBudget(campaign.getBudget());
        summary.setSpent(campaign.getSpent());
        summary.setRemaining(campaign.getBudget() - campaign.getBudget());
        summary.setPercentageUsed(
                Math.round((campaign.getSpent() / campaign.getBudget()) * 10000.0) / 100.0
        );

        return summary;
    }

    public List<Expense> getExpensesByCampaign(Long campaignId) {
        getCampaignById(campaignId);
        return repository.findExpensesByCampaignId(campaignId);
    }

    public Expense addExpense(Long campaignId, Expense expense) {
        Campaign campaign = getCampaignById(campaignId);
        expense.setCampaignId(campaignId);
        campaign.setSpent(campaign.getSpent() + expense.getAmount());
        return repository.saveExpense(expense);
    }

    public GlobalBudgetSummary getGlobalBudgetSummary() {
        List<Campaign> active = repository.findAll().stream()
                .filter(c -> "active".equalsIgnoreCase(c.getStatus()))
                .collect(Collectors.toList());

        double totalBudget    = active.stream().mapToDouble(Campaign::getBudget).sum();
        double totalSpent     = active.stream().mapToDouble(Campaign::getSpent).sum();
        double totalAvailable = totalBudget - totalSpent;
        double pct = totalBudget > 0
                ? Math.round((totalSpent / totalBudget) * 10000.0) / 100.0
                : 0.0;

        GlobalBudgetSummary summary = new GlobalBudgetSummary();
        summary.setActiveCampaigns(active.size());
        summary.setTotalBudget(totalBudget);
        summary.setTotalSpent(totalSpent);
        summary.setTotalAvailable(totalAvailable);
        summary.setConsumptionPercentage(pct);
        return summary;
    }

    public Campaign updateBudget(Long campaignId, Double newBudget) {
        Campaign campaign = getCampaignById(campaignId);
        campaign.setSpent(0.0);
        campaign.setBudget(newBudget);
        return campaign;
    }

    public CampaignResponse create(CampaignRequest request) {
        validate(request);

        Double spentValue = (request.spent() != null) ? request.spent() : 0.0;

        String startDateStr = request.startDate().toString();
        String endDateStr = (request.endDate() != null) ? request.endDate().toString() : null;

        String statusValue = (request.status() != null && !request.status().isBlank())
            ? request.status().trim().toLowerCase()
            : "active";

        Campaign newCampaign = new Campaign(
            request.id(),
            request.name().trim(),
            request.client().trim(),
            request.type().trim(),
            request.status().trim(),
            request.budget(),
            spentValue,
            request.currency().trim().toUpperCase(),
            startDateStr,
            endDateStr
        );

        Campaign saved = repository.save(newCampaign);
        return CampaignResponse.fromModel(saved);
    }

    private void validate(CampaignRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'name' es obligatorio");
        }
        if (request.client() == null || request.client().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'client' es obligatorio");
        }
        if (request.type() == null || request.type().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'type' es obligatorio");
        }
        if (request.budget() == null || request.budget() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'budget' debe ser mayor a cero");
        }
        if (request.spent() != null && request.spent() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'spent' no puede ser negativo");
        }
        if (request.currency() == null || request.currency().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'currency' es obligatorio");
        }
        if (request.startDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'startDate' es obligatorio");
        }
        if (request.endDate() != null && request.endDate().isBefore(request.startDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha 'endDate' no puede ser anterior a 'startDate'");
        }
    }
}
