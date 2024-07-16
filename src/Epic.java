import java.util.ArrayList;
public class Epic extends Task {
    public ArrayList<Subtask> subTask;

    public Epic(String name, String description, Status status, int id) {
        super(name, description, status, id);
        typeTask = TaskType.EPIC;
        this.subTask = new ArrayList<Subtask>();
    }
    @Override
    public String toString() {
        return "Epic{" +
                "id='" + idTask + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", typeTask='" + typeTask + '\'' +
                ", status=" + status + '\'' +
                ", subtask=" + subTask + '\'' +
                '}';


    }

}