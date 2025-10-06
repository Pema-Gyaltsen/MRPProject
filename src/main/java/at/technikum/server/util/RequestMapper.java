package at.technikum.server.util;
import at.technikum.server.http.Method;
import at.technikum.server.http.Request;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class RequestMapper {
    public Request fromExchange(HttpExchange ex) throws IOException {
        Request r = new Request();
        r.setMethod(Method.valueOf(ex.getRequestMethod()));
        URI uri = ex.getRequestURI();
        r.setPath(uri.getPath());
        r.setQuery(uri.getRawQuery());
        ex.getRequestHeaders().forEach((k,v)-> r.setHeader(k, String.join(",", v)));
        byte[] bytes = ex.getRequestBody().readAllBytes();
        r.setBody(new String(bytes, StandardCharsets.UTF_8));
        return r;
    }
}
