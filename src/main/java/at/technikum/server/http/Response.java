package at.technikum.server.http;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Response {
    private final HttpExchange ex;
    public Response(HttpExchange ex){ this.ex = ex; }

    public void send(Status status, String contentType, String body){
        try {
            byte[] bytes = body == null ? new byte[0] : body.getBytes(StandardCharsets.UTF_8);
            ex.getResponseHeaders().set("Content-Type", contentType);
            ex.sendResponseHeaders(status.code, bytes.length);
            try(OutputStream os = ex.getResponseBody()){ os.write(bytes); }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            ex.close();
        }
    }

    public void json(Status status, String json){ send(status, ContentType.JSON, json); }
    public void text(Status status, String text){ send(status, ContentType.TEXT, text); }
}
