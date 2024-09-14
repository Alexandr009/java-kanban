package ru.yandex.task_manager.task;

import java.time.LocalDateTime;

public class Subtask extends Task {
    public int idEpic;


    public Subtask(String name, String description, Status status, int id, int epicId) {
        super(name, description, status, id);
        typeTask = TaskType.SUBTASK;
        this.idEpic = epicId;
    }

    @Override
    public String toString() {
    String text = "";
        if(this.status != Status.DONE ) {
            text = "Subtask{" +
                    "id='" + idTask + '\'' +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", typeTask='" + typeTask + '\'' +
                    ", status=" + status + '\'' +
                    ", idEpic=" + idEpic + '\'' +
                    ", startTime=" + startTime + '\'' +//
                    '}';
        } else if (this.status == Status.DONE) {
            LocalDateTime endTime = getEndTime();
            text = "Subtask{" +
                "id='" + idTask + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", typeTask='" + typeTask + '\'' +
                ", status=" + status + '\'' +
                ", idEpic=" + idEpic + '\'' +
                    ", startTime=" + startTime + '\'' +
                    ", endTime=" + endTime +
                    '}';
        }

        return text;
    }
}
