import org.junit.jupiter.api.Test;
import ru.yandex.task_manager.task.Epic;
import ru.yandex.task_manager.manager.HistoryManager;
import ru.yandex.task_manager.manager.Managers;
import ru.yandex.task_manager.manager.TaskManager;
import ru.yandex.task_manager.task.Subtask;
import ru.yandex.task_manager.task.Task;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManagersTest {
    @Test
    void returnDefaultTaskManager(){
        HashMap<Integer, ArrayList<Task>> taskOdject = new HashMap<>();
        HashMap<Integer, ArrayList<Epic>> epicOdject = new HashMap<>();
        HashMap<Integer, ArrayList<Subtask>> subtaskOdject = new HashMap<>();
        TaskManager taskManager =  Managers.getDefault(taskOdject, epicOdject, subtaskOdject);
        TaskManager taskManager2 =  Managers.getDefault(taskOdject, epicOdject, subtaskOdject);
        boolean check = taskManager.equals(taskManager2);
        assertTrue(check,"Экземпляры не ровны");
    }
    @Test
    void returnDefaultHistoryManager(){
        HistoryManager historyManager = Managers.getDefaultHistory();
        HistoryManager historyManager2 =  Managers.getDefaultHistory();
        boolean check = historyManager.equals(historyManager2);
        assertTrue(check,"Экземпляры не ровны");
    }
}
