package at.technikum.server;
import at.technikum.application.common.Application;
import at.technikum.server.http.Response;
import at.technikum.server.util.RequestMapper;
import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Handler implements HttpHandler {
    private final Application application;
    private final RequestMapper mapper = new RequestMapper();
    public Handler(Application application){ this.application = application; }

    @Override public void handle(HttpExchange ex) throws IOException {
        var req = mapper.fromExchange(ex);
        var resp = application.handle(req);
        send(ex, resp);
    }

    private void send(HttpExchange ex, Response r) throws IOException {
        ex.getResponseHeaders().set("Content-Type", r.getContentType());
        byte[] bytes = (r.getStatusCode()==204) ? new byte[0] : r.getBody().getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(r.getStatusCode(), bytes.length);
        try (OutputStream os = ex.getResponseBody()) { if (bytes.length>0) os.write(bytes); }
    }
}
