package at.technikum.server;
import at.technikum.application.common.Application;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Server {
    private HttpServer http;
    private final int port;
    private final Application app;

    public Server(int port, Application app){ this.port=port; this.app=app; }

    public void start() {
        try {
            http = HttpServer.create(new InetSocketAddress("localhost", port), 0);
            http.createContext("/", new Handler(app));
            http.setExecutor(null);
            http.start();
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}
