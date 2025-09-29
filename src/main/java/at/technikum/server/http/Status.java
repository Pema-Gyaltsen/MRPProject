package at.technikum.server.http;

public enum Status {
    OK(200), CREATED(201), NO_CONTENT(204),
    BAD_REQUEST(400), UNAUTHORIZED(401), FORBIDDEN(403), NOT_FOUND(404),
    CONFLICT(409),
    INTERNAL_SERVER_ERROR(500);

    public final int code;
    Status(int c){ this.code = c; }
}
