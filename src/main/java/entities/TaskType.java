package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@NamedQuery(name = "TaskType.deleteAllRows", query = "DELETE FROM TaskType ")
public class TaskType implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "taskType")
    private List<Task> task;

    public TaskType() {
    }

    public TaskType(String name) {
        this.name = name;
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
