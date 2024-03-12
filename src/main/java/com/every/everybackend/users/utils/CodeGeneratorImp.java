package com.every.everybackend.users.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CodeGeneratorImp implements CodeGenerator {

    @Override
    public String generateCode(int length) {
        String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }

        return randomString.toString();
    }
}
