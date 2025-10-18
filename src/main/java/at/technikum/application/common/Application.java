package at.technikum.application.common; //packages prevent name clashes and control visibility

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public interface Application {

    Response handle(Request request);
}
