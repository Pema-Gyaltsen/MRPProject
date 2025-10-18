// ExceptionMapper.java
package at.technikum.application.mrp.mrpexception;

import at.technikum.server.http.ContentType;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

import java.util.HashMap;
import java.util.Map;

public class ExceptionMapper {

    private final Map<Class<?>, Status> map = new HashMap<>();

    public ExceptionMapper() {
        // map custom exceptions to HTTP
        register(BadRequestException.class,     Status.BAD_REQUEST);          // 400
        register(UnauthorizedException.class,   Status.UNAUTHORIZED);         // 401
        register(ForbiddenException.class,      Status.FORBIDDEN);            // 403
        register(NotFoundException.class,       Status.NOT_FOUND);            // 404
        register(ConflictException.class,       Status.CONFLICT);             // 409
        register(JsonConversionException.class, Status.BAD_REQUEST);          // 400
    }

    public void register(Class<?> clazz, Status status) { map.put(clazz, status); }

    public Response toResponse(Exception ex) {
        Status status = map.get(ex.getClass());
        if (status == null) status = Status.INTERNAL_SERVER_ERROR;  // <- correct fallback

        String body = "{\"error\":\"" + ex.getClass().getSimpleName() + "\",\"message\":\"" +
                safe(ex.getMessage()) + "\"}";

        Response r = new Response();
        r.setStatus(status);
        r.setContentType(ContentType.APPLICATION_JSON);
        r.setBody(body);
        return r;
    }

    private String safe(String s) { return (s == null) ? "" : s.replace("\"","\\\""); }
}
