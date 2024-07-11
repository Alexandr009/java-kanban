public class Node {
    int taskId;
    public String name;
    public String description;
    public String typeTask;
    public Status status;
    Node prev;
    Node next;

    public Node (int taskId,String name,String description,String typeTask,Status status){
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.typeTask = typeTask;
        this.status = status;
    }
}
