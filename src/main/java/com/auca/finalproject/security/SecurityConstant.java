package com.auca.finalproject.security;

public class SecurityConstant {

	public static final String SIGN_UP_URLS = "/users/**";
	public static final String SECRET = "secretKeyToGenerateJWT";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final long EXPIRATION_TIME = 1800000;
}
