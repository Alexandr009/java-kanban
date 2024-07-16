import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;
public class Main {
    static Scanner scanner;
    static TaskManager taskManager;
    static HistoryManager historyManager;
    static FileBackedTaskManager taskManagerBacked;
    public static final String FILE = "C:/Users/Aleksandr.Abramovich/javaTrening/task-manager/source/file.txt";

    public static void main(String[] args) throws IOException {
        scanner = new Scanner(System.in);
        HashMap<Integer, Task> taskOdject = new HashMap<>();
        HashMap<Integer, Epic> epicOdject = new HashMap<>();
        HashMap<Integer, Subtask> subtaskOdject = new HashMap<>();

        taskManagerBacked = new FileBackedTaskManager(taskOdject, epicOdject, subtaskOdject);
        taskManagerBacked = FileBackedTaskManager.loadFromFile(FILE);

        taskManager =  Managers.getDefault(taskOdject, epicOdject, subtaskOdject);
        historyManager = Managers.getDefaultHistory();


        while (true) {
            printMenu();
            int command = scanner.nextInt();

            switch (command) {
                case 1:
                    HashMap<Integer, Task> listTask = taskManager.getAllTask();
                    System.out.println("Task :" + listTask);
                    break;
                case 2:
                    System.out.println("Введите название задачи");
                    String name = scanner.next();
                    System.out.println("Введите описание задачи");
                    String description = scanner.next();
                    addTask(name, description, Status.NEW);
                    break;
                case 3:
                    System.out.println("Введите идентификатор задачи");
                    int id = scanner.nextInt();
                    Task task = taskManager.getTask(id);
                    System.out.println("task = " + task);
                    break;
                case 4:
                    taskManager.deleteAllTask();
                    break;
                case 5:
                    System.out.println(taskManager.toString());
                    break;
                case 6:
                    updateTask();
                    break;
                case 7:
                    System.out.println("Введите идентификатор задачи");
                    int idTask = scanner.nextInt();
                    deleteTaskRecord(idTask);
                    break;
                case 8:
                    System.out.println("Введите название Epic");
                    String nameEpic = scanner.next();
                    System.out.println("Введите описание Epic");
                    String descriptionEpic = scanner.next();
                    addEpic(nameEpic, descriptionEpic, Status.NEW);
                    break;
                case 9:
                    System.out.println("Введите идентификатор epic");
                    int idEpic = scanner.nextInt();
                    Epic epic = taskManager.getEpic(idEpic);
                    System.out.println("epic = " + epic);
                    break;
                case 10:
                    taskManager.deleteAllEpic();
                    break;
                case 11:
                    HashMap<Integer, Epic> listEpic = taskManager.getAllEpic();
                    System.out.println("Epic :" + listEpic);
                    break;
                case 12:
                    updateEpic();
                    break;
                case 13:
                    System.out.println("Введите идентификатор Epic");
                    deleteEpicRecord(scanner.nextInt());
                case 14:
                    System.out.println("Введите идентификатор Epic");
                    Epic epicSubtask = taskManager.getEpic(scanner.nextInt());
                    System.out.println("epic list Subtasks -" + epicSubtask);
                    break;
                case 15:
                    System.out.println("Введите название Subtask");
                    String nameSubtask = scanner.next();
                    System.out.println("Введите описание Subtask");
                    String descriptionSubtask = scanner.next();
                    System.out.println("Введите идентификатор epic для Subtask");
                    int idEpicParent = scanner.nextInt();
                    addSubtask(nameSubtask, descriptionSubtask, Status.NEW, idEpicParent);
                    break;
                case 16:
                    System.out.println("Введите идентификатор Subtask");
                    Subtask subtask = taskManager.getSubtask(scanner.nextInt());
                    System.out.println("subtask = " + subtask);
                    break;

                case 17:
                    taskManager.deleteAllSubtask();
                    break;
                case 18:
                    HashMap<Integer, Subtask> listSubtask = taskManager.getAllSubtask();
                    System.out.println("Subtask :" + listSubtask);
                    break;
                case 19:
                    updateSubtask();
                    break;
                case 20:
                    System.out.println("Введите идентификатор Subtask");
                    deleteSubtaskRecord(scanner.nextInt());
                    break;
                case 21:
                    List<Task> listHistory = historyManager.getHistory();
                    System.out.println("listHistory = " + listHistory);
                    break;
                case 0:
                    return;
            }

        }
    }

    private static void printMenu() {
        System.out.println("Выберите команду:");
        System.out.println("1 - Показать список всех Таск");
        System.out.println("2 - Добавить новою Таск");
        System.out.println("3 - Показать Таск по идентификатору");
        System.out.println("4 - Удалить все Таск");
        System.out.println("5 - Показать все Таск");
        System.out.println("6 - Обновить Таск по идентификатору");
        System.out.println("7 - Удалить Таск по идентификатору");
        /////////////////////////////////////////
        System.out.println("8 - Добавить новою Epic");
        System.out.println("9 - Показать Epic по идентификатору");
        System.out.println("10 - Удалить все Epic");
        System.out.println("11 - Показать все Epic");
        System.out.println("12 - Обновить Epic");
        System.out.println("13 - Удалить Epic по идентификатору");
        System.out.println("14 - Получить все Subtask по идентификатору Epic");
        /////////////////////////////////////////
        System.out.println("15 - Добавить новою Subtask");
        System.out.println("16 - Показать Subtask по идентификатору");
        System.out.println("17 - Удалить все Subtask");
        System.out.println("18 - Показать все Subtask");
        System.out.println("19 - Обновить Subtask");
        System.out.println("20 - Удалить Subtask по идентификатору");
        System.out.println("21 - История просмотров задач");
        ///////////////////////////////////////
        System.out.println("0 - Выход");
    }

    private static void updateTask() {
        System.out.println("Введите идентификатор задачи");
        int id = scanner.nextInt();

        System.out.println("Введите новое название задачи");
        String name = scanner.next();
        System.out.println("Введите новое описание задачи");
        String description = scanner.next();

        System.out.println("Введите новоый статус задачи: 1 - IN_PROGRESS, 2 - DONE");
        int status = scanner.nextInt();
        Status newStatus;
        switch (status) {
            case 1:
                newStatus = Status.IN_PROGRESS;
                break;
            case 2:
                newStatus = Status.DONE;
                break;
            default:
                newStatus = Status.NEW;
                break;
        }
        taskManager.updateTask(id, name, description, newStatus);
    }

    private static void deleteTaskRecord(int id) {
        taskManager.deleteTask(id);
    }

    private static void addTask(String name, String description, Status status) {
        int idNew = taskManagerBacked.addTask(name, description, status);
        System.out.println("идентификатор = " + idNew);
    }

    private static void addEpic(String name, String description, Status status) {
        int idNew = taskManagerBacked.addEpic(name, description, status);
        System.out.println("идентификатор = " + idNew);
    }

    private static void deleteEpicRecord(int id) {
        taskManager.deleteEpic(id);
    }

    private static void updateEpic() {
        System.out.println("Введите идентификатор Epic");
        int id = scanner.nextInt();

        System.out.println("Введите новое название epic");
        String name = scanner.next();
        System.out.println("Введите новое описание epic");
        String description = scanner.next();

        taskManager.updateEpic(id, name, description);
    }

    private static void addSubtask(String name, String description, Status status, int idEpic) {
        int idNew = taskManagerBacked.addSubtask(name, description, status, idEpic);
        System.out.println("идентификатор = " + idNew);
    }

    private static void updateSubtask() {
        System.out.println("Введите идентификатор Subtask");
        int id = scanner.nextInt();

        System.out.println("Введите новое название Subtask");
        String name = scanner.next();
        System.out.println("Введите новое описание Subtask");
        String description = scanner.next();

        System.out.println("Введите новоый статус Subtask: 1 - IN_PROGRESS, 2 - DONE");
        int status = scanner.nextInt();
        Status newStatus;
        switch (status) {
            case 1:
                newStatus = Status.IN_PROGRESS;
                break;
            case 2:
                newStatus = Status.DONE;
                break;
            default:
                newStatus = Status.NEW;
                break;
        }
        Subtask subtask = taskManager.updateSubtask(id, name, description, newStatus);
        System.out.println("subtask = " + subtask);
    }

    private static void deleteSubtaskRecord(int id) {
        taskManager.deleteSubtask(id);
    }


}