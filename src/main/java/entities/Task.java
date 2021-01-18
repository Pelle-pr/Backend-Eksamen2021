package entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Task implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Column(length = 255)
    private String comment;

    private String dueDate;

    @ManyToOne
    private Opportunity opportunity;

    @ManyToOne
    private TaskStatus taskStatus;

    @ManyToOne
    private TaskType taskType;

    public Task() {
    }

    public Task(String title, String comment, String dueDate) {
        this.title = title;
        this.comment = comment;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

}
