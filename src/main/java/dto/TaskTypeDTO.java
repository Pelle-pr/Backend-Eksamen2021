package dto;

import entities.TaskType;

public class TaskTypeDTO {

    private int id;
    private String name;

    public TaskTypeDTO() {
    }

    public TaskTypeDTO(TaskType t) {
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
