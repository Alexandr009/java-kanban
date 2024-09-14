import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.task_manager.task.Epic;
import ru.yandex.task_manager.manager.InMemoryTaskManager;
import ru.yandex.task_manager.task.Status;
import ru.yandex.task_manager.task.Subtask;
import ru.yandex.task_manager.task.Task;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    static HashMap<Integer, Task> listTask;
    static HashMap<Integer, Subtask> listSubtask;
    static HashMap<Integer, Epic> listEpic;
    public static InMemoryTaskManager inMemoryTaskManager;

    static int idFirstTask;
    static int idFirstEpic;
    static int idFirstSubtask;
    @BeforeEach
     void beforeEach(){
        listTask = new HashMap<Integer, Task>();
        listEpic = new HashMap<Integer, Epic>();
        listSubtask = new HashMap<Integer, Subtask>();
        inMemoryTaskManager = new InMemoryTaskManager(listTask,listEpic,listSubtask);


        String name = "Test addNewTask";
        String description = "Test addNewTask description";
        idFirstTask =  inMemoryTaskManager.addTask(name, description, Status.NEW);

        String nameEpic = "Test addNewEpic";
        String descriptionEpic = "Test addNewEpic description";
        idFirstEpic =  inMemoryTaskManager.addEpic(nameEpic, descriptionEpic, Status.NEW);

        String nameSubtask = "Test addNewSubtask";
        String descriptionSubtask = "Test addNewSubtask description";
        idFirstSubtask =  inMemoryTaskManager.addSubtask(nameSubtask, descriptionSubtask, Status.NEW,idFirstEpic);
    }
    ///////////////////////ru.yandex.task_manager.task.Task////////////////////////////////////////////////
    @Test
    void addNewTask() {
        String name = "Test addNewTask";
        String description = "Test addNewTask description";
        int idTask =  inMemoryTaskManager.addTask(name, description, Status.NEW);
        assertNotNull(idTask, "Задача не найдена.");
    }

    @Test
    void getTaskUpdateTask(){

        String name = "Test addNewTask";
        String description = "Test addNewTask description";

        Task taskFirst = inMemoryTaskManager.getTask(idFirstTask);
        assertNotNull(taskFirst,"ru.yandex.task_manager.task.Task не найден.");

        Status newStatus = Status.IN_PROGRESS;
        inMemoryTaskManager.updateTask(idFirstTask, name, description, newStatus);
        assertEquals(Status.IN_PROGRESS,inMemoryTaskManager.getTask(idFirstTask).status,  "Статусы не совпадают.");
    }
    @Test
    void deleteTaskRecord(){

        inMemoryTaskManager.deleteTask(idFirstTask);
        assertNull(inMemoryTaskManager.listTask.get(idFirstTask),"ru.yandex.task_manager.task.Task не удален.");
    }

    @Test
    void deleteAllTask(){
        int listSize  = inMemoryTaskManager.listTask.size();
        assertEquals(1, listSize, "Размер списка не совпадает.");
        inMemoryTaskManager.deleteAllTask();
        int listSize2  = inMemoryTaskManager.listTask.size();
        assertEquals(0, listSize2, "Размер списка не совпадает.");
    }
    @Test
    void getListTaskSize2(){
        String name = "Test addNewTask";
        String description = "Test addNewTask description";
        int idTask =  inMemoryTaskManager.addTask(name, description, Status.NEW);
        HashMap<Integer, Task> list  = inMemoryTaskManager.getAllTask();
        assertEquals(2, list.size(), "Размер списка не совпадает.");
    }
    ///////////////////////ru.yandex.task_manager.task.Epic////////////////////////////////////////////////
    @Test
    void addNewEpic() {
        String name = "ru.yandex.task_manager.task.Epic addNewTask";
        String description = "ru.yandex.task_manager.task.Epic addNewTask description";
        int idEpic =  inMemoryTaskManager.addEpic(name, description, Status.NEW);
        assertNotNull(idEpic, "ru.yandex.task_manager.task.Epic не найден.");
    }
    @Test
    void getEpicUpdateEpic() {

        String name = "ru.yandex.task_manager.task.Epic new name";
        String description = "ru.yandex.task_manager.task.Epic addNewTask description";
        Epic epicFirst = inMemoryTaskManager.getEpic(idFirstEpic);
        assertNotNull(epicFirst,"ru.yandex.task_manager.task.Epic не найден.");
        inMemoryTaskManager.updateEpic(idFirstEpic, name, description);
        assertEquals("ru.yandex.task_manager.task.Epic new name", inMemoryTaskManager.getEpic(idFirstEpic).name, "Имена не совпадают.");
    }
    @Test
    void deleteEpicRecord(){

        inMemoryTaskManager.deleteEpic(idFirstEpic);
        assertNull(inMemoryTaskManager.listEpic.get(idFirstEpic),"ru.yandex.task_manager.task.Epic не удален.");
    }
    @Test
    void deleteAllEpic(){
        int listSize  = inMemoryTaskManager.listEpic.size();
        assertEquals(1, listSize, "Размер списка не совпадает.");
        inMemoryTaskManager.deleteAllEpic();
        int listSize2  = inMemoryTaskManager.listEpic.size();
        assertEquals(0, inMemoryTaskManager.listEpic.size(), "Размер списка не совпадает.");
    }
    @Test
    void getListEpicSize2(){
        String name = "Test addNewEpic";
        String description = "Test addNewEpic description";
        int idEpic =  inMemoryTaskManager.addEpic(name, description, Status.NEW);
        HashMap<Integer, Epic> list  = inMemoryTaskManager.getAllEpic();
        assertEquals(2, list.size(), "Размер списков не совпадает.");
    }
    @Test
    void addNewSubtask() {
        String name = "Test addNewSubtask";
        String description = "Test addNewSubtask description";
        int idSubtask =  inMemoryTaskManager.addSubtask(name, description, Status.NEW,idFirstEpic);
        assertNotNull(idSubtask, "ru.yandex.task_manager.task.Subtask не найден.");
    }
    @Test
    void getSubtaskUpdateSubtaskUpdateStatusEpic(){

        String name = "Test addNewSubtask";
        String description = "Test addNewSubtask description";

        Task subTaskFirst = inMemoryTaskManager.getSubtask(idFirstSubtask);
        assertNotNull(subTaskFirst,"ru.yandex.task_manager.task.Task не найден.");

        Status newStatus = Status.IN_PROGRESS;
        inMemoryTaskManager.updateSubtask(idFirstSubtask, name, description, newStatus);
        assertEquals(Status.IN_PROGRESS,inMemoryTaskManager.getSubtask(idFirstSubtask).status,  "Статусы не совпадают.");
        assertEquals(Status.IN_PROGRESS,inMemoryTaskManager.getEpic(idFirstEpic).status,  "Статусы не совпадают.");
    }
    @Test
    void deleteSubtaskRecord(){
        inMemoryTaskManager.deleteSubtask(idFirstSubtask);
        assertNull(inMemoryTaskManager.listSubtask.get(idFirstSubtask),"ru.yandex.task_manager.task.Task не удален.");
    }
    @Test
    void deleteSubtaskRecordCheckEpicSubtask(){
        Subtask subtask = listSubtask.get(idFirstSubtask);
        Epic epic = listEpic.get(subtask.idEpic);
        inMemoryTaskManager.deleteSubtask(idFirstSubtask);
        assertNull(inMemoryTaskManager.listSubtask.get(idFirstSubtask),"ru.yandex.task_manager.task.Task не удален.");
        boolean check = epic.subTask.contains(subtask);
        assertFalse(check,"ru.yandex.task_manager.task.Task не удален.");
    }

    @Test
    void deleteAllSubtask(){
        int listSize  = inMemoryTaskManager.listSubtask.size();
        assertEquals(1, listSize, "Размер списка не совпадает.");
        inMemoryTaskManager.deleteAllSubtask();
        int listSize2  = inMemoryTaskManager.listSubtask.size();
        assertEquals(0, listSize2, "Размер списка не совпадает.");
    }
    @Test
    void getListSubtaskSize2(){
        String name = "Test addNewSubtask";
        String description = "Test addNewSubtask description";
        int idTask =  inMemoryTaskManager.addSubtask(name, description, Status.NEW,idFirstEpic);
        HashMap<Integer, Subtask> list  = inMemoryTaskManager.getAllSubtask();
        assertEquals(2, list.size(), "Размер списка не совпадает.");
    }

    @Test
    void setEpicStatusNEW(){
        listEpic.get(idFirstEpic).status = null;
        int idSubtask1 =  inMemoryTaskManager.addSubtask("Subtask1", "description1", null,idFirstEpic);
        int idSubtask2 =  inMemoryTaskManager.addSubtask("Subtask2", "description2", Status.NEW,idFirstEpic);
        inMemoryTaskManager.updateSubtask(idSubtask1,"ru.yandex.task_manager.task.Subtask new","description new",Status.NEW);
        assertEquals(Status.NEW, listEpic.get(idFirstEpic).status, "Статусы не совпадают.");
    }

    @Test
    void setEpicStatusDONE(){
        listEpic.get(idFirstEpic).status = null;
        int idSubtask1 =  inMemoryTaskManager.addSubtask("Subtask1", "description1", Status.NEW,idFirstEpic);

        inMemoryTaskManager.updateSubtask(idFirstSubtask,"ru.yandex.task_manager.task.Subtask new","description new",Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(idSubtask1,"ru.yandex.task_manager.task.Subtask new","description new",Status.IN_PROGRESS);

        inMemoryTaskManager.updateSubtask(idFirstSubtask,"ru.yandex.task_manager.task.Subtask new","description new",Status.DONE);
        inMemoryTaskManager.updateSubtask(idSubtask1,"ru.yandex.task_manager.task.Subtask new","description new",Status.DONE);

        assertEquals(Status.DONE, listEpic.get(idFirstEpic).status, "Статусы не совпадают.");
    }
    @Test
    void setEpicStatusNEWandDONE(){
        int idSubtask1 =  inMemoryTaskManager.addSubtask("Subtask1", "description1", Status.NEW,idFirstEpic);

        inMemoryTaskManager.updateSubtask(idFirstSubtask,"ru.yandex.task_manager.task.Subtask new","description new",Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(idFirstSubtask,"ru.yandex.task_manager.task.Subtask new","description new",Status.DONE);

        assertEquals(Status.NEW, listEpic.get(idFirstEpic).status, "Статусы не совпадают.");
    }

    @Test
    void setEpicStatusIN_PROGRESS(){
        listEpic.get(idFirstEpic).status = null;
        int idSubtask1 =  inMemoryTaskManager.addSubtask("Subtask1", "description1", Status.NEW,idFirstEpic);

        inMemoryTaskManager.updateSubtask(idFirstSubtask,"ru.yandex.task_manager.task.Subtask new","description new",Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(idSubtask1,"ru.yandex.task_manager.task.Subtask new","description new",Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, listEpic.get(idFirstEpic).status, "Статусы не совпадают.");
    }

    @Test
    void checkIntersectionsIntervals(){
        inMemoryTaskManager.updateTask(idFirstTask,"ru.yandex.task_manager.task.Task new","description new",Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(idFirstSubtask,"ru.yandex.task_manager.task.Subtask new","description new",Status.IN_PROGRESS);
        List<Task> listPrioritiTasks = inMemoryTaskManager.getPrioritizedTasks();
        assertEquals(listPrioritiTasks.size(), 1, "Пересечения интервалов.");
    }

}
