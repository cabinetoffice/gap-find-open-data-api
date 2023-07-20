package gov.cabinetoffice.api.exceptions;

public class ApiKeyDoesNotExistException extends RuntimeException {

	public ApiKeyDoesNotExistException(String message) {
		super(message);
	}

}
