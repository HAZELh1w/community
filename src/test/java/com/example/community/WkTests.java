package com.example.community;

import java.io.IOException;

/**
 * @author hazel
 * @description 功能描述
 * @create 2023/5/18 15:25
 */
public class WkTests {
    public static void main(String[] args) {
        String cmd = "d:/dev/wkhtmltopdf/bin/wkhtmltoimage --quality 75 https://wkhtmltopdf.org/downloads.html D:\\work\\data\\wk-images\\1.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
