package com.auca.finalproject.payload;

public class JTWLoginSuccessResponse {
  
	private boolean success;
	private String token;
	
	public JTWLoginSuccessResponse(boolean success, String token) {
		this.success = success;
		this.token = token;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
