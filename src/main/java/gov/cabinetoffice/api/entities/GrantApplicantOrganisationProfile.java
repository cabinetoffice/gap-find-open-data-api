package gov.cabinetoffice.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "grant_applicant_organisation_profile")
public class GrantApplicantOrganisationProfile {
    // TODO make it as a model
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "legal_name")
    private String legalName;

    @Column
    private String type;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column
    private String town;

    @Column
    private String county;

    @Column
    private String postcode;

    @Column(name = "charity_commission_number")
    private String charityCommissionNumber;

    @Column(name = "companies_house_number")
    private String companiesHouseNumber;

}
