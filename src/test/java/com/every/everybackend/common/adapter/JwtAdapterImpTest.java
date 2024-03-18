package com.every.everybackend.common.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
class JwtAdapterImpTest {

    private final JwtAdapterImp jwtAdapterImp = new JwtAdapterImp("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest", 30);


    @DisplayName("토큰을 생성한다")
    @Test
    void createToken() {
        // given
        String email = "test@test.com";
        // when
        String token = jwtAdapterImp.createToken(email);
        // then
        assertNotNull(token);
    }

    @DisplayName("토큰을 파싱하여 이메일을 반환한다")
    @Test
    void getEmail() {
        // given
        String email = "test@test.com";
        String token = jwtAdapterImp.createToken(email);

        // when
        String result = jwtAdapterImp.getEmail(token);

        // then
        assertEquals(email, result);
    }

}