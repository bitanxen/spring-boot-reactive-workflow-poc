package in.bitanxen.app.config;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorkflowRegistrationContextHolder {

    private static List<WorkflowRegistration> registrations = new CopyOnWriteArrayList<>();

    private WorkflowRegistrationContextHolder() {}

    public static List<WorkflowRegistration> getRegistrations() {
        return registrations;
    }

    public static void setRegistrations(List<WorkflowRegistration> workflowRegistrations) {
        registrations = new CopyOnWriteArrayList<>(workflowRegistrations);
    }
}
