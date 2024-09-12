package ru.yandex.task_manager.http;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.task_manager.manager.TaskManager;
import ru.yandex.task_manager.task.Status;
import ru.yandex.task_manager.task.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

public class TasksHttpHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public TasksHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET": {
                Integer id = getIdFromUrl(exchange.getRequestURI().getPath());
                if (id == null) {
                    HashMap<Integer, Task> list = taskManager.getAllTask();
                    sendText(exchange, list.toString(), 200);

                } else {
                    Task task = taskManager.getTask(id);
                    sendText(exchange, task.toString(), 200);
                }
                break;
            }
            case "POST": {
                //int id2 = taskManager.addTask("1", "11", Status.NEW);
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
                    Integer idPath = getIdFromUrl(exchange.getRequestURI().getPath());
                    //addTask
                    if (idPath == null) {

                        int id = taskManager.addTask(name, description, status);
                        String response = "{ \"id\": " + id + " }"; // Возвращаем ID созданной задачи
                        sendText(exchange, response, 201);

                    } else {//updateTask
                        Task task = taskManager.getTask(idPath);
                        if (task != null) {
                           //boolean check = taskManager.checkPrioritizedTasks(task);

                            Status newStatus = getStatusCode(String.valueOf(status));
                            taskManager.updateTask(idPath, name, description, newStatus);
                            String response = "{ \"id\": " + idPath + " }"; // Возвращаем ID созданной задачи
                            sendText(exchange, response, 200);
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                }

            break;
        }
        case "DELETE": {
            Integer id = getIdFromUrl(exchange.getRequestURI().getPath());
            if (id == null) {
                sendNotFound(exchange);
            } else {
                Task task = taskManager.getTask(id);
                if (task != null) {
                    taskManager.deleteTask(id);
                    String response = "{ \"response\": \"Таск удален\" }"; // Возвращаем ID созданной задачи
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
