package ai.wapl.noteapi.util.exception;

public class UnauthorizedException extends RuntimeException {
    String message;

    public UnauthorizedException() {

    }
    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
