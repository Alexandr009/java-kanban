package ru.yandex.task_manager.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.task_manager.task.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

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
            Integer id = Integer.valueOf(pathParts[2]);
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

   protected String readBody(HttpExchange exchange){
       StringBuilder bodyBuilder = new StringBuilder();
       try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
           String line;
           while ((line = reader.readLine()) != null) {
               bodyBuilder.append(line);
           }
       } catch (IOException e) {
           throw new RuntimeException(e);
       }

       return bodyBuilder.toString();
   }
    protected static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls(); // Включает сериализацию null-значений

        // Регистрируем адаптеры для нестандартных типов
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());

        return gsonBuilder.create();
    }
    protected void sendInternalServerError(HttpExchange exchange, String errorMessage) throws IOException {
        String response = "{ \"error\": \"" + errorMessage + "\" }";
        sendText(exchange, response, 500);
    }
}