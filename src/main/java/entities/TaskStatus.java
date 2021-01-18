package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
public class TaskStatus implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "taskStatus")
    private List<Task> task;

    public TaskStatus() {
    }

    public TaskStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
