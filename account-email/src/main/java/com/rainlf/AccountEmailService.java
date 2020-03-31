package com.rainlf;

public interface AccountEmailService {
    void sendMail(String to, String subject, String htmlText);
}
