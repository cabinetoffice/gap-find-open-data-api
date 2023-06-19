package gov.cabinetoffice.api.prototype.enums;

import java.util.Map;

import static gov.cabinetoffice.api.prototype.constants.ValidationMaps.LONG_ANSWER_VALIDATION;
import static gov.cabinetoffice.api.prototype.constants.ValidationMaps.NO_VALIDATION;
import static gov.cabinetoffice.api.prototype.constants.ValidationMaps.NUMERIC_ANSWER_VALIDATION;
import static gov.cabinetoffice.api.prototype.constants.ValidationMaps.SHORT_ANSWER_VALIDATION;
import static gov.cabinetoffice.api.prototype.constants.ValidationMaps.SINGLE_FILE_UPLOAD_VALIDATION;

public enum ResponseTypeEnum {

	YesNo(NO_VALIDATION), SingleSelection(NO_VALIDATION), Dropdown(NO_VALIDATION), MultipleSelection(NO_VALIDATION),
	ShortAnswer(SHORT_ANSWER_VALIDATION), LongAnswer(LONG_ANSWER_VALIDATION), AddressInput(NO_VALIDATION),
	Numeric(NUMERIC_ANSWER_VALIDATION), Date(NO_VALIDATION), SingleFileUpload(SINGLE_FILE_UPLOAD_VALIDATION);

	private final Map<String, Object> validation;

	ResponseTypeEnum(Map<String, Object> validation) {
		this.validation = validation;
	}

	public Map<String, Object> getValidation() {
		return validation;
	}

}
