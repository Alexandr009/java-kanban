package ru.yandex.task_manager.http;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.task_manager.manager.HistoryManager;
import ru.yandex.task_manager.manager.Managers;
import ru.yandex.task_manager.manager.TaskManager;
import ru.yandex.task_manager.task.Epic;
import ru.yandex.task_manager.task.Subtask;
import ru.yandex.task_manager.task.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class HttpTaskServer {
    private static TaskManager taskManager;
    private static HistoryManager historyManager;
    private static HttpServer httpServer;
    private final static int PORT = 8080;

    public HttpTaskServer(TaskManager taskManager, HistoryManager historyManager) throws IOException {
        this.taskManager = taskManager;
        this.historyManager = historyManager;
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        this.httpServer.createContext("/tasks", (HttpHandler) new TasksHttpHandler(taskManager));
        this.httpServer.createContext("/epics", (HttpHandler) new EpicsHttpHandler(taskManager));
        this.httpServer.createContext("/subtasks", (HttpHandler) new SubtasksHttpHandler(taskManager));
        this.httpServer.createContext("/history", (HttpHandler) new HistoryHttpHandler(historyManager));
        this.httpServer.createContext("/prioritized", (HttpHandler) new PrioritizedHttpHandler(taskManager));
    }

    public static void main(String[] args) throws IOException {
        HashMap<Integer, Task> taskOdject = new HashMap<>();
        HashMap<Integer, Epic> epicOdject = new HashMap<>();
        HashMap<Integer, Subtask> subtaskOdject = new HashMap<>();
        taskManager = Managers.getDefault(taskOdject, epicOdject, subtaskOdject);
        historyManager = Managers.getDefaultHistory();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager,historyManager);
        httpTaskServer.start();
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }
}
