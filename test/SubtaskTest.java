import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubtaskTest {
    static Subtask subtask;

    @BeforeAll
    static void beforeAll(){
        int counter = 2;
        subtask = new Subtask("Subtask", "description", Status.NEW, counter,1);
    }

    @Test
    void addNewSubtask() {
        System.out.println("" + subtask);
        HashMap<Integer, Subtask> listSubtask = new HashMap<>();
        listSubtask.put(subtask.idTask, subtask);

        final Subtask savedSubtask = listSubtask.get(subtask.idTask);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
    }

    @Test
    void checkToString(){
        String taskParent = "Subtask{id='2'name='Subtask', description='description', typeTask='SUBTASK', status=NEW', idEpic=1'}";
        assertEquals(subtask.toString(), taskParent, "Задачи не совпадают.");
    }
}
