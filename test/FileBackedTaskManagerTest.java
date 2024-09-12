import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.task_manager.manager.*;
import ru.yandex.task_manager.task.Epic;
import ru.yandex.task_manager.task.Status;
import ru.yandex.task_manager.task.Subtask;
import ru.yandex.task_manager.task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    static HashMap<Integer, Task> listTask;
    static HashMap<Integer, Subtask> listSubtask;
    static HashMap<Integer, Epic> listEpic;
    public static InMemoryTaskManager inMemoryTaskManager;
    public static TaskManager taskManager;
    static FileBackedTaskManager taskManagerBacked;

    @BeforeEach
    void beforeEach() throws IOException {
        listTask = new HashMap<Integer, Task>();
        listEpic = new HashMap<Integer, Epic>();
        listSubtask = new HashMap<Integer, Subtask>();

        taskManagerBacked =  new FileBackedTaskManager(listTask,listEpic,listSubtask);

    }

    @Test
    void loadFromFileTest() throws IOException {

        Path tempFilePath = Files.createTempFile("temp_task_manager", ".txt");
        File tempFile = tempFilePath.toFile();

        String testData = "idTask,name,description,typeTask,status,epicId\n" +
                "1,name1,description1,TASK,NEW,\n" +
                "4,name2,description2,TASK,NEW,\n" +
                "5,name3,description3,TASK,NEW,\n" +
                "6,name4,description4,TASK,NEW,\n" +
                "2,name5,description5,EPIC,NEW,\n" +
                "7,name6,description6,EPIC,NEW,\n" +
                "3,name7,description7,SUBTASK,NEW,2";
        Files.writeString(tempFilePath, testData);


        taskManagerBacked = FileBackedTaskManager.loadFromFile(tempFile.getAbsolutePath());
        HashMap<Integer, Task> listTaskTest = taskManagerBacked.getAllTask();
        HashMap<Integer, Epic> listEpicTest = taskManagerBacked.getAllEpic();
        HashMap<Integer, Subtask> listSubtaskTest = taskManagerBacked.getAllSubtask();
        assertNotEquals(0, listTaskTest.size(), "Размер списка не совпадает.");
        assertNotEquals(0, listEpicTest.size(), "Размер списка не совпадает.");
        assertNotEquals(0, listSubtaskTest.size(), "Размер списка не совпадает.");

        tempFile.deleteOnExit();
    }

    @Test
    void fromStringCheck() throws IOException {
        Path tempFilePath = Files.createTempFile("temp_task_manager", ".txt");
        File tempFile = tempFilePath.toFile();

        inMemoryTaskManager = new InMemoryTaskManager(listTask,listEpic,listSubtask);

        String name = "Test addNewTask";
        String description = "Test addNewTask description";
        int idFirstTask =  inMemoryTaskManager.addTask(name, description, Status.NEW);

        String nameEpic = "Test addNewEpic";
        String descriptionEpic = "Test addNewEpic description";
        int idFirstEpic =  inMemoryTaskManager.addEpic(nameEpic, descriptionEpic, Status.NEW);

        String nameSubtask = "Test addNewSubtask";
        String descriptionSubtask = "Test addNewSubtask description";
        int idFirstSubtask =  inMemoryTaskManager.addSubtask(nameSubtask, descriptionSubtask, Status.NEW,idFirstEpic);

        taskManagerBacked.saveFile(tempFile.getAbsolutePath());


        long isEmpty = Files.size(tempFilePath);

        assertNotEquals(false,isEmpty > 50,"Файл пустой.");
        tempFile.deleteOnExit();
    }

    @Test
    void checkEpicInSubtask() throws IOException {

        Path tempFilePath = Files.createTempFile("temp_task_manager", ".txt");
        File tempFile = tempFilePath.toFile();

        String testData = "idTask,name,description,typeTask,status,epicId\n" +
                "1,name1,description1,TASK,NEW,\n" +
                "4,name2,description2,TASK,NEW,\n" +
                "5,name3,description3,TASK,NEW,\n" +
                "6,name4,description4,TASK,NEW,\n" +
                "2,name5,description5,EPIC,NEW,\n" +
                "7,name6,description6,EPIC,NEW,\n" +
                "3,name7,description7,SUBTASK,NEW,2";
        Files.writeString(tempFilePath, testData);


        taskManagerBacked = FileBackedTaskManager.loadFromFile(tempFile.getAbsolutePath());
        HashMap<Integer, Task> listTaskTest = taskManagerBacked.getAllTask();
        HashMap<Integer, Epic> listEpicTest = taskManagerBacked.getAllEpic();
        HashMap<Integer, Subtask> listSubtaskTest = taskManagerBacked.getAllSubtask();
        assertEquals(listEpicTest.get(2).idTask, listSubtaskTest.get(3).idEpic, "В подзадаче нет эпика.");

        tempFile.deleteOnExit();
    }

    @Test
    void testAddTaskDoesNotThrowException() {
        assertDoesNotThrow(() -> {
            FileBackedTaskManager taskManagerBacked = new FileBackedTaskManager(new HashMap<>(), new HashMap<>(), new HashMap<>());
            taskManagerBacked.addTask("ru.yandex.task_manager.task.Task Name", "ru.yandex.task_manager.task.Task Description", Status.NEW);
        }, "Добавление задачи не должно выбрасывать никаких исключений.");
    }

    @Test
    void testSaveFileThrowsException() {
        assertThrows(ManagerSaveException.class, () -> {
            Path tempFilePath = Files.createTempFile("temp_task_manager_invalid", ".txt");
            tempFilePath.toFile().setReadOnly();

            FileBackedTaskManager taskManagerBacked = new FileBackedTaskManager(new HashMap<>(), new HashMap<>(), new HashMap<>());
            taskManagerBacked.saveFile(tempFilePath.toString());
        }, "Ожидается исключение ru.yandex.task_manager.manager.ManagerSaveException при попытке сохранения в файл, доступный только для чтения.");
    }



}
