package com.example.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author hazel
 * @description 功能描述
 * @create 2023/5/17 18:52
 */
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
