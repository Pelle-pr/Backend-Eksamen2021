package dto;

import entities.TaskStatus;
import entities.TaskType;

public class TaskStatusDTO {

    private int id;
    private String name;

    public TaskStatusDTO() {
    }

    public TaskStatusDTO(TaskStatus t) {
        this.id = t.getId();
        this.name = t.getName();
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
