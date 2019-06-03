package com.example.wikunamu.configuration;

public class JWTParameter {


    public static final String URL = "/login";
    public static final String AUTH_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer";
    public static final int ACCESS_TOKEN_EXPIRATION = 24 * 60 * 60; // 1day
    public static final String JWT_SECRET = "dsflkakw0893209rpoiefjslkdljrsf0980epqdsa";

    public static final int REFRESH_TOKEN_EXPIRATION = 30 * 24 * 60 * 60; // 30 days


}
