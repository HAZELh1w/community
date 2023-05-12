package com.example.community;

import com.example.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author hazel
 * @description 功能描述
 * @create 2023/4/29 14:42
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTests {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter(){
        String text = "这里可以赌博,可以吸毒,可以嫖娼,可以开票,哈哈哈!";
        text = sensitiveFilter.filter(text);
        System.out.println(text);

        text = "这里可以❤赌😀博※,可以⭐吸🌙毒♦,可以♠嫖♣娼❀,可以🐕开🐉票🐂,哈哈哈!";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
