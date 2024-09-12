package ru.yandex.task_manager.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.task_manager.manager.TaskManager;
import ru.yandex.task_manager.task.Epic;
import ru.yandex.task_manager.task.Status;
import ru.yandex.task_manager.task.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class EpicsHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public EpicsHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET": {
                Integer id = getIdFromUrl(exchange.getRequestURI().getPath());
                if (id == null) {
                    HashMap<Integer, Epic> list = taskManager.getAllEpic();
                    sendText(exchange, list.toString(), 200);

                } else {
                    Task task = taskManager.getEpic(id);
                    sendText(exchange, task.toString(), 200);
                }
                break;
            }
            case "POST": {
                // int id2 = taskManager.addEpic("1", "11", Status.NEW);
                StringBuilder bodyBuilder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        bodyBuilder.append(line);
                    }
                }

                String body = bodyBuilder.toString();

                // Парсим JSON с использованием JsonElement и JsonObject
                JsonElement jsonElement = JsonParser.parseString(body);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String name = jsonObject.get("name").getAsString();
                    String description = jsonObject.get("description").getAsString();
                    String statusStr = jsonObject.get("status").getAsString();

                    // Преобразуем строковый статус в enum
                    Status status = Status.valueOf(statusStr.toUpperCase()); // Предполагается, что статус передается в нижнем регистре

                    // Сохраняем новую задачу
                    int id = taskManager.addEpic(name, description, status);

                    // Возвращаем ответ
                    String response = "{ \"id\": " + id + " }"; // Возвращаем ID созданной задачи
                    sendText(exchange, response, 201);

                }

                break;
            }
            case "DELETE": {
                Integer id = getIdFromUrl(exchange.getRequestURI().getPath());
                if (id == null) {
                    sendNotFound(exchange);
                } else {
                    Task task = taskManager.getEpic(id);
                    if (task != null) {
                        taskManager.deleteEpic(id);
                        String response = "{ \"response\": \"Сабтаск удален\" }"; // Возвращаем ID созданной задачи
                        sendText(exchange, response, 200);
                    } else {
                        sendNotFound(exchange);
                    }
                }
                break;
            }
            default:
                throw new RuntimeException();
        }
    }

}
