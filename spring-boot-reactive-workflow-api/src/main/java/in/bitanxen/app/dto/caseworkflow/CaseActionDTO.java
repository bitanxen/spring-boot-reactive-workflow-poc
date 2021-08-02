package in.bitanxen.app.dto.caseworkflow;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CaseActionDTO {
    private String actionId;
    private Map<String, String> actionFieldDetails;
    private String comment;
}
