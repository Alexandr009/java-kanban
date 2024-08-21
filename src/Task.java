import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ArrayList;
public class Task extends ArrayList<Task> {
    public String name;
    public String description;
    public int idTask;
    public TaskType typeTask;
    public Status status;
    public Duration duration;
    public LocalDateTime startTime;

    public Task(String name, String description, Status status, int idTask) {
        this.name = name;
        this.description = description;
        this.typeTask = TaskType.TASK;
        this.status = status;
        this.idTask = idTask;
    }

    public LocalDateTime getEndTime(){
        ////дата и время завершения задачи, которые рассчитываются исходя из startTime и duration.
        //duration = Duration.between(this.startTime,LocalDateTime.now());
        long durationMin = duration.getSeconds() / 60;
        LocalDateTime endTime = startTime.plusMinutes(durationMin);
        return endTime;
    }
    public void setStartTime(){
        this.startTime = LocalDateTime.now();
    }
    public void setDuration(){
        this.duration = Duration.between(this.startTime,LocalDateTime.now());
    }

    @Override
    public String toString() {
        String text = "";
        if(this.status != Status.DONE ) {
            text = "Task{" +
                    "id='" + idTask + '\'' +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", typeTask='" + typeTask + '\'' +
                    ", status=" + status + '\'' +
                    ", startTime=" + startTime + '\'' +
                    '}';
        } else if (this.status == Status.DONE) {
            LocalDateTime endTime = getEndTime();
            text = "Task{" +
                    "id='" + idTask + '\'' +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", typeTask='" + typeTask + '\'' +
                    ", status=" + status + '\'' +
                    ", startTime=" + startTime + '\'' +
                    ", endTime=" + endTime +
                    '}';
        }

        return text;
        //LocalDateTime endTime = getEndTime();
       /* return "Task{" +
                "id='" + idTask + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", typeTask='" + typeTask + '\'' +
                ", status=" + status + '\'' +
                ", startTime=" + startTime + '\'' +
                ", endTime=" + endTime +
                '}';*/
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(typeTask, task.typeTask) && status == task.status;
    }

}