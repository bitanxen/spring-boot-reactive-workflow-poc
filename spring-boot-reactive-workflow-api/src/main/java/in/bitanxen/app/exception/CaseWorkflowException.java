package in.bitanxen.app.exception;

public class CaseWorkflowException extends RuntimeException {

    public CaseWorkflowException(String msg) {
        super(msg);
    }

    public CaseWorkflowException(Throwable t) {
        super(t);
    }
}
