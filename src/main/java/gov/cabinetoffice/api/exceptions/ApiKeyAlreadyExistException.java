package gov.cabinetoffice.api.exceptions;

public class ApiKeyAlreadyExistException extends RuntimeException {

	public ApiKeyAlreadyExistException(String message) {
		super(message);
	}

}
