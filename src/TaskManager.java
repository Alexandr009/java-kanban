import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public static int counter;
    //HashMap<Integer, ArrayList<Task>> listTask;
    HashMap<Integer, Task> listTask;
    HashMap<Integer, Subtask> listSubtask;
    HashMap<Integer, Epic> listEpic;

    public TaskManager(HashMap listTask, HashMap listEpic, HashMap listSubtask) {
        this.listTask = listTask;
        this.listEpic = listEpic;
        this.listSubtask = listSubtask;
    }

    ///////////////////Subtask////////////////////////////////////
    int addSubtask(String name, String description, Status status, int idEpic) {
        counter = counter + 1;
        Subtask subtask = new Subtask(name, description, status, counter, idEpic);
        System.out.println("" + subtask);
        listSubtask.put(counter, subtask);
        listEpic.get(idEpic).subTask.add(subtask);
        return counter;
    }

    Subtask getSubtask(int id) {
        Subtask subtask = listSubtask.get(id);
        return subtask;
    }

    void deleteAllSubtask() {
        listSubtask.clear();
        System.out.println("Все Subtask удалены");
    }

    HashMap getAllSubtask() {
        return listSubtask;
    }

    Subtask updateSubtask(int id, String name, String description, Status status) {
        listSubtask.get(id).name = name;
        listSubtask.get(id).description = description;
        listSubtask.get(id).status = status;
        //
        int idEpic = listSubtask.get(id).idEpic;
        //ArrayList<Subtask> subtasksForEpic = new ArrayList<>();
        Epic epic = getEpic(listSubtask.get(id).idEpic);
        boolean checkSameStatus = true;
        ArrayList<Status> listStatus = new ArrayList<>();
        for (Subtask subtack : epic.subTask) {
            listStatus.add(subtack.status);
            if (epic.subTask.size() == listStatus.size()) {
                for (int i = 0; i < listStatus.size(); i++) {
                    if (listStatus.get(0) != listStatus.get(i)) {
                        checkSameStatus = false;
                    }
                }
            }
        }
        if (checkSameStatus == true) {
            Epic epicUpdated = updateEpicStatus(idEpic, status);
            System.out.println("epicUpdated = " + epicUpdated);
        }
        //
        return listSubtask.get(id);
    }

    void deleteSubtask(int id) {
        listSubtask.remove(id);
    }

    ///////////////////epic////////////////////////////////////
    int addEpic(String name, String description, Status status) {
        counter = counter + 1;
        Epic epic = new Epic(name, description, status, counter);
        System.out.println("" + epic);
        listEpic.put(counter, epic);
        return counter;
    }

    Epic getEpic(int id) {
        Epic epic = listEpic.get(id);
        return epic;
    }

    void deleteAllEpic() {
        listEpic.clear();
        System.out.println("Все Epic удалены");
    }

    HashMap getAllEpic() {
        return listEpic;
    }

    void deleteEpic(int id) {
        listEpic.remove(id);
    }

    void updateEpic(int id, String name, String description) {
        listEpic.get(id).name = name;
        listEpic.get(id).description = description;
    }

    Epic updateEpicStatus(int id, Status status) {
        Epic epic = listEpic.get(id);
        epic.status = status;
        return epic;
    }
    ///////////////////Task///////////////////////////////

    int addTask(String name, String description, Status status) {
        counter = counter + 1;
        Task task = new Task(name, description, status, counter);
        System.out.println("" + task);
        listTask.put(counter, task);
        return counter;
    }

    HashMap getAllTask() {
        return listTask;
    }

    Task getTask(int id) {
        Task task = listTask.get(id);
        return task;
    }

    void deleteAllTask() {
        listTask.clear();
        System.out.println("Все Task удалены");
    }

    void updateTask(int id, String name, String description, Status status) {
        listTask.get(id).name = name;
        listTask.get(id).description = description;
        listTask.get(id).status = status;
    }

    void deleteTask(int id) {
        listTask.remove(id);
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "listTask=" + listTask +
                '}';
    }
}
