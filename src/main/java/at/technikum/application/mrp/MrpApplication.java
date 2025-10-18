package at.technikum.application.mrp;

import at.technikum.application.common.Application;
import at.technikum.application.common.Controller;
import at.technikum.application.common.Router;
import at.technikum.server.http.ContentType;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

public class MrpApplication implements Application {
    private final Router router = new Router();

    public MrpApplication() {
        router.addRoute("/api/users",  new at.technikum.application.mrp.controller.AuthController()); //create an object, pass that object to router.addRoute
        router.addRoute("/api/media", new at.technikum.application.mrp.controller.MediaController());
    }

    @Override
    public Response handle(Request request) {
        var target = router.findController(request.getPath());
        if (target.isEmpty()) {
            return status(Status.NOT_FOUND);
        }
        try {
            return target.get().handle(request); //.get(): predefined method from java.util.Optional, returns contained Controller object in target
        } catch (IllegalArgumentException e) {        // e.g., bad id/validation
            return status(Status.BAD_REQUEST);
        } catch (Exception e) {                       // fallback
            return status(Status.INTERNAL_SERVER_ERROR);
        }
    }

    // local helper (since we can’t use Controller’s instance helpers here)
    private Response status(Status s) {
        Response r = new Response();
        r.setStatus(s);
        r.setContentType(ContentType.TEXT_PLAIN);
        r.setBody(s.getMessage()); // "Not Found", etc.
        return r;
    } //creates small text/plain error response
}
