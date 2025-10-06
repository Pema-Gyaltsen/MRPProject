// at/technikum/application/common/Controller.java
package at.technikum.application.common;

import at.technikum.server.http.ContentType;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public abstract class Controller {

    // ----- JSON codec kept at the edge (controller layer) -----
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    protected <T> T readJson(Request req, Class<T> type) throws Exception {
        return MAPPER.readValue(req.getBody(), type);
    }

    protected Response json(Object body, Status status) {
        try {
            String s = MAPPER.writeValueAsString(body);
            Response r = new Response();
            r.setStatus(status);
            r.setContentType(ContentType.APPLICATION_JSON);
            r.setBody(s);
            return r;
        } catch (Exception e) {
            return status(Status.INTERNAL_SERVER_ERROR);
        }
    }

    protected Response json(Object body) { return json(body, Status.OK); }

    // ----- existing helpers from professor -----
    public abstract Response handle(Request request);

    protected Response ok() { return status(Status.OK); }

    protected Response status(Status status) {
        return text(status.getMessage(), status);
    }

    protected Response text(String text) { return text(text, Status.OK); }

    protected Response text(String text, Status status) {
        Response response = new Response();
        response.setStatus(status);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody(text);
        return response;
    }
}
