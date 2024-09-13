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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TasksHttpHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public TasksHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET": {
                    Integer id = getIdFromUrl(exchange.getRequestURI().getPath());
                    if (id == null) {
                        List<Task> list = (List<Task>) taskManager.getAllTask().values().stream().collect(Collectors.toList());
                        // Преобразуем список задач в JSON
                        Gson gson = getGson();
                        String responsCovert = gson.toJson(list);

                        sendText(exchange, responsCovert, 200);

                    } else {
                        Task task = taskManager.getTask(id);
                        Gson gson = getGson();
                        String covert = gson.toJson(task);
                        sendText(exchange, covert, 200);


                    }
                    break;
                }
                case "POST": {
                    String body = readBody(exchange);

                    // Парсим JSON с использованием JsonElement и JsonObject
                    JsonElement jsonElement = JsonParser.parseString(body);//
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        String name = jsonObject.get("name").getAsString();
                        String description = jsonObject.get("description").getAsString();
                        String status = jsonObject.get("status").getAsString();
                        Status newStatus = getStatusCode(String.valueOf(status));
                        // Преобразуем строковый статус в enum

                        // Сохраняем новую задачу
                        Integer idPath = getIdFromUrl(exchange.getRequestURI().getPath());
                        //addTask
                        if (idPath == null) {

                            int id = taskManager.addTask(name, description, newStatus);
                            String response = "{ \"id\": " + id + " }"; // Возвращаем ID созданной задачи
                            sendText(exchange, response, 201);

                        } else {//updateTask
                            Task task = taskManager.getTask(idPath);
                            if (task != null) {
                                //boolean check = taskManager.checkPrioritizedTasks(task);
                                List<Task> listPrioritiz = taskManager.getPrioritizedTasks();
                                int idPrioritiz = listPrioritiz.get(idPath).idTask;
                                if (listPrioritiz.size() > 0 && idPrioritiz != idPath) {
                                    sendHasInteractions(exchange);
                                } else {

                                    taskManager.updateTask(idPath, name, description, newStatus);
                                    String response = "{ \"id\": " + idPath + " }"; // Возвращаем ID созданной задачи
                                    sendText(exchange, response, 200);
                                }
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
        } catch (Exception e) {
            sendInternalServerError(exchange, e.getMessage());
        }
    }


}
