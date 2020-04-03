package com.rainlf;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author : rain
 * @date : 2020/4/3 14:45
 */
public class AccountPersistServiceTest {

    private AccountPersistService accountPersistService;

    @Before
    public void setUp() throws Exception {
        File persistDataFile = new File("target/test-classes/persist-data.xml");
        if (persistDataFile.exists()) {
            persistDataFile.delete();
        }

        ApplicationContext ctx = new ClassPathXmlApplicationContext("account-persist.xml");
        accountPersistService = (AccountPersistService) ctx.getBean("accountPersistService");

        Account account = new Account();
        account.setId("001");
        account.setName("cc");
        account.setEmail("cc@qq.com");
        account.setPassword("12345678");
        account.setActivated(true);

        accountPersistService.createAccount(account);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void readAccount() {
        Account account = accountPersistService.readAccount("001");
        assertNotNull(account);
        assertEquals("cc", account.getName());
        assertEquals("cc@qq.com", account.getEmail());
        assertEquals("12345678", account.getPassword());
        assertTrue(account.isActivated());
    }

    @Test
    public void updateAccount() {
        Account account = new Account();
        account.setId("001");
        account.setName("xin");
        account.setEmail("xin@qq.com");
        account.setPassword("87654321");
        account.setActivated(false);
        accountPersistService.updateAccount(account);

        Account xin = accountPersistService.readAccount("001");
        assertEquals("xin", xin.getName());
        assertEquals("xin@qq.com", xin.getEmail());
        assertEquals("87654321", xin.getPassword());
        assertFalse(xin.isActivated());
    }

    @Test
    public void deleteAccount() {
        accountPersistService.deleteAccount("001");
        Account account = accountPersistService.readAccount("001");
        assertNull(account);
    }
}