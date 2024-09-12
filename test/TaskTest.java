import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.task_manager.task.Status;
import ru.yandex.task_manager.task.Task;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    static Task task;

    @BeforeAll
    static void beforeAll(){
        int counter = 1;
        task = new Task("ru.yandex.task_manager.task.Task", "description", Status.NEW, counter);
    }

    @Test
    void addNewTask() {
        System.out.println("" + task);
        HashMap<Integer, Task> listTask = new HashMap<>();
        listTask.put(task.idTask, task);

        final Task savedTask = listTask.get(task.idTask);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void checkEqualsTask(){
        int counter = 2;
        Task taskTwo = new Task("ru.yandex.task_manager.task.Task", "description", Status.NEW, counter);
        assertTrue(taskTwo.equals(task));
    }
    @Test
    void checkToString(){
        String taskParent = "ru.yandex.task_manager.task.Task{id='1'name='ru.yandex.task_manager.task.Task', description='description', typeTask='TASK', status=NEW', startTime=null'}";
        assertEquals(task.toString(), taskParent, "Задачи не совпадают.");
    }

}
