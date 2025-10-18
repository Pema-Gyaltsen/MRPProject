package at.technikum.application.mrp.mrpexception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String msg) { super(msg); }
}
