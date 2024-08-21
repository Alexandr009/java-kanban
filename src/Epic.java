import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class Epic extends Task {
    public ArrayList<Subtask> subTask;
    public LocalDateTime endTime;

    public Epic(String name, String description, Status status, int id) {
        super(name, description, status, id);
        typeTask = TaskType.EPIC;
        this.subTask = new ArrayList<Subtask>();
    }
    @Override
    public void setStartTime(){
        Optional<Subtask> earliestSubtask = this.subTask.stream().min(Comparator.comparing(tasks -> tasks.startTime));
        //находим минимальное значение по startTime с использованием компаратора
        this.startTime = earliestSubtask.get().startTime;
    }

    @Override
    public LocalDateTime getEndTime(){

        Optional<LocalDateTime> latestEndTime = this.subTask.stream()
                .map(subtask -> subtask.startTime.plusMinutes(subtask.duration.getSeconds() / 60))
                .max(Comparator.naturalOrder());

        endTime = latestEndTime.get();
        return endTime;
    }
    @Override
    public String toString() {
        String text = "";
        if(this.status != Status.DONE ) {
            text =  "Epic{" +
                "id='" + idTask + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", typeTask='" + typeTask + '\'' +
                ", status=" + status + '\'' +
                ", subtask=" + subTask + '\'' +
                    ", startTime=" + startTime + '\'' +//
                '}';
        } else if (this.status == Status.DONE) {
            LocalDateTime endTime = getEndTime();
            text = "Epic{" +
                    "id='" + idTask + '\'' +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", typeTask='" + typeTask + '\'' +
                    ", status=" + status + '\'' +
                    ", startTime=" + startTime + '\'' +
                    ", endTime=" + endTime +
                    ", subtask=" + subTask + '\'' +
                    '}';
        }
        return text;
    }

}