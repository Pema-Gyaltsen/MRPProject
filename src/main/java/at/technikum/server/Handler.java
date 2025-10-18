package at.technikum.server;

import at.technikum.application.common.Application;
import at.technikum.application.mrp.mrpexception.ExceptionMapper; // <-- add
import at.technikum.server.http.Response;
import at.technikum.server.util.RequestMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Handler implements HttpHandler {
    private final Application application;
    private final RequestMapper mapper = new RequestMapper();
    private final ExceptionMapper exceptions = new ExceptionMapper(); // <-- add

    public Handler(Application application){ this.application = application; }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        Response resp;
        try {
            var req = mapper.fromExchange(ex);
            resp = application.handle(req); // your normal routing
        } catch (Exception e) {
            // map any thrown exception to an HTTP response (JSON + proper status)
            resp = exceptions.toResponse(e);
        }
        send(ex, resp);
    }

    private void send(HttpExchange ex, Response r) throws IOException {
        ex.getResponseHeaders().set("Content-Type", r.getContentType());
        byte[] bytes = (r.getStatusCode() == 204) ? new byte[0]
                : r.getBody().getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(r.getStatusCode(), bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            if (bytes.length > 0) os.write(bytes);
        }
    }
}
