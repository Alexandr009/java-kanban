package ru.yandex.task_manager.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ArrayList;
public class Task {

    public String name;
    public String description;
    public int idTask;
    public TaskType typeTask;
    public Status status;
    public Duration duration;
    public LocalDateTime startTime;

    public Task(String name, String description, Status status, int idTask) {
        this.name = name;
        this.description = description;
        this.typeTask = TaskType.TASK;
        this.status = status;
        this.idTask = idTask;
    }

    public LocalDateTime getEndTime(){
        long durationMin = duration.getSeconds() / 60;
        LocalDateTime endTime = startTime.plusMinutes(durationMin);
        return endTime;
    }
    public void setStartTime(){
        this.startTime = LocalDateTime.now();
    }
    public void setDuration(){
        this.duration = Duration.between(this.startTime,LocalDateTime.now());
    }

    @Override
    public String toString() {
        String text = "";
        if(this.status != Status.DONE ) {
            text = "Task{" +
                    "id='" + idTask + '\'' +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", typeTask='" + typeTask + '\'' +
                    ", status=" + status + '\'' +
                    ", startTime=" + startTime + '\'' +
                    '}';
        } else if (this.status == Status.DONE) {
            LocalDateTime endTime = getEndTime();
            text = "Task{" +
                    "id='" + idTask + '\'' +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", typeTask='" + typeTask + '\'' +
                    ", status=" + status + '\'' +
                    ", startTime=" + startTime + '\'' +
                    ", endTime=" + endTime +
                    '}';
        }

        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(typeTask, task.typeTask) && status == task.status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getIdTask() {
        return idTask;
    }

    public TaskType getTypeTask() {
        return typeTask;
    }

    public Status getStatus() {
        return status;
    }

}