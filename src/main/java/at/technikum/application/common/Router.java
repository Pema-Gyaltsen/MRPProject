package at.technikum.application.common;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.sun.net.httpserver.HttpExchange;
import java.util.*;
import java.util.regex.*;

public class Router {
    private final List<Route> routes = new ArrayList<>();

    public Router add(Route r){ routes.add(r); return this; }

    public void handle(HttpExchange ex){
        Request req = new Request(ex);
        Response res = new Response(ex);
        String m = req.method();
        String p = req.path();

        for(Route r : routes){
            if(!r.method.equalsIgnoreCase(m)) continue;
            if(Pattern.matches(r.pathPattern, p)){
                r.handler.accept(ex);
                return;
            }
        }
        res.text(at.technikum.server.http.Status.NOT_FOUND, "Not Found");
    }
}
