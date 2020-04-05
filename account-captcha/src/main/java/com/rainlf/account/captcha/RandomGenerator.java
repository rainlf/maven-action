package com.rainlf.account.captcha;

import java.util.Random;

public class RandomGenerator {
    private static String RANGE = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static String getRandomString() {
        Random random = new Random();

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            result.append(RANGE.charAt(random.nextInt(RANGE.length())));
        }
        return result.toString();
    }
}
