public class Subtask extends Task {
    public int idEpic;


    public Subtask(String name, String description, Status status, int id, int epicId) {
        super(name, description, status, id);
        typeTask = TaskType.SUBTASK;
        this.idEpic = epicId;
    }

    @Override
    public String toString() {

        return "Subtask{" +
                "id='" + idTask + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", typeTask='" + typeTask + '\'' +
                ", status=" + status + '\'' +
                ", idEpic=" + idEpic + '\'' +
                '}';

    }
}
