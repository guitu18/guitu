package com.guitu18.module.service;

import com.guitu18.module.entity.User;

/**
 * @author zhangkuan
 * @date 2019/8/21
 */
public class UserService {

    public User getByUserName(String username) {
        if (!"zhangkuan".equals(username)) {
            return null;
        }
        return new User("zhangkuan", "123456");
    }

}
