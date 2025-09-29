package at.technikum.application.common;

import java.util.function.Consumer;
import com.sun.net.httpserver.HttpExchange;

public class Route {
    public final String method;
    public final String pathPattern; // e.g. ^/api/media/(\\d+)$
    public final Consumer<HttpExchange> handler;

    public Route(String method, String pattern, Consumer<HttpExchange> handler){
        this.method = method;
        this.pathPattern = pattern;
        this.handler = handler;
    }
}
