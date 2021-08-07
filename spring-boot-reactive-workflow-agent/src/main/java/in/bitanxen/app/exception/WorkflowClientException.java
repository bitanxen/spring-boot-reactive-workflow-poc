package in.bitanxen.app.exception;

public class WorkflowClientException extends RuntimeException {

    public WorkflowClientException(String msg) {
        super(msg);
    }

    public WorkflowClientException(Throwable t) {
        super(t);
    }
}
