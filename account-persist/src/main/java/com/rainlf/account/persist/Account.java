package com.rainlf.account.persist;

import lombok.Data;

/**
 * @author : rain
 * @date : 2020/4/3 13:12
 */
@Data
public class Account {

    private String id;
    private String name;
    private String email;
    private String password;
    private boolean activated;
}
