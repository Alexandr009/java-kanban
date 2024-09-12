package ru.yandex.task_manager.manager;

import java.util.HashMap;

public final class Managers {
    public static HistoryManager historyManager;
    public static TaskManager taskManager;

    public static HistoryManager getDefaultHistory(){
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }
    public static TaskManager getDefault(HashMap listTask, HashMap listEpic, HashMap listSubtask){
        if (taskManager == null) {
            taskManager = new InMemoryTaskManager(listTask,listEpic,listSubtask);
        }
        return taskManager;
    }

}
