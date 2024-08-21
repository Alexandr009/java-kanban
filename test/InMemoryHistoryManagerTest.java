import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryHistoryManagerTest {

    static Task task1;
    static Task task2;
    static Task task3;
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
    void checkListHistoryDouble(){
        inMemoryHistoryManager.addTaskToHistory(task1);
        inMemoryHistoryManager.addTaskToHistory(task2);
        inMemoryHistoryManager.addTaskToHistory(task1);
        List<Task> task = inMemoryHistoryManager.getHistory();
        assertEquals(2, task.size(), "Неверное количество задач.");
    }
    @Test
    void deleteTaskToHistory(){
        inMemoryHistoryManager.remove(task1.idTask);
        List<Task> task = inMemoryHistoryManager.getHistory();
        assertEquals(1, task.size(), "Задача не удалена из истории.");
    }

    @Test
    void emptyHistory(){
        inMemoryHistoryManager.remove(task1.idTask);
        inMemoryHistoryManager.remove(task2.idTask);
        List<Task> task = inMemoryHistoryManager.getHistory();
        assertEquals(0, task.size(), "История не пустая.");
    }

    @Test
    void deleteFirstTaskToHistory(){
        inMemoryHistoryManager.remove(task1.idTask);
        List<Task> listHistory = inMemoryHistoryManager.getHistory();
        assertTrue(listHistory.stream().noneMatch(task -> task.idTask == task1.idTask), "Первый таск не удален из истории.");
    }

    @Test
    void deleteLastTaskToHistory() {
        Task task3 = new Task("Task3", "description", Status.NEW, 3);
        inMemoryHistoryManager.addTaskToHistory(task3);
        inMemoryHistoryManager.remove(task3.idTask);
        List<Task> listHistory = inMemoryHistoryManager.getHistory();
        assertEquals(2, listHistory.size(), "Последняя задача не удалена из истории.");
    }

}
