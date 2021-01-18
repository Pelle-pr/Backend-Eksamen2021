package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@NamedQuery(name = "OpportunityStatus.deleteAllRows", query = "DELETE FROM OpportunityStatus ")
public class OpportunityStatus implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "opportunityStatus")
    private List<Opportunity> opportunityList;


    public OpportunityStatus() {
    }

    public OpportunityStatus(String name) {
        this.name = name;
    }

    public void AddOpportunity (Opportunity o){
        if(this.opportunityList == null){
            this.opportunityList = new ArrayList<>();
        }
        this.opportunityList.add(o);
        o.setOpportunityStatus(this);
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
}
