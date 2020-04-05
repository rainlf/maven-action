package com.rainlf.account.captcha;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AccountCaptchaServiceTest {

    private AccountCaptchaService accountCaptchaService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("account-captcha.xml");
        accountCaptchaService = (AccountCaptchaService) ctx.getBean("accountCaptchaService");
    }

    @Test
    public void testGenerateCaptcha() throws Exception {
        String captchaKey = accountCaptchaService.generateCaptchaKey();
        assertNotNull(captchaKey);

        byte[] captchaImage = accountCaptchaService.generateCaptchaImage(captchaKey);
        assertTrue(captchaImage.length > 0);

        File image = new File("target/" + captchaKey + ".png");
        try (OutputStream output = new FileOutputStream(image)) {
            output.write(captchaImage);
        }
        assertTrue(image.exists() && image.length() > 0);
    }

    @Test
    public void testValidateCaptchaCorrect() throws Exception {
        List<String> preDefinedTexts = new ArrayList<>();
        preDefinedTexts.add("12345");
        preDefinedTexts.add("abcde");
        accountCaptchaService.setPreDefinedTexts(preDefinedTexts);

        String captchaKey = accountCaptchaService.generateCaptchaKey();
        accountCaptchaService.generateCaptchaImage(captchaKey);
        assertTrue(accountCaptchaService.validateCaptcha(captchaKey, "12345"));

        captchaKey = accountCaptchaService.generateCaptchaKey();
        accountCaptchaService.generateCaptchaImage(captchaKey);
        assertTrue(accountCaptchaService.validateCaptcha(captchaKey, "abcde"));
    }

    @Test
    public void testValidateCaptchaIncorrect() throws Exception {
        List<String> preDefinedTexts = new ArrayList<>();
        preDefinedTexts.add("12345");
        accountCaptchaService.setPreDefinedTexts(preDefinedTexts);

        String captchaKey = accountCaptchaService.generateCaptchaKey();
        accountCaptchaService.generateCaptchaImage(captchaKey);
        assertFalse(accountCaptchaService.validateCaptcha(captchaKey, "67890"));
    }
}