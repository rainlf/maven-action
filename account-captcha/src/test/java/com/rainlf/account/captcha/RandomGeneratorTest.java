package com.rainlf.account.captcha;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;

public class RandomGeneratorTest {

    @Test
    public void getRandomString() {
        Set<String> randoms = new HashSet<>(100);

        for (int i = 0; i < 100; i++) {
            String random = RandomGenerator.getRandomString();
            assertFalse(randoms.contains(random));
            randoms.add(random);
        }
    }
}