package com.example.community.util;

import com.example.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author hazel
 * @description 持有用户的信息，用于代替session对象
 * @create 2023/4/27 11:19
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
