package at.technikum.application.mrp.mrpexception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String msg) { super(msg); }
}
