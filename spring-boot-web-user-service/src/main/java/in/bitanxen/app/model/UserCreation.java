package in.bitanxen.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "tb_user_creation")
@Getter
@Setter
public class UserCreation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "entry_id")
    private String entryId;

    @Column(name = "case_id")
    private String caseId;

    @Column(name = "case_status")
    private String caseStatus;

    @Column(name = "case_status_desc")
    private String caseStatusDesc;

    @Column(name = "created_by")
    private String createdBy;

    private LocalDateTime createdOn;
}
