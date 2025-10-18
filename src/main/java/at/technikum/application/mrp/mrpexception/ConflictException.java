//e.g., username taken

package at.technikum.application.mrp.mrpexception;

public class ConflictException extends RuntimeException {
    public ConflictException(String msg) { super(msg); }
}
