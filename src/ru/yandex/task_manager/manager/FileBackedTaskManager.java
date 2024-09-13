package ru.yandex.task_manager.manager;

import ru.yandex.task_manager.exception.ManagerSaveException;
import ru.yandex.task_manager.task.Epic;
import ru.yandex.task_manager.task.Status;
import ru.yandex.task_manager.task.Subtask;
import ru.yandex.task_manager.task.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTaskManager extends InMemoryTaskManager {

    static HashMap<Integer, Task> listTask;
    static HashMap<Integer, Subtask> listSubtask;
    static HashMap<Integer, Epic> listEpic;
    public static final String FILE = "C:/Users/Aleksandr.Abramovich/javaTrening/task-manager/source/file.txt";
    public static int maxId;

    public FileBackedTaskManager(HashMap<Integer, Task> taskMap, HashMap<Integer, Epic> epicMap, HashMap<Integer, Subtask> subtaskMap) throws IOException {
        super(taskMap,epicMap,subtaskMap);

        listTask = taskMap;
        listEpic = epicMap;
        listSubtask = subtaskMap;

    }

    public static FileBackedTaskManager loadFromFile(String File) throws IOException {
        Path taskFile = Path.of(File);
        List<String> lines = Files.readAllLines(taskFile);

        FileBackedTaskManager manager = null;
        lines.stream()
                .skip(1)  // Пропускаем первую строку (заголовок)
                .map(FileBackedTaskManager::fromString)  // Преобразуем каждую строку в объект ru.yandex.task_manager.task.Task
                .forEach(task -> {
                    switch (task.typeTask) {
                        case TASK:
                            listTask.put(task.idTask, task);
                            break;
                        case EPIC:
                            listEpic.put(task.idTask, (Epic) task);
                            break;
                        case SUBTASK:
                            listSubtask.put(task.idTask, (Subtask) task);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + task.typeTask);
                    }
                });

        try {
            manager = new FileBackedTaskManager(listTask, listEpic, listSubtask);
        } catch (IOException e){
            throw ManagerSaveException.loadException(e);
        }
        return (manager);
    }

    private static Task fromString(String line) {
        String[] taskData = line.split(",");
        String type = taskData[3].trim();

        if (Integer.parseInt(taskData[0].trim()) > maxId){
            maxId = Integer.parseInt(taskData[0].trim());
            counter = maxId;
        }

        Task task = null;

        switch (type) {
            case ("TASK"):
                task = new Task(taskData[1].trim(), taskData[2].trim(), Status.NEW, Integer.parseInt(taskData[0].trim()));
                break;
            case ("EPIC"):
                task = new Epic(taskData[1].trim(), taskData[2].trim(), Status.NEW, Integer.parseInt(taskData[0].trim()));
                break;
            case ("SUBTASK"):
                task = new Subtask(taskData[1].trim(), taskData[2].trim(), Status.NEW, Integer.parseInt(taskData[0].trim()), Integer.parseInt(taskData[5].trim()));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return task;
    }

    @Override
    public int addTask(String name, String description, Status status) {
        int id = super.addTask(name, description, status);
        try {
            saveFile(FILE);
        } catch (IOException e) {
            throw ManagerSaveException.saveException(e);
        }
        return id;
    }

    @Override
    public int addEpic(String name, String description, Status status) {
        int id = super.addEpic(name, description, status);
        try {
            saveFile(FILE);
        } catch (IOException e) {
            throw ManagerSaveException.saveException(e);
        }
        return id;
    }

    @Override
    public int addSubtask(String name, String description, Status status, int idEpic) {
        int id = super.addSubtask(name, description, status, idEpic);
        try {
            saveFile(FILE);
        } catch (IOException e) {
            throw ManagerSaveException.saveException(e);
        }
        return id;
    }

    public void saveFile(String File) throws IOException {
        Path taskFile = Path.of(File);
        if (!Files.exists(taskFile)) {
            Files.createFile(taskFile);
        }

        List<String> lines = new ArrayList<>();
        lines.add("idTask,name,description,typeTask,status,epicId,startTime,duration");
        lines.addAll(
                listTask.values().stream()
                        .map(this::toString)  // Применяем метод toString к каждому элементу
                        .collect(Collectors.toList())  // Собираем результат в список
        );
        lines.addAll(
                listEpic.values().stream()
                        .map(this::toString)  // Применяем метод toString к каждому элементу
                        .collect(Collectors.toList())  // Собираем результат в список
        );
        lines.addAll(
                listSubtask.values().stream()
                        .map(this::toString)  // Применяем метод toString к каждому элементу
                        .collect(Collectors.toList())  // Собираем результат в список
        );

        try {
            Files.write(taskFile, lines, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){
            throw ManagerSaveException.saveException(e);
        }

    }

    private String toString(Task task) {
        return String.join(",", String.valueOf(task.idTask), task.name, task.description, String.valueOf(task.typeTask), task.status.toString(),
                task.startTime != null ? task.startTime.toString() : "", // пустая строка вместо null
                task.duration != null ? task.duration.toString() : "",
                "");
    }

    private String toString(Epic task) {
        return String.join(",", String.valueOf(task.idTask), task.name, task.description,  String.valueOf(task.typeTask), task.status.toString(),
                task.startTime != null ? task.startTime.toString() : "", // пустая строка вместо null
                task.duration != null ? task.duration.toString() : "",
                "");
    }

    private String toString(Subtask task) {
        return String.join(",", String.valueOf(task.idTask), task.name, task.description,  String.valueOf(task.typeTask), task.status.toString(),
                task.startTime != null ? task.startTime.toString() : "", // пустая строка вместо null
                task.duration != null ? task.duration.toString() : "",
                String.valueOf(task.idEpic));
    }


}