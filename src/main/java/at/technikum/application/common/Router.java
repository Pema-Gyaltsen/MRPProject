package at.technikum.application.common;

import java.util.ArrayList; //resizable list implementation
import java.util.List; //interface for ordered sequences
import java.util.Optional; //container that may hold a value or be empty (avoids null)

public class Router {

    private List<Route> routes; //generic List of Route object

    public Router() { this.routes = new ArrayList<>();} //constructor


    //given an incoming request path (/api/media/), find the matching Controller
    public Optional<Controller> findController(String path) {
        //optional<Controllre: may be a controller or may be not, cuz may not find a Controller, also allowsCallerToHandle"NULL" case safely
        for (Route route: this.routes) { //enhanced for-loop over routes, java for each loop, for each route inside this.routes, do the body
            if (path.startsWith(route.getPath())) { //prefix match on strings
                return Optional.of(route.getController()); //if matching route, return Controller wrapped in Optional: wraps a non-null value; if value null, throw NullPointerException
            }
        }
        return Optional.empty(); //loop finished without route match, creates and returns an empty Optional with no value inside
    }

    public void addRoute(String path, Controller controller) {
        routes.add(
                new Route(path, controller)
        );
    }
}
