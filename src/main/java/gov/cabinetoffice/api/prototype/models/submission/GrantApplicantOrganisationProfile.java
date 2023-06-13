package gov.cabinetoffice.api.prototype.models.submission;

import gov.cabinetoffice.api.prototype.enums.GrantApplicantOrganisationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Entity
@Table
public class GrantApplicantOrganisationProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "applicant_id", referencedColumnName = "id")
    private GrantApplicant applicant;

    @Column
    private String legalName;

    @Column
    @Enumerated(EnumType.STRING)
    private GrantApplicantOrganisationType type;

    @Column
    private String addressLine1;

    @Column
    private String addressLine2;

    @Column
    private String town;

    @Column
    private String county;

    @Column
    private String postcode;

    @Column
    private String charityCommissionNumber;

    @Column
    private String companiesHouseNumber;

}
