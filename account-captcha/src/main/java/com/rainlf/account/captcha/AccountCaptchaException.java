package com.rainlf.account.captcha;

public class AccountCaptchaException extends RuntimeException {

    public AccountCaptchaException(String message) {
        super(message);
    }

    public AccountCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }
}
