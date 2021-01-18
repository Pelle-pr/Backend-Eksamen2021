package entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@NamedQuery(name = "Opportunity.deleteAllRows", query = "DELETE FROM Opportunity ")
public class Opportunity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int amount;
    private String CloseDate;

    @ManyToOne
    private Contact contact;

    @ManyToOne
    private OpportunityStatus opportunityStatus;

    public Opportunity() {
    }

    public Opportunity(String name, int amount, String closeDate) {
        this.name = name;
        this.amount = amount;
        this.CloseDate = closeDate;
    }

    public OpportunityStatus getOpportunityStatus() {
        return opportunityStatus;
    }

    public void setOpportunityStatus(OpportunityStatus opportunityStatus) {
        this.opportunityStatus = opportunityStatus;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return CloseDate;
    }

    public void setCloseDate(String closeDate) {
        CloseDate = closeDate;
    }
}
