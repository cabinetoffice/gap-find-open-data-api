package gov.cabinetoffice.api.prototype.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Setter
@Getter
@Table(name = "grant_applicant")
public class GrantApplicant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "user_id")
	private UUID userId;

	@OneToOne(mappedBy = "applicant")
	@JsonIgnoreProperties
	private GrantApplicantOrganisationProfile organisationProfile;

	@OneToMany(mappedBy = "applicant")
	@Builder.Default
	private List<Submission> submissions = new ArrayList<>();

}
