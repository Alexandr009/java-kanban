import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    public List<Task> listHistory;
    public InMemoryHistoryManager(){
        listHistory = new ArrayList<Task>(10);
    }
    @Override
    public List<Task> getHistory(){
        return listHistory;
    }
    @Override
    public void addTaskToHistory(Task task){
        if(listHistory.size() == 10){
            listHistory.remove(0);
        }
        listHistory.add(task);
    }
}
