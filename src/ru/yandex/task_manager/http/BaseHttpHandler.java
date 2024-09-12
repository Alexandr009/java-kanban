package ru.yandex.task_manager.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.task_manager.task.Status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected void sendText(HttpExchange h, String text, Integer statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, 0);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, 0);
        h.close();
    }

    protected Integer getIdFromUrl(String path) {
        String[] pathParts = path.split("/");
        if (pathParts.length > 2) {
            //get id
            Integer id = Integer.valueOf(pathParts[2]);
          //  System.out.println("id = " + id);
            return id;
        }

        return null;
    }


   protected Status getStatusCode (String status){
       switch (status) {
           case "IN_PROGRESS":
               return Status.IN_PROGRESS;
           case "DONE":
               return Status.DONE;
           default:
               return Status.NEW;
       }
   }
}