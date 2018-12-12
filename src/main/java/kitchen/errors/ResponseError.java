package kitchen.errors;

public class ResponseError extends Exception {
    private String message;
    private StackTraceElement[] stackTrace;

    public ResponseError(String message, String... args) {
        this.message = String.format(message, (Object[]) args);
    }

    public ResponseError(Exception e) {
        this.message = e.getMessage();
        this.stackTrace = e.getStackTrace();
    }

    public String getMessage() {
        return this.message;
    }

    public StackTraceElement[] getStackTrace() { return stackTrace; }
}
