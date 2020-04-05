package com.rainlf.account.service;

import com.rainlf.account.captcha.AccountCaptchaException;
import com.rainlf.account.captcha.AccountCaptchaService;
import com.rainlf.account.captcha.RandomGenerator;
import com.rainlf.account.email.AccountEmailService;
import com.rainlf.account.persist.Account;
import com.rainlf.account.persist.AccountPersistException;
import com.rainlf.account.persist.AccountPersistService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class AccountServiceImpl implements AccountService {

    private AccountEmailService accountEmailService;

    private AccountPersistService accountPersistService;

    private AccountCaptchaService accountCaptchaService;

    private Map<String, String> activationMap = new HashMap<>();

    @Override
    public String generateCaptchaKey() throws AccountCaptchaException {
        try {
            return accountCaptchaService.generateCaptchaKey();
        } catch (AccountCaptchaException e) {
            throw new AccountServiceException("Unable to generate captcha key", e);
        }
    }

    @Override
    public byte[] generateCaptchaImage(String captchaKey) throws AccountCaptchaException {
        try {
            return accountCaptchaService.generateCaptchaImage(captchaKey);
        } catch (AccountCaptchaException e) {
            throw new AccountServiceException("Unable to generate captcha image", e);
        }
    }

    @Override
    public void signUp(SignUpRequest signUpRequest) throws AccountCaptchaException {
        try {
            if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
                throw new AccountServiceException("2 password do not match");
            }

            if (!accountCaptchaService.validateCaptcha(signUpRequest.getCaptchaKey(), signUpRequest.getCaptchaValue())) {
                throw new AccountServiceException("Incorrect captcha");
            }

            Account account = new Account();
            account.setId(signUpRequest.getId());
            account.setEmail(signUpRequest.getEmail());
            account.setName(signUpRequest.getName());
            account.setPassword(signUpRequest.getPassword());
            account.setActivated(false);

            accountPersistService.createAccount(account);

            String activationId = RandomGenerator.getRandomString();
            activationMap.put(activationId, account.getId());

            String link = signUpRequest.getActivateServiceUrl().endsWith("/")
                    ? signUpRequest.getActivateServiceUrl() + activationId
                    : signUpRequest.getActivateServiceUrl() + "?key=" + activationId;

            accountEmailService.sendMail(account.getEmail(), "Please activate your account", link);
        } catch (Exception e) {
            throw new AccountServiceException("Unable to sing up account", e);
        }
    }

    @Override
    public void activate(String activationNumber) throws AccountCaptchaException {
        String accountId = activationMap.get(activationNumber);
        if (accountId == null) {
            throw new AccountServiceException("Invalid account activation id");
        }

        try {
            Account account = accountPersistService.readAccount(accountId);
            account.setActivated(true);
            accountPersistService.updateAccount(account);
        } catch (AccountPersistException e) {
            throw new AccountServiceException("Unable to activate account");
        }
    }

    @Override
    public void login(String id, String password) throws AccountCaptchaException {
        try {
            Account account = accountPersistService.readAccount(id);
            if (account == null) {
                throw new AccountServiceException("Account does not exist");
            }
            if (!account.isActivated()) {
                throw new AccountServiceException("Account is disable");
            }
            if (!account.getPassword().equals(password)) {
                throw new AccountServiceException("Incorrect password");
            }
        } catch (AccountPersistException e) {
            throw new AccountServiceException("Unable to login", e);
        }
    }
}
