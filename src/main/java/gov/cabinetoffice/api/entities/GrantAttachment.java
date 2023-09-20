package gov.cabinetoffice.api.entities;

import gov.cabinetoffice.api.enums.GrantAttachmentStatus;
import jakarta.persistence.*;
import lombok.*;

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

//	commented this out because it's an unused property that negatively impacts performance
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "submission_id", referencedColumnName = "id")
//	private Submission submission;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "created_by", referencedColumnName = "id")
//	private GrantApplicant createdBy;

	@Column(name = "question_id", nullable = false)
	private String questionId;

	@Column(name = "version", nullable = false)
	@Builder.Default
	private Integer version = 1;

	@Column(name = "created", nullable = false)
	@Builder.Default
	private Instant created = Instant.now();

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
