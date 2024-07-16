import java.util.Objects;
import java.util.ArrayList;
public class Task extends ArrayList<Task> {
    public String name;
    public String description;
    public int idTask;
    public TaskType typeTask;
    public Status status;

    public Task(String name, String description, Status status, int idTask) {
        this.name = name;
        this.description = description;
        this.typeTask = TaskType.TASK;
        this.status = status;
        this.idTask = idTask;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + idTask + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", typeTask='" + typeTask + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(typeTask, task.typeTask) && status == task.status;
    }

}