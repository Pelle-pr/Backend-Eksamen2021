package dto;

import entities.Task;

public class TaskDTO {

    private String title;
    private String comment;
    private String dueDate;
    private String taskType;
    private String taskStatus;


    public TaskDTO() {
    }

    public TaskDTO(Task t) {
        this.title = t.getTitle();
        this.comment = t.getComment();
        this.dueDate = t.getDueDate();
        this.taskType = t.getTaskType().getName();
        this.taskStatus = t.getTaskStatus().getName();
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}
