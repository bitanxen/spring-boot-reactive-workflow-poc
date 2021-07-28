package in.bitanxen.app.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tb_case_workflow")
public class Case {

    private String caseId;

}
