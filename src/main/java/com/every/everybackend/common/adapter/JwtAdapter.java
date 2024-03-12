package com.every.everybackend.common.adapter;

public interface JwtAdapter {

    public String createToken(String email);

    public String getEmail(String token);
}
