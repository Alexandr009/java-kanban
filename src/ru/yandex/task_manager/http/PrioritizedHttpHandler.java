package ru.yandex.task_manager.http;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.task_manager.manager.HistoryManager;
import ru.yandex.task_manager.manager.TaskManager;
import ru.yandex.task_manager.task.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHttpHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET": {
                List<Task> listPrioritiz = taskManager.getPrioritizedTasks();
                sendText(exchange,listPrioritiz.toString(),200);
                break;
            }
            default:
                sendNotFound(exchange);
                throw new RuntimeException();
        }
    }

}
