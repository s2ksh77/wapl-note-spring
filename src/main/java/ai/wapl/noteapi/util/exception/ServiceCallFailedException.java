package ai.wapl.noteapi.util.exception;

public class ServiceCallFailedException extends RuntimeException {

    public ServiceCallFailedException() {}

    public ServiceCallFailedException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
