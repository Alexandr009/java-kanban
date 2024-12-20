package ru.yandex.task_manager.http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.task_manager.manager.TaskManager;
import ru.yandex.task_manager.task.Epic;
import ru.yandex.task_manager.task.Status;
import ru.yandex.task_manager.task.Subtask;
import ru.yandex.task_manager.task.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SubtasksHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public SubtasksHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET": {
                    Integer id = getIdFromUrl(exchange.getRequestURI().getPath());
                    if (id == null) {
                        List<Subtask> list = (List<Subtask>) taskManager.getAllSubtask().values().stream().collect(Collectors.toList());
                        // Преобразуем список задач в JSON
                        Gson gson = getGson();
                        String responsCovert = gson.toJson(list);
                        sendText(exchange, responsCovert, 200);
                    } else {
                        Task task = taskManager.getSubtask(id);
                        Gson gson = getGson();
                        String covert = gson.toJson(task);
                        sendText(exchange, covert, 200);
                    }
                    break;
                }
                case "POST": {
                    String body = readBody(exchange);

                    // Парсим JSON с использованием JsonElement и JsonObject
                    JsonElement jsonElement = JsonParser.parseString(body);
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        String name = jsonObject.get("name").getAsString();
                        String description = jsonObject.get("description").getAsString();
                        String status = jsonObject.get("status").getAsString();
                        String epicIdStr = jsonObject.get("idEpic").getAsString();
                        Status newStatus = getStatusCode(String.valueOf(status));
                        Integer epicId = Integer.valueOf(epicIdStr);

                        Integer idPath = getIdFromUrl(exchange.getRequestURI().getPath());


                        if (idPath == null) {

                            int id = taskManager.addSubtask(name, description, newStatus, epicId);
                            String response = "{ \"id\": " + id + " }"; // Возвращаем ID созданной задачи
                            sendText(exchange, response, 201);

                        } else {//updateTask
                            Subtask subtask = taskManager.getSubtask(idPath);
                            if (subtask != null) {
                                List<Task> listPrioritiz = taskManager.getPrioritizedTasks();

                                if (listPrioritiz.size() == 0) {
                                    taskManager.updateSubtask(idPath, name, description, newStatus);
                                    String response = "{ \"id\": " + idPath + " }"; // Возвращаем ID созданной задачи
                                    sendText(exchange, response, 200);

                                } else {
                                    Task taskPrioritiz = listPrioritiz.getFirst();
                                    ;
                                    if (taskPrioritiz.idTask == subtask.idTask) {
                                        taskManager.updateSubtask(idPath, name, description, newStatus);
                                        String response = "{ \"id\": " + idPath + " }"; // Возвращаем ID созданной задачи
                                        sendText(exchange, response, 200);
                                    } else {
                                        sendHasInteractions(exchange);
                                    }
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
                            taskManager.deleteSubtask(id);
                            String response = "{ \"response\": \"Сабтаск удален\" }"; // Возвращаем ID созданной задачи
                            sendText(exchange, response, 200);
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