package com.example.community.controller;

import com.example.community.entity.User;
import com.example.community.service.UserService;
import com.example.community.util.CommunityConstant;
import com.example.community.util.CommunityUtil;
import com.example.community.util.MailClient;
import com.example.community.util.RedisKeyUtil;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hazel
 * @description 功能描述
 * @create 2023/4/22 22:28
 */
@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }

    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String,Object> map = userService.register(user);
        if (map == null || map.isEmpty()){
            model.addAttribute("msg","注册成功,我们已经向您的邮箱发送了一封激活邮件，请尽快激活！");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }

    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId")int userId,@PathVariable("code") String code){
        int result = userService.activation(userId,code);
        if (result == ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功，您的账号已经可以正常使用了！");
            model.addAttribute("target","/login");
        }else if (result == ACTIVATION_REPEAT){
            model.addAttribute("msg","无效操作，该账号已经激活过了！");
            model.addAttribute("target","/index");
        }else {
            model.addAttribute("msg","激活失败，您提供的激活码不正确！");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response){
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存入session
//        session.setAttribute("kaptcha",text);

        //验证码的归属
        String kaptchaOwner = CommunityUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner",kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);
        //将验证码存入redis
        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        redisTemplate.opsForValue().set(redisKey,text,60, TimeUnit.SECONDS);

        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("响应验证码失败"+e.getMessage());
        }
    }

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(String username,String password,String code,boolean rememberme,Model model,HttpServletResponse response,@CookieValue("kaptchaOwner")String kaptchaOwner){
        //检查验证码
//        String kaptcha = (String) session.getAttribute("kaptcha");
        String kaptcha = null;
        if (StringUtils.isNotBlank(kaptchaOwner)){
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
        }
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确!");
            return "/site/login";
        }

        //检查账号,密码
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }

    // 忘记密码页面
    @RequestMapping(path = "/forget", method = RequestMethod.GET)
    public String getForgetPage() {
        return "/site/forget";
    }

    // 获取验证码
    @RequestMapping(path = "/forget/code", method = RequestMethod.GET)
    @ResponseBody
    public void getForgetCode(String email, HttpSession session) {
        if (StringUtils.isBlank(email)) {
            return;
        }

        // 发送邮件
        Context context = new Context();
        context.setVariable("email", email);
        String code = CommunityUtil.generateUUID().substring(0, 4);
        context.setVariable("verifyCode", code);
        String content = templateEngine.process("/mail/forget", context);
        mailClient.sendMail(email, "找回密码", content);

        // 保存验证码
        session.setAttribute("verifyCode", code);
    }

    // 重置密码
    @RequestMapping(path = "/forget/password", method = RequestMethod.POST)
    public String resetPassword(String email, String verifyCode, String password, Model model, HttpSession session) {
        String code = (String) session.getAttribute("verifyCode");
        if (StringUtils.isBlank(verifyCode) || StringUtils.isBlank(code) || !code.equalsIgnoreCase(verifyCode)) {
            model.addAttribute("codeMsg", "验证码错误!");
            return "/site/forget";
        }

        Map<String, Object> map = userService.resetPassword(email, password);
        if (map.containsKey("user")) {
            return "redirect:/login";
        } else {
            model.addAttribute("emailMsg", map.get("emailMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/forget";
        }
    }
}
