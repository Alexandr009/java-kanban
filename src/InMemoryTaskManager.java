import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
public class InMemoryTaskManager implements TaskManager {

    public static int counter;
    HashMap<Integer, Task> listTask;
    HashMap<Integer, Subtask> listSubtask;
    HashMap<Integer, Epic> listEpic;

    HistoryManager historyManager;
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(task -> task.startTime));
    public InMemoryTaskManager(HashMap listTask, HashMap listEpic, HashMap listSubtask) {
        this.listTask = listTask;
        this.listEpic = listEpic;
        this.listSubtask = listSubtask;
         historyManager = Managers.getDefaultHistory();

    }

    public InMemoryTaskManager() {
        System.out.println("InMemoryTaskManager constructor = ");
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
        Epic epic = listEpic.get(idEpic);

        boolean checkSameStatus = epic.subTask.stream()
                .map(s->s.status)//Используем map(s -> s.status) для получения потока статусов подзадач.
                .distinct()//Используем distinct() для удаления дубликатов статусов.
                .count() == 1;//Если количество уникальных статусов равно 1, то все подзадачи имеют одинаковый статус.

        if (status.equals(Status.IN_PROGRESS) && listSubtask.get(id).startTime == null){
            listSubtask.get(id).setStartTime();
        }
        if (status.equals(Status.DONE)){
            listSubtask.get(id).setDuration();
            prioritizedTasks.remove(listSubtask.get(id));
        }



        if (checkSameStatus == true) {
            Epic epicUpdated = updateEpicStatus(idEpic, status);
            System.out.println("epicUpdated = " + epicUpdated);
        }
        if (listSubtask.get(id).startTime != null && !checkPrioritizedTasks(listSubtask.get(id))){
            prioritizedTasks.add(listSubtask.get(id));
        }
        return listSubtask.get(id);
    }

    @Override
    public void deleteSubtask(int id) {
        //delete Subtask in Epic
        Subtask subtask = listSubtask.get(id);
        Epic epic = listEpic.get(subtask.idEpic);
        epic.subTask.remove(subtask);
        prioritizedTasks.remove(listSubtask.get(id));
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

        if (status.equals(Status.IN_PROGRESS) && listEpic.get(id).startTime == null){
            listEpic.get(id).setStartTime();
        }
        if (status.equals(Status.DONE)){
            listEpic.get(id).setDuration();
        }

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
        if (status.equals(Status.IN_PROGRESS) && listTask.get(id).startTime == null){
            listTask.get(id).setStartTime();
        }
        listTask.get(id).status = status;

        if (status.equals(Status.DONE)){
            listTask.get(id).setDuration();
            prioritizedTasks.remove(listTask.get(id));
        }
        if (listTask.get(id).startTime != null && !checkPrioritizedTasks(listTask.get(id))){
            prioritizedTasks.add(listTask.get(id));
        }
    }

    @Override
    public void deleteTask(int id) {
        prioritizedTasks.remove(listTask.get(id));
        listTask.remove(id);
    }

    @Override
    public List<Task> getPrioritizedTasks(){
        return new ArrayList<>(prioritizedTasks);
    }

    public boolean checkPrioritizedTasks(Task newTask){
        //Использует anyMatch() для проверки, пересекается ли хотя бы одна из существующих задач с новой.
            boolean check = prioritizedTasks.stream()
                    .anyMatch(existingTask -> checkTaskStartTime(existingTask,newTask));
                return check;
    }
     public boolean checkTaskStartTime(Task existingTask, Task newTask){
         LocalDateTime existingTaskStart = existingTask.startTime;

         LocalDateTime newTaskStart = newTask.startTime;
         // Проверка на пересечение интервалов
         boolean check = existingTaskStart.isBefore(newTaskStart) ;
         return check;
     }
}

