package ru.yandex.task_manager.manager;

import ru.yandex.task_manager.task.Task;

import java.util.List;

public interface HistoryManager {
     void addTaskToHistory(Task task);
     List<Task> getHistory();
     void remove(int id);
}
