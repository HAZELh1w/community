package com.example.community;

import com.example.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hazel
 * @description 功能描述
 * @create 2023/4/30 13:48
 */
public class TransactionTests {
    @Autowired
    private AlphaService alphaService;

    @Test
    public void testSave1(){
        Object obj = alphaService.save1();
        System.out.println(obj);
    }

    @Test
    public void testSave2(){
        Object obj = alphaService.save2();
        System.out.println(obj);
    }
}
