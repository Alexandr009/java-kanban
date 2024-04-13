import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public interface TaskManager {
    /////////////////Common////////////////////////////
    //List<Task> getHistory();
    ///////////////////Subtask////////////////////////////////////
    int addSubtask(String name, String description, Status status, int idEpic);

    Subtask getSubtask(int id);

    void deleteAllSubtask();

    HashMap getAllSubtask();

    Subtask updateSubtask(int id, String name, String description, Status status);

    void deleteSubtask(int id);

    ///////////////////epic////////////////////////////////////
    int addEpic(String name, String description, Status status);

    Epic getEpic(int id);

    void deleteAllEpic();

    HashMap getAllEpic();

    void deleteEpic(int id);

    void updateEpic(int id, String name, String description);

    Epic updateEpicStatus(int id, Status status);

    int addTask(String name, String description, Status status);

    HashMap getAllTask();

    Task getTask(int id);

    void deleteAllTask();

    void updateTask(int id, String name, String description, Status status);

    void deleteTask(int id);

    @Override
    String toString();
}
