import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.task_manager.http.HttpTaskServer;
import ru.yandex.task_manager.http.SubtasksHttpHandler;
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
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubtasksHttpHandlerTest {
    static HashMap<Integer, Subtask> listSubtask;
    static HashMap<Integer, Task> listTask;
    static HashMap<Integer, Epic> listEpic;
    public static InMemoryTaskManager inMemoryTaskManager;
    public static HistoryManager historyManager;
    HttpTaskServer taskServer;
    Gson gson;
    static int counter;

    @BeforeEach
    public void setUp() throws IOException {
        counter = 1;
        listSubtask = new HashMap<>();
        listTask = new HashMap<>();
        listEpic = new HashMap<>();
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault(listTask, listEpic, listSubtask);
        historyManager = Managers.getDefaultHistory();

        taskServer = new HttpTaskServer(inMemoryTaskManager, historyManager);
        SubtasksHttpHandler subtasksHttpHandler = new SubtasksHttpHandler(inMemoryTaskManager);
        gson = SubtasksHttpHandler.getGson();
        taskServer.start();
    }

    @AfterEach
    public void tearDown() {
        taskServer.stop();
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        // создаём эпик
        int epicId = inMemoryTaskManager.addEpic("Epic for Subtask", "Epic description", Status.NEW);

        // создаём и добавляем сабтаск в менеджер
        int subtaskId = inMemoryTaskManager.addSubtask("Subtask 4", "Testing subtask 4", Status.NEW, epicId);
        inMemoryTaskManager.updateSubtask(subtaskId,"Subtask 4", "Testing subtask 4", Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtaskId,"Subtask 4", "Testing subtask 4", Status.DONE);
        Subtask subtaskNew = inMemoryTaskManager.getSubtask(subtaskId);
        
        // создаём HTTP-клиент и запрос на удаление сабтаска
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // отправляем запрос
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что сабтаск был удалён
        assertFalse(inMemoryTaskManager.listSubtask.containsKey(subtaskId), "Сабтаск не был удалён");
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        // создаём эпик
        int epicId = inMemoryTaskManager.addEpic("Epic for Subtask", "Epic description", Status.NEW);

        // создаём сабтаск
        Subtask subtask = new Subtask("Subtask 1", "Testing subtask", Status.NEW, counter+1,epicId);

        // конвертируем его в JSON
        String subtaskJson = gson.toJson(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        // вызываем REST для создания сабтасков
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int idRespons = 0;
        JsonElement jsonElement = JsonParser.parseString(response.body());//
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            idRespons = jsonObject.get("id").getAsInt();
        }
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что сабтаск добавлен в менеджер
        HashMap<Integer, Subtask> subtasksFromManager = inMemoryTaskManager.getAllSubtask();

        assertNotNull(subtasksFromManager, "Сабтаски не возвращаются");
        assertEquals("Subtask 1", subtasksFromManager.get(idRespons).name, "Некорректное имя сабтаска");
    }

    @Test
    public void testGetSubtask() throws IOException, InterruptedException {
        // создаём эпик
        Epic epic = new Epic("Epic for Subtask", "Epic description", Status.NEW, 1);
        int epicId = inMemoryTaskManager.addEpic(epic.name, epic.description, epic.status);

        // создаём и добавляем сабтаск в менеджер
        Subtask subtask = new Subtask("Subtask 2", "Testing subtask 2", Status.NEW,counter+1, epicId);
        int subtaskId = inMemoryTaskManager.addSubtask(subtask.name, subtask.description, subtask.status, epicId);

        // создаём HTTP-клиент и запрос на получение сабтаска
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // отправляем запрос
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что сабтаск возвращён корректно
        Subtask receivedSubtask = gson.fromJson(response.body(), Subtask.class);
        assertNotNull(receivedSubtask, "Сабтаск не возвращён");
        assertEquals(subtaskId, receivedSubtask.idTask, "ID сабтаска не совпадает");
        assertEquals("Subtask 2", receivedSubtask.name, "Имя сабтаска не совпадает");
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        // создаём эпик
        Epic epic = new Epic("Epic for Subtask", "Epic description", Status.NEW, 1);
        int epicId = inMemoryTaskManager.addEpic(epic.name, epic.description, epic.status);

        // создаём и добавляем сабтаск в менеджер
        Subtask subtask = new Subtask("Subtask 3", "Testing subtask 3", Status.NEW,counter+1, epicId);
        int subtaskId = inMemoryTaskManager.addSubtask(subtask.name, subtask.description, subtask.status, epicId);

        // обновляем сабтаск
        inMemoryTaskManager.updateSubtask(subtaskId,"Updated Subtask 3", "Updated description", Status.IN_PROGRESS);
       // Subtask updatedSubtask = new Subtask("Updated Subtask 3", "Updated description", Status.DONE,counter+1, epicId);
        String updatedSubtaskJson = gson.toJson(inMemoryTaskManager.getSubtask(subtaskId));
        System.out.println("updatedSubtaskJson = " + updatedSubtaskJson);
        // создаём HTTP-клиент и запрос на обновление сабтаска
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(updatedSubtaskJson))
                .build();

        // отправляем запрос
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что сабтаск обновлён в менеджере
        Subtask receivedSubtask = inMemoryTaskManager.getSubtask(subtaskId);
        assertNotNull(receivedSubtask, "Обновлённый сабтаск не найден");
        assertEquals("Updated Subtask 3", receivedSubtask.name, "Имя обновлённого сабтаска не совпадает");
        assertEquals(Status.IN_PROGRESS, receivedSubtask.getStatus(), "Статус обновлённого сабтаска не совпадает");
    }


}
