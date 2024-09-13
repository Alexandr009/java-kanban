import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.task_manager.http.EpicsHttpHandler;
import ru.yandex.task_manager.http.HttpTaskServer;
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

public class EpicsHttpHandlerTest {
    static HashMap<Integer, Epic> listEpic;
    static HashMap<Integer, Task> listTask;
    static HashMap<Integer, Subtask> listSubtask;
    public static InMemoryTaskManager inMemoryTaskManager;
    public static HistoryManager historyManager;
    HttpTaskServer taskServer;
    Gson gson;

    @BeforeEach
    public void setUp() throws IOException {
        listEpic = new HashMap<>();
        listTask = new HashMap<>();
        listSubtask = new HashMap<>();
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault(listTask, listEpic, listSubtask);
        historyManager = Managers.getDefaultHistory();

        taskServer = new HttpTaskServer(inMemoryTaskManager, historyManager);
        EpicsHttpHandler epicsHttpHandler = new EpicsHttpHandler(inMemoryTaskManager);
        gson = EpicsHttpHandler.getGson();
        taskServer.start();
    }

    @AfterEach
    public void tearDown() {
        taskServer.stop();
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        // создаём эпик
        Epic epic = new Epic("Epic 1", "Testing epic", Status.NEW, 1);

        // конвертируем его в JSON
        String epicJson = gson.toJson(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        // вызываем REST для создания эпиков
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());
        int idRespons = 0;
        JsonElement jsonElement = JsonParser.parseString(response.body());//
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            idRespons = jsonObject.get("id").getAsInt();
        }

        // проверяем, что эпик добавлен в менеджер
        HashMap<Integer, Epic> epicsFromManager = inMemoryTaskManager.getAllEpic();

        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Epic 1", inMemoryTaskManager.getEpic(idRespons).name, "Некорректное имя эпика");
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        // создаём и добавляем эпик в менеджер
        Epic epic = new Epic("Epic 2", "Testing epic 2", Status.NEW, 1);
        int epicId = inMemoryTaskManager.addEpic(epic.getName(), epic.getDescription(), epic.getStatus());

        // создаём HTTP-клиент и запрос на получение эпика
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // отправляем запрос
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что эпик возвращён корректно
        Epic receivedEpic = gson.fromJson(response.body(), Epic.class);
        assertNotNull(receivedEpic, "Эпик не возвращён");
        assertEquals(epicId, receivedEpic.idTask, "ID эпика не совпадает");
        assertEquals("Epic 2", receivedEpic.getName(), "Имя эпика не совпадает");
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        // создаём и добавляем эпик в менеджер
        Epic epic = new Epic("Epic 3", "Testing epic 3", Status.NEW, 1);
        int epicId = inMemoryTaskManager.addEpic(epic.getName(), epic.getDescription(), epic.getStatus());

        // создаём HTTP-клиент и запрос на удаление эпика
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // отправляем запрос
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что эпик был удалён
        HashMap<Integer, Epic> epicsFromManager = inMemoryTaskManager.getAllEpic();
        assertFalse(epicsFromManager.containsKey(epicId), "Эпик не был удалён");
    }
}
