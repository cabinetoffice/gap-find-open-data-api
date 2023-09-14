package gov.cabinetoffice.api.entities;

import jakarta.persistence.*;
import lombok.*;

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
	private String userId;

	//commented this out because it's an unused property
//	@OneToOne(mappedBy = "applicant")
//	@JsonIgnoreProperties
//	private GrantApplicantOrganisationProfile organisationProfile;

//	@OneToMany(mappedBy = "applicant")
//	@Builder.Default
//	private List<Submission> submissions = new ArrayList<>();

}
