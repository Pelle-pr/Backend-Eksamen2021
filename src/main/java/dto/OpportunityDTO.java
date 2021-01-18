package dto;

import entities.Opportunity;

public class OpportunityDTO {

    private int id;
    private String name;
    private int amount;
    private String closeDate;
    private String status;

    public OpportunityDTO() {
    }

    public OpportunityDTO(Opportunity opportunity) {
        this.id = opportunity.getId();
        this.name = opportunity.getName();
        this.amount = opportunity.getAmount();
        this.closeDate = opportunity.getCloseDate();
        this.status = opportunity.getOpportunityStatus().getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }
}
