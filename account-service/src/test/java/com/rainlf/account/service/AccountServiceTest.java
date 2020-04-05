package com.rainlf.account.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccountServiceTest {

    private AccountService accountService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("account-service.xml");
        accountService = (AccountService) ctx.getBean("accountService");
    }

    @Test
    public void generateCaptchaKey() {
        Set<String> keys = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String key = accountService.generateCaptchaKey();
            assertFalse(keys.contains(key));
            keys.add(key);
        }
    }

    @Test
    public void generateCaptchaImage() {
        String key = accountService.generateCaptchaKey();
        byte[] image = accountService.generateCaptchaImage(key);
        assertTrue(image.length > 0);
    }
}