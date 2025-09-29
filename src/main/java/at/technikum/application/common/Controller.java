package at.technikum.application.common;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public abstract class Controller {
    protected Request req;
    protected Response res;
    public void bind(Request req, Response res){ this.req = req; this.res = res; }
}
