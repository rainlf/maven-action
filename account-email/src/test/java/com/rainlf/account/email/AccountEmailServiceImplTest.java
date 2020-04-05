package com.rainlf.account.email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.mail.MessagingException;

public class AccountEmailServiceImplTest {

    private GreenMail greenMail;

    @Before
    public void setUp() throws Exception {
        greenMail = new GreenMail(ServerSetup.SMTP);
        greenMail.setUser("test@gmail.com", "123456");
//        greenMail.start();
    }

    @After
    public void tearDown() throws Exception {
//        greenMail.stop();
    }

    @Test
    public void sendMail() throws MessagingException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("account-email.xml");
        AccountEmailService accountEmailService = (AccountEmailService) ctx.getBean("accountEmailService");

        String subject = "Test Subject";
        String htmlText = "<h3>Test</h3>";
        accountEmailService.sendMail("test2@gmail.com", subject, htmlText);

//        greenMail.waitForIncomingEmail(2000, 1);
//
//        Message[] messages = greenMail.getReceivedMessages();
//        assertEquals(1, messages.length);
//        assertEquals(subject, messages[0].getSubject());
//        assertEquals(htmlText, GreenMailUtil.getBody(messages[0]).trim());
    }
}