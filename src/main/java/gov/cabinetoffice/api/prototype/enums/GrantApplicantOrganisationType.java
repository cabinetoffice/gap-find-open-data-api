package gov.cabinetoffice.api.prototype.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GrantApplicantOrganisationType {

    LIMITED_COMPANY("Limited Company"), UNLIMITED_COMPANY("Unlimited Company"),
    REGISTERED_CHARITY("Registered Charity"), OTHER("Other");

    private String name;

    private GrantApplicantOrganisationType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static GrantApplicantOrganisationType valueOfName(String name) {
        for (GrantApplicantOrganisationType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }

    @JsonCreator
    public static GrantApplicantOrganisationType getGrantApplicantOrganisationTypeFromName(String name) {

        return valueOfName(name);

    }

}
