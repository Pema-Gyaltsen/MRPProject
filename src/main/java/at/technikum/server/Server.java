package at.technikum.server;

import at.technikum.application.common.Router;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Server {
    private final HttpServer http;
    public Server(Router router){
        try{
            http = HttpServer.create(new InetSocketAddress(8080), 0);
            http.createContext("/", new Handler(router));
            http.setExecutor(null);
        }catch(Exception e){ throw new RuntimeException(e); }
    }
    public void start(){ http.start(); }
}
