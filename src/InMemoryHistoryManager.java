import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
public class InMemoryHistoryManager implements HistoryManager{
    public List<Task> listHistory;
    public final  Map<Integer,Node> taskMap = new HashMap<>();
    private Node head;
    private Node tail;
    public InMemoryHistoryManager(){

        listHistory = new ArrayList<Task>(10);
    }
    private void linkLast(Task task){
        Node newNode = new Node(task.idTask,task.name, task.description,task.typeTask,task.status);//
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        taskMap.put(task.idTask, newNode);
    }
    @Override
    public List<Task> getHistory(){
        listHistory = new ArrayList<>();
        Node current = head;
            while (current != null){
                listHistory.add(new Task(current.name,current.description,current.status,current.taskId));
                current = current.next;
            }
            return listHistory;
    }

    @Override
    public void remove(int id) {
        Node node = taskMap.get(id);
        if(node != null){
            removeNode(node);
        }
    }

    private void removeNode(Node node){//
        if(node == head){
            head = head.next;
            if(head != null){
                head.prev = null;
            }
        }else{
            node.prev.next = node.next;
        }

        if (node == tail){
            tail = tail.prev;
            if(tail != null){
                tail.next = null;
            }
        }else {
            if(node.next != null){
                node.next.prev = node.prev;
            }
        }

        node.prev = null;
        node.next = null;
        taskMap.remove(node.taskId);
    }

    @Override
    public void addTaskToHistory(Task task){
      if (taskMap.containsKey(task.idTask)){
          removeNode(taskMap.get(task.idTask));
      }
      if (taskMap.size() == 10){
          removeNode(head);
      }
      linkLast(task);

    }
}