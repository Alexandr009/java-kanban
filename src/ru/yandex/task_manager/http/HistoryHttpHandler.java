package ru.yandex.task_manager.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.task_manager.manager.HistoryManager;
import ru.yandex.task_manager.manager.TaskManager;
import ru.yandex.task_manager.task.Status;
import ru.yandex.task_manager.task.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class HistoryHttpHandler extends BaseHttpHandler {
        private final HistoryManager historyManager;

    public HistoryHttpHandler(HistoryManager historyManager) {
            this.historyManager = historyManager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            switch (exchange.getRequestMethod()) {
                case "GET": {
                    List<Task> listHistory = historyManager.getHistory();
                    sendText(exchange,listHistory.toString(),200);
                    break;
                }
                default:
                    throw new RuntimeException();
            }
        }

    }
