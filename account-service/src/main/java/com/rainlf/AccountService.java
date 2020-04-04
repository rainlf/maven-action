package com.rainlf;

public interface AccountService {

    String generateCaptchaKey() throws AccountCaptchaException;

    byte[] generateCaptchaImage(String captchaKey) throws AccountCaptchaException;

    void signUp(SignUpRequest signUpRequest) throws AccountCaptchaException;

    void activate(String activationNumber) throws AccountCaptchaException;

    void login(String id, String password) throws AccountCaptchaException;
}
