import java.util.*;
import java.util.List;
import java.util.ArrayList;
public class InMemoryTaskManager implements TaskManager {

    public static int counter;
    HashMap<Integer, Task> listTask;
    HashMap<Integer, Subtask> listSubtask;
    HashMap<Integer, Epic> listEpic;

    HistoryManager historyManager;
    public InMemoryTaskManager(HashMap listTask, HashMap listEpic, HashMap listSubtask) {
        this.listTask = listTask;
        this.listEpic = listEpic;
        this.listSubtask = listSubtask;
         historyManager = Managers.getDefaultHistory();

    }

    ///////////////////Subtask////////////////////////////////////
    @Override
    public int addSubtask(String name, String description, Status status, int idEpic) {
        counter = counter + 1;
        Subtask subtask = new Subtask(name, description, status, counter, idEpic);
        System.out.println("" + subtask);
        listSubtask.put(counter, subtask);
        listEpic.get(idEpic).subTask.add(subtask);
        return counter;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = listSubtask.get(id);
        historyManager.addTaskToHistory(subtask);
        return subtask;
    }

    @Override
    public void deleteAllSubtask() {
        listSubtask.clear();
        System.out.println("Все Subtask удалены");
    }

    @Override
    public HashMap getAllSubtask() {
        return listSubtask;
    }

    @Override
    public Subtask updateSubtask(int id, String name, String description, Status status) {
        listSubtask.get(id).name = name;
        listSubtask.get(id).description = description;
        listSubtask.get(id).status = status;

        int idEpic = listSubtask.get(id).idEpic;
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
        return listSubtask.get(id);
    }

    @Override
    public void deleteSubtask(int id) {
        listSubtask.remove(id);
    }

    ///////////////////epic////////////////////////////////////
    @Override
    public int addEpic(String name, String description, Status status) {
        counter = counter + 1;
        Epic epic = new Epic(name, description, status, counter);
        System.out.println("" + epic);
        listEpic.put(counter, epic);
        return counter;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = listEpic.get(id);
        historyManager.addTaskToHistory(epic);
        return epic;
    }

    @Override
    public void deleteAllEpic() {
        listEpic.clear();
        System.out.println("Все Epic удалены");
    }

    @Override
    public HashMap getAllEpic() {
        return listEpic;
    }

    @Override
    public void deleteEpic(int id) {
        listEpic.remove(id);
    }

    @Override
    public void updateEpic(int id, String name, String description) {
        listEpic.get(id).name = name;
        listEpic.get(id).description = description;
    }

    @Override
    public Epic updateEpicStatus(int id, Status status) {
        Epic epic = listEpic.get(id);
        epic.status = status;
        return epic;
    }
    ///////////////////Task///////////////////////////////

    @Override
    public int addTask(String name, String description, Status status) {
        counter = counter + 1;
        Task task = new Task(name, description, status, counter);
        System.out.println("" + task);
        listTask.put(counter, task);
        return counter;
    }

    @Override
    public HashMap getAllTask() {
        return listTask;
    }

    @Override
    public Task getTask(int id) {
        Task task = listTask.get(id);
        historyManager.addTaskToHistory(task);
        return task;
    }

    @Override
    public void deleteAllTask() {
        listTask.clear();
        System.out.println("Все Task удалены");
    }

    @Override
    public void updateTask(int id, String name, String description, Status status) {
        listTask.get(id).name = name;
        listTask.get(id).description = description;
        listTask.get(id).status = status;
    }

    @Override
    public void deleteTask(int id) {
        listTask.remove(id);
    }
}
