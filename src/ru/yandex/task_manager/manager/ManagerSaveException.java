package ru.yandex.task_manager.manager;

public class ManagerSaveException extends RuntimeException{
    public static final String MSG_SAVE = "Error saving";
    public static final String MSG_LOAD = "Error loading";

    public static ManagerSaveException saveException(Exception e){
        return new ManagerSaveException(MSG_SAVE,e);
    }

    public static ManagerSaveException loadException(Exception e){
        return new ManagerSaveException(MSG_LOAD,e);
    }
    private ManagerSaveException(String msg,Exception e){
        super(msg,e);

    }
}
