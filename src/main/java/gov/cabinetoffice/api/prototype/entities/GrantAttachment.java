package gov.cabinetoffice.api.prototype.entities;

import gov.cabinetoffice.api.prototype.enums.GrantAttachmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "grant_attachment")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrantAttachment {

	@Id
	@GeneratedValue
	@Column(name = "grant_attachment_id")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "submission_id", referencedColumnName = "id")
	private Submission submission;

	@Column(name = "question_id", nullable = false)
	private String questionId;

	@Column(name = "version", nullable = false)
	@Builder.Default
	private Integer version = 1;

	@Column(name = "created", nullable = false)
	@Builder.Default
	private Instant created = Instant.now();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", referencedColumnName = "id")
	private GrantApplicant createdBy;

	@Column(name = "last_updated", nullable = false)
	private Instant lastUpdated;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private GrantAttachmentStatus status;

	@Column(name = "filename", nullable = false)
	private String filename;

	@Column(name = "location", nullable = false, columnDefinition = "TEXT")
	private String location;

}
