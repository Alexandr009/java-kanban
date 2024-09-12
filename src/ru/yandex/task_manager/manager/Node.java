package ru.yandex.task_manager.manager;

import ru.yandex.task_manager.task.Status;
import ru.yandex.task_manager.task.TaskType;

public class Node {
    int taskId;
    public String name;
    public String description;
    public TaskType typeTask;
    public Status status;
    Node prev;
    Node next;

    public Node (int taskId, String name, String description, TaskType typeTask, Status status){
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.typeTask = typeTask;
        this.status = status;
    }
}
