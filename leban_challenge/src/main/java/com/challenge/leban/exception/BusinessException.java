package com.challenge.leban.exception;

public class BusinessException extends CoreException {
    

    private final int statusCode;
	private final String codeMessage;

	public BusinessException() {
		statusCode = 400;
		codeMessage = "exception.business";
	}

	public BusinessException(String message) {
		super(message);
		statusCode = 400;
		codeMessage = "exception.business";
	}

	public BusinessException(String message, String codeMessage, int statusCode) {
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
