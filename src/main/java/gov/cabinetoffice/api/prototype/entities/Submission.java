package gov.cabinetoffice.api.prototype.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.cabinetoffice.api.prototype.enums.SubmissionStatus;
import gov.cabinetoffice.api.prototype.models.submission.GrantApplicant;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionDefinition;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "grant_submission")
public class Submission {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "applicant_id", referencedColumnName = "id")
    @JsonIgnoreProperties("submissions")
    @ToString.Exclude
    private GrantApplicant applicant;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "scheme_id")
    @ToString.Exclude
    private SchemeEntity scheme;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "application_id")
    @JsonIgnoreProperties("schemes")
    @ToString.Exclude
    private ApplicationFormEntity application;

    @Column
    private int version;

    @CreatedDate
    private LocalDateTime created;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @JsonIgnoreProperties("submissions")
    @ToString.Exclude
    private GrantApplicant createdBy;

    @LastModifiedDate
    private LocalDateTime lastUpdated;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "last_updated_by", referencedColumnName = "id")
    @JsonIgnoreProperties("submissions")
    @ToString.Exclude
    private GrantApplicant lastUpdatedBy;

    @Column
    private ZonedDateTime submittedDate;

    @Column
    private String applicationName;

    @Column
    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private SubmissionDefinition definition;

    @Column(name = "gap_id")
    private String gapId;

    @Column(name = "last_required_checks_export")
    private Instant lastRequiredChecksExport;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Submission that = (Submission) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}