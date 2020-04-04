package com.rainlf;

import lombok.Data;

@Data
public class SignUpRequest {
    private String id;
    private String email;
    private String name;
    private String password;
    private String confirmPassword;
    private String captchaKey;
    private String captchaValue;
    private String activateServiceUrl;
}
