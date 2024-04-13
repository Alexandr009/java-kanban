import java.util.HashMap;
import java.util.List;

public final class Managers {
    public static HistoryManager historyManager;// = new InMemoryHistoryManager();
    public static TaskManager taskManager; //= new InMemoryTaskManager();

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
