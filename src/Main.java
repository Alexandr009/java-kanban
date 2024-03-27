import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner scanner;
    static TaskManager taskManager;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        HashMap<Integer, ArrayList<Task>> taskOdject = new HashMap<>();
        HashMap<Integer, ArrayList<Epic>> epicOdject = new HashMap<>();
        HashMap<Integer, ArrayList<Subtask>> subtaskOdject = new HashMap<>();
        taskManager = new TaskManager(taskOdject, epicOdject, subtaskOdject);

        while (true) {
            printMenu();
            int command = scanner.nextInt();

            switch (command) {
                case 100:
                    HashMap<Integer, Task> listTask = taskManager.getAllTask();
                    System.out.println("Task :" + listTask);
                case 1:
                    System.out.println("Введите название задачи");
                    String name = scanner.next();
                    System.out.println("Введите описание задачи");
                    String description = scanner.next();
                    addTask(name, description, Status.NEW);
                    break;
                case 2:
                    System.out.println("Введите id задачи");
                    int id = scanner.nextInt();
                    Task task = taskManager.getTask(id);
                    System.out.println("task = " + task);
                    break;
                case 3:
                    taskManager.deleteAllTask();
                    break;
                case 4:
                    System.out.println(taskManager.toString());
                    break;
                case 5:
                    updateTask();
                    break;
                case 6:
                    System.out.println("Введите id задачи");
                    int idTask = scanner.nextInt();
                    deleteTaskRecord(idTask);
                    break;
                case 11:
                    System.out.println("Введите название Epic");
                    String nameEpic = scanner.next();
                    System.out.println("Введите описание Epic");
                    String descriptionEpic = scanner.next();
                    addEpic(nameEpic, descriptionEpic, Status.NEW);
                    break;
                case 12:
                    System.out.println("Введите id epic");
                    int idEpic = scanner.nextInt();
                    Epic epic = taskManager.getEpic(idEpic);
                    System.out.println("epic = " + epic);
                    break;
                case 13:
                    taskManager.deleteAllEpic();
                    break;
                case 14:
                    HashMap<Integer, Epic> listEpic = taskManager.getAllEpic();
                    System.out.println("Epic :" + listEpic);
                    break;
                case 15:
                    updateEpic();
                    break;
                case 16:
                    System.out.println("Введите id Epic");
                    deleteEpicRecord(scanner.nextInt());
                case 17:
                    System.out.println("Введите id Epic");
                    Epic epicSubtask = taskManager.getEpic(scanner.nextInt());
                    System.out.println("epic list Subtasks -" + epicSubtask);
                    break;
                case 21:
                    System.out.println("Введите название Subtask");
                    String nameSubtask = scanner.next();
                    System.out.println("Введите описание Subtask");
                    String descriptionSubtask = scanner.next();
                    System.out.println("Введите id epic для Subtask");
                    int idEpicParent = scanner.nextInt();
                    addSubtask(nameSubtask, descriptionSubtask, Status.NEW, idEpicParent);
                    break;
                case 22:
                    System.out.println("Введите id Subtask");
                    Subtask subtask = taskManager.getSubtask(scanner.nextInt());
                    System.out.println("subtask = " + subtask);
                    break;

                case 23:
                    taskManager.deleteAllSubtask();
                    break;
                case 24:
                    HashMap<Integer, Subtask> listSubtask = taskManager.getAllSubtask();
                    System.out.println("Subtask :" + listSubtask);
                    break;
                case 25:
                    updateSubtask();
                    break;
                case 26:
                    System.out.println("Введите id Subtask");
                    deleteSubtaskRecord(scanner.nextInt());
                    break;
                case 0:
                    return;
            }

        }

    }

    private static void printMenu() {
        System.out.println("Выберите команду:");
        System.out.println("100 - Показать список всех Таск");
        System.out.println("1 - Добавить новою Таск");
        System.out.println("2 - Показать Таск по Id");
        System.out.println("3 - Удалить все Таск");
        System.out.println("4 - Показать все Таск");
        System.out.println("5 - Обновить Таск по ид");
        System.out.println("6 - Удалить Таск по ид");
        /////////////////////////////////////////
        System.out.println("11 - Добавить новою Epic");
        System.out.println("12 - Показать Epic по Id");
        System.out.println("13 - Удалить все Epic");
        System.out.println("14 - Показать все Epic");
        System.out.println("15 - Обновить Epic");
        System.out.println("16 - Удалить Epic по ид");
        System.out.println("17 - Получить все Subtask по ид Epic");
        /////////////////////////////////////////
        System.out.println("21 - Добавить новою Subtask");
        System.out.println("22 - Показать Subtask по Id");
        System.out.println("23 - Удалить все Subtask");
        System.out.println("24 - Показать все Subtask");
        System.out.println("25 - Обновить Subtask");
        System.out.println("26 - Удалить Subtask по ид");
        ///////////////////////////////////////
        System.out.println("0 - Выход");
    }

    private static void updateTask() {
        System.out.println("Введите id задачи");
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
        int idNew = taskManager.addTask(name, description, status);
        System.out.println("id = " + idNew);
    }

    private static void addEpic(String name, String description, Status status) {
        int idNew = taskManager.addEpic(name, description, status);
        System.out.println("id = " + idNew);
    }

    private static void deleteEpicRecord(int id) {
        taskManager.deleteEpic(id);
    }

    private static void updateEpic() {
        System.out.println("Введите id Epic");
        int id = scanner.nextInt();

        System.out.println("Введите новое название epic");
        String name = scanner.next();
        System.out.println("Введите новое описание epic");
        String description = scanner.next();

        taskManager.updateEpic(id, name, description);
    }

    private static void addSubtask(String name, String description, Status status, int idEpic) {
        int idNew = taskManager.addSubtask(name, description, status, idEpic);
        System.out.println("id = " + idNew);
    }

    private static void updateSubtask() {
        System.out.println("Введите id Subtask");
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