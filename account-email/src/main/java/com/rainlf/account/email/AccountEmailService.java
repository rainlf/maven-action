package com.rainlf.account.email;

public interface AccountEmailService {
    void sendMail(String to, String subject, String htmlText);
}
