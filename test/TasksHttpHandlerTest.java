import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.yandex.task_manager.http.HttpTaskServer;
import ru.yandex.task_manager.http.TasksHttpHandler;
import ru.yandex.task_manager.manager.HistoryManager;
import ru.yandex.task_manager.manager.InMemoryTaskManager;
import ru.yandex.task_manager.manager.Managers;
import ru.yandex.task_manager.manager.TaskManager;
import ru.yandex.task_manager.task.Epic;
import ru.yandex.task_manager.task.Status;
import ru.yandex.task_manager.task.Subtask;
import ru.yandex.task_manager.task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TasksHttpHandlerTest {
    static HashMap<Integer, Task> listTask;
    static HashMap<Integer, Subtask> listSubtask;
    static HashMap<Integer, Epic> listEpic;
    public static InMemoryTaskManager inMemoryTaskManager;
    public static HistoryManager historyManager;
    static int idFirstTask;
    static int idFirstEpic;
    static int idFirstSubtask;

   // TaskManager manager;
    HttpTaskServer taskServer;
    Gson gson;

    @BeforeEach
    public void setUp() throws IOException {

        HashMap<Integer, Task> listTask = new HashMap<>();
        HashMap<Integer, Epic> listSubtask = new HashMap<>();
        HashMap<Integer, Subtask> listEpic = new HashMap<>();
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault(listTask, listEpic, listSubtask);
        historyManager = Managers.getDefaultHistory();

        taskServer = new HttpTaskServer(inMemoryTaskManager,historyManager);
        TasksHttpHandler tasksHttpHandler = new TasksHttpHandler(inMemoryTaskManager);
        gson = TasksHttpHandler.getGson();
        taskServer.start();
    }

    @AfterEach
    public void tearDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test 1", "Testing task", Status.NEW, 1);
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);
        System.out.println("taskJson = " + taskJson);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        // вызываем рест для создания задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());
        int idRespons = 0;
        JsonElement jsonElement = JsonParser.parseString(response.body());//
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            idRespons = jsonObject.get("id").getAsInt();

        
        }
        // проверяем, что задача добавлена в менеджер
        Task taskManager = inMemoryTaskManager.getTask(idRespons);

        assertNotNull(taskManager, "Задачи не возвращаются");
        assertEquals(1, inMemoryTaskManager.getAllTask().size(), "Некорректное количество задач");
        assertEquals("Test 1", taskManager.name, "Некорректное имя задачи");
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        // создаём и добавляем задачу в менеджер
        Task task = new Task("Test 2", "Testing task 2", Status.NEW, 1);
        int taskId = inMemoryTaskManager.addTask(task.getName(), task.getDescription(), task.getStatus());

        // создаём HTTP-клиент и запрос на получение задачи
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // отправляем запрос
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что задача возвращена корректно
        Task receivedTask = gson.fromJson(response.body(), Task.class);
        assertNotNull(receivedTask, "Задача не возвращена");
        assertEquals(taskId, receivedTask.idTask, "ID задачи не совпадает");
        assertEquals("Test 2", receivedTask.getName(), "Имя задачи не совпадает");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        // создаём и добавляем задачу в менеджер
        Task task = new Task("Test 3", "Testing task 3", Status.NEW, 1);
        int taskId = inMemoryTaskManager.addTask(task.getName(), task.getDescription(), task.getStatus());

        // создаём HTTP-клиент и запрос на удаление задачи
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // отправляем запрос
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());
        int inMemoryTaskManagerSize = inMemoryTaskManager.listSubtask.size();
        // проверяем, что задача была удалена
        assertEquals(inMemoryTaskManagerSize,0, "Задача не была удалена");
    }
}
