import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    static Task task1;
    static Task task2;
    static InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    void beforeEach(){
        inMemoryHistoryManager = new InMemoryHistoryManager();
        int counter = 1;
         task1 = new Task("Task1", "description", Status.NEW, counter);
        inMemoryHistoryManager.addTaskToHistory(task1);
        counter++;
         task2 = new Task("Task2", "description", Status.NEW, counter);
        inMemoryHistoryManager.addTaskToHistory(task2);
    }

    @Test
    void checkGetHistory2Task(){
        List<Task> listHistory = inMemoryHistoryManager.getHistory();
        assertEquals(2, listHistory.size(), "Неверное количество задач.");

    }

    @Test
    void checkListHistoryLength10Limit(){
        inMemoryHistoryManager.addTaskToHistory(task1);
        inMemoryHistoryManager.addTaskToHistory(task2);
        inMemoryHistoryManager.addTaskToHistory(task1);
        inMemoryHistoryManager.addTaskToHistory(task2);
        inMemoryHistoryManager.addTaskToHistory(task1);
        inMemoryHistoryManager.addTaskToHistory(task2);
        inMemoryHistoryManager.addTaskToHistory(task1);
        inMemoryHistoryManager.addTaskToHistory(task2);
        inMemoryHistoryManager.addTaskToHistory(task1);
        inMemoryHistoryManager.addTaskToHistory(task2);
        assertEquals(10, inMemoryHistoryManager.listHistory.size(), "Неверное количество задач.");
    }


}
