import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.task_manager.manager.Node;
import ru.yandex.task_manager.task.Status;
import ru.yandex.task_manager.task.TaskType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NodeTest {
    static Node node;
    @BeforeAll
    static void beforeAll(){
        int counter = 1;
        node = new Node(counter,"nameTask", "description", TaskType.TASK, Status.NEW);
    }

    @Test
    void addNewTask() {
        Map<Integer,Node> taskMap = new HashMap<>();
        taskMap.put(1, node);
        assertEquals(1,taskMap.size(),"ru.yandex.task_manager.manager.Node не создана.");
    }
}
