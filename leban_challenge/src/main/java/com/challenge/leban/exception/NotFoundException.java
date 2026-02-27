package com.challenge.leban.exception;

public class NotFoundException extends CoreException {
    
    private final int statusCode;
	private final String codeMessage;

	public NotFoundException() {
		statusCode = 404;
		codeMessage = "exception.notFound";
	}

	public NotFoundException(String message) {
		super(message);
		statusCode = 404;
		codeMessage = "exception.notFound";
	}

	public NotFoundException(String message, String codeMessage, int statusCode) {
		super(message);
		this.statusCode = statusCode;
		this.codeMessage = codeMessage;
	}

	@Override
	public Integer getStatusCode() {
		return statusCode;
	}

	@Override
	public String getCodeMessage() {
		return codeMessage;
	}

}
