package gov.cabinetoffice.api.prototype.models.submission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.cabinetoffice.api.prototype.entities.Submission;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
@Setter
@Getter
public class GrantApplicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private UUID userId;

    @OneToOne(mappedBy = "applicant")
    @JsonIgnoreProperties("applicant")
    private GrantApplicantOrganisationProfile organisationProfile;

    @OneToMany(mappedBy = "applicant")
    @Builder.Default
    private List<Submission> submissions = new ArrayList<>();

}
