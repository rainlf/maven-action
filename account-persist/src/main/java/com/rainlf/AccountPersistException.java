package com.rainlf;

/**
 * @author : rain
 * @date : 2020/4/3 13:18
 */
public class AccountPersistException extends RuntimeException {
    public AccountPersistException(String message, Throwable cause) {
        super(message, cause);
    }
}
