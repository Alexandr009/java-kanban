import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NodeTest {
    static Node node;
    @BeforeAll
    static void beforeAll(){
        int counter = 1;
        node = new Node(counter,"nameTask", "description","Task",Status.NEW);
    }

    @Test
    void addNewTask() {
        Map<Integer,Node> taskMap = new HashMap<>();
        taskMap.put(1, node);
        assertEquals(1,taskMap.size(),"Нода не создана.");
    }
}
