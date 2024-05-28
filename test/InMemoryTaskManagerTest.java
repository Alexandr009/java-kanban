import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

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
    ///////////////////////Task////////////////////////////////////////////////
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
        assertNotNull(taskFirst,"Task не найден.");

        Status newStatus = Status.IN_PROGRESS;
        inMemoryTaskManager.updateTask(idFirstTask, name, description, newStatus);
        assertEquals(Status.IN_PROGRESS,inMemoryTaskManager.getTask(idFirstTask).status,  "Статусы не совпадают.");
    }
    @Test
    void deleteTaskRecord(){

        inMemoryTaskManager.deleteTask(idFirstTask);
        assertNull(inMemoryTaskManager.listTask.get(idFirstTask),"Task не удален.");
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
    ///////////////////////Epic////////////////////////////////////////////////
    @Test
    void addNewEpic() {
        String name = "Epic addNewTask";
        String description = "Epic addNewTask description";
        int idEpic =  inMemoryTaskManager.addEpic(name, description, Status.NEW);
        assertNotNull(idEpic, "Epic не найден.");
    }
    @Test
    void getEpicUpdateEpic() {

        String name = "Epic new name";
        String description = "Epic addNewTask description";
        Epic epicFirst = inMemoryTaskManager.getEpic(idFirstEpic);
        assertNotNull(epicFirst,"Epic не найден.");
        inMemoryTaskManager.updateEpic(idFirstEpic, name, description);
        assertEquals("Epic new name", inMemoryTaskManager.getEpic(idFirstEpic).name, "Имена не совпадают.");
    }
    @Test
    void deleteEpicRecord(){

        inMemoryTaskManager.deleteEpic(idFirstEpic);
        assertNull(inMemoryTaskManager.listEpic.get(idFirstEpic),"Epic не удален.");
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
        assertNotNull(idSubtask, "Subtask не найден.");
    }
    @Test
    void getSubtaskUpdateSubtaskUpdateStatusEpic(){

        String name = "Test addNewSubtask";
        String description = "Test addNewSubtask description";

        Task subTaskFirst = inMemoryTaskManager.getSubtask(idFirstSubtask);
        assertNotNull(subTaskFirst,"Task не найден.");

        Status newStatus = Status.IN_PROGRESS;
        inMemoryTaskManager.updateSubtask(idFirstSubtask, name, description, newStatus);
        assertEquals(Status.IN_PROGRESS,inMemoryTaskManager.getSubtask(idFirstSubtask).status,  "Статусы не совпадают.");
        assertEquals(Status.IN_PROGRESS,inMemoryTaskManager.getEpic(idFirstEpic).status,  "Статусы не совпадают.");
    }
    @Test
    void deleteSubtaskRecord(){
        inMemoryTaskManager.deleteSubtask(idFirstSubtask);
        assertNull(inMemoryTaskManager.listSubtask.get(idFirstSubtask),"Task не удален.");
    }
    @Test
    void deleteSubtaskRecordCheckEpicSubtask(){
        Subtask subtask = listSubtask.get(idFirstSubtask);
        Epic epic = listEpic.get(subtask.idEpic);
        inMemoryTaskManager.deleteSubtask(idFirstSubtask);
        assertNull(inMemoryTaskManager.listSubtask.get(idFirstSubtask),"Task не удален.");
        boolean check = epic.subTask.contains(subtask);
        assertFalse(check,"Task не удален.");
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
}
