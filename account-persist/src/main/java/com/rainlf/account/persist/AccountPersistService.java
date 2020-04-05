package com.rainlf.account.persist;

/**
 * @author : rain
 * @date : 2020/4/3 13:18
 */
public interface AccountPersistService {

    Account createAccount(Account account) throws AccountPersistException;

    Account readAccount(String id) throws AccountPersistException;

    Account updateAccount(Account account) throws AccountPersistException;

    void deleteAccount(String id) throws AccountPersistException;
}
