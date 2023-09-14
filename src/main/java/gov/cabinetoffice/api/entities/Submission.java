package gov.cabinetoffice.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.cabinetoffice.api.enums.SubmissionStatus;
import gov.cabinetoffice.api.models.submission.SubmissionDefinition;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
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

	//commented this out because it's an unused property
//	@ManyToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "applicant_id", referencedColumnName = "id")
//	@JsonIgnoreProperties("submissions")
//	@ToString.Exclude
//	private GrantApplicant applicant;

//		@ManyToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "created_by", referencedColumnName = "id")
//	@JsonIgnoreProperties("submissions")
//	@ToString.Exclude
//	private GrantApplicant createdBy;

//	@ManyToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "last_updated_by", referencedColumnName = "id")
//	@JsonIgnoreProperties("submissions")
//	@ToString.Exclude
//	private GrantApplicant lastUpdatedBy;

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

	@LastModifiedDate
	@Column(name = "last_updated")
	private LocalDateTime lastUpdated;

	@Column(name = "submitted_date")
	private ZonedDateTime submittedDate;

	@Column(name = "application_name")
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