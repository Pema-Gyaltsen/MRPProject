package at.technikum.server;

import at.technikum.application.common.Router;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Handler implements HttpHandler {
    private final Router router;
    public Handler(Router router){ this.router = router; }
    @Override public void handle(HttpExchange exchange){
        router.handle(exchange);
    }
}
