import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    static Epic epic;
    static int counter;

    @BeforeAll
    static void beforeAll(){
        counter = 1;
        epic = new Epic("Epic", "description", Status.NEW, counter);
    }

    @Test
    void addNewEpic() {
        System.out.println("" + epic);
        HashMap<Integer, Epic> listEpic = new HashMap<>();
        listEpic.put(epic.idTask, epic);

        final Epic savedEpic = listEpic.get(epic.idTask);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    void checkToString(){
        String taskParent = "Epic{id='1'name='Epic', description='description', typeTask='EPIC', status=NEW', subtask=[]', startTime=null'}";
        assertEquals(epic.toString(), taskParent, "Задачи не совпадают.");
    }


}
