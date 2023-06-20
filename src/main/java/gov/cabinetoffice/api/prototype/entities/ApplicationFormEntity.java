package gov.cabinetoffice.api.prototype.entities;

import gov.cabinetoffice.api.prototype.enums.ApplicationStatusEnum;
import gov.cabinetoffice.api.prototype.models.application.ApplicationDefinition;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.Instant;

@Entity
@Table(name = "grant_application")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationFormEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "grant_application_id")
	private Integer grantApplicationId;

	@Column(name = "grant_scheme_id")
	private Integer grantSchemeId;

	@Column(name = "version")
	private Integer version;

	@Column(name = "created")
	private Instant created;

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "last_updated")
	private Instant lastUpdated;

	@Column(name = "last_update_by")
	private Integer lastUpdateBy;

	@Column(name = "last_published")
	private Instant lastPublished;

	@Column(name = "application_name")
	private String applicationName;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ApplicationStatusEnum applicationStatus;

	@Column(name = "definition", nullable = false, columnDefinition = "json")
	@Type(JsonType.class)
	private ApplicationDefinition definition;

}