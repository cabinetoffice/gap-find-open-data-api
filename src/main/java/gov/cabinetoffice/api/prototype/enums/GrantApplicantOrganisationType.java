package gov.cabinetoffice.api.prototype.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum GrantApplicantOrganisationType {

	LIMITED_COMPANY("Limited Company"), UNLIMITED_COMPANY("Unlimited Company"),
	REGISTERED_CHARITY("Registered Charity"), OTHER("Other");

	private final String name;

	GrantApplicantOrganisationType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static GrantApplicantOrganisationType valueOfName(String name) {
		return Arrays.stream(values()).filter(type -> type.name.equals(name)).findFirst().orElse(null);
	}

	@JsonCreator
	public static GrantApplicantOrganisationType getGrantApplicantOrganisationTypeFromName(String name) {

		return valueOfName(name);

	}

}
