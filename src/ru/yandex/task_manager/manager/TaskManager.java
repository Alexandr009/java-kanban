package ru.yandex.task_manager.manager;

import ru.yandex.task_manager.task.Epic;
import ru.yandex.task_manager.task.Status;
import ru.yandex.task_manager.task.Subtask;
import ru.yandex.task_manager.task.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    /////////////////Common////////////////////////////
    ///////////////////ru.yandex.task_manager.task.Subtask////////////////////////////////////
    int addSubtask(String name, String description, Status status, int idEpic);

    Subtask getSubtask(int id);

    void deleteAllSubtask();

    HashMap getAllSubtask();

    Subtask updateSubtask(int id, String name, String description, Status status);

    void deleteSubtask(int id);

    ///////////////////epic////////////////////////////////////
    int addEpic(String name, String description, Status status);

    Epic getEpic(int id);

    void deleteAllEpic();

    HashMap getAllEpic();

    void deleteEpic(int id);

    void updateEpic(int id, String name, String description);

    Epic updateEpicStatus(int id, Status status);

    int addTask(String name, String description, Status status);

    HashMap getAllTask();

    Task getTask(int id);

    void deleteAllTask();

    void updateTask(int id, String name, String description, Status status);

    void deleteTask(int id);

    List<Task> getPrioritizedTasks();
    //public boolean checkPrioritizedTasks();

    @Override
    String toString();
}
