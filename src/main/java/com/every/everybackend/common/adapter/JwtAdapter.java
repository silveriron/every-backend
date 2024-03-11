package com.every.everybackend.common.adapter;

import org.springframework.stereotype.Component;

@Component
public interface JwtAdapter {

    public String createToken(String email);

    public String getEmail(String token);
}
