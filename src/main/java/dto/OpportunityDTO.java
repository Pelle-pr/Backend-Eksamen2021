package dto;

import entities.Opportunity;

public class OpportunityDTO {


    private String name;
    private int amount;
    private String closeDate;

    public OpportunityDTO() {
    }

    public OpportunityDTO(Opportunity opportunity) {
        this.name = opportunity.getName();
        this.amount = opportunity.getAmount();
        this.closeDate = closeDate;
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
