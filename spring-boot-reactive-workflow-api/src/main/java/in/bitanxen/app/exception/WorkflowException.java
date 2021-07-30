package in.bitanxen.app.exception;

public class WorkflowException extends RuntimeException {

    public WorkflowException(String msg) {
        super(msg);
    }

    public WorkflowException(Throwable throwable) {
        super(throwable);
    }
}
