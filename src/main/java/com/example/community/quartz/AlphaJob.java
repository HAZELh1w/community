package com.example.community.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author hazel
 * @description 功能描述
 * @create 2023/5/17 20:19
 */
public class AlphaJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(Thread.currentThread().getName() + " :execute a quartz job");
    }
}
