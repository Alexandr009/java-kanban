import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    static Task task;

    @BeforeAll
    static void beforeAll(){
        int counter = 1;
        task = new Task("Task", "description", Status.NEW, counter);
    }

    @Test
    void addNewTask() {

        //task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, counter);
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
        Task taskTwo = new Task("Task", "description", Status.NEW, counter);
        //boolean chexck = taskTwo.equals(task);
        assertTrue(taskTwo.equals(task));
    }
    //Task{id='2'name='Task', description='description', typeTask='Task', status=NEW}

    @Test
    void checkToString(){
        String taskParent = "Task{id='1'name='Task', description='description', typeTask='Task', status=NEW}";
        assertEquals(task.toString(), taskParent, "Задачи не совпадают.");
    }

}
