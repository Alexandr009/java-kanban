import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;

public class HttpTaskServer {
    private final TaskManager taskManager;
    private static HttpServer httpServer;
    private final static int PORT = 8080;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost",PORT),0);
        this.httpServer.createContext("/task", (HttpHandler) new TasksHttpHandler());
    }

    public static void main(String[] args) throws IOException{
        TaskManager taskManager = Managers.getDefault(null,null,null);
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    public void start(){
        httpServer.start();
    }

    public void stop(){
        httpServer.stop(0);
    }
}
