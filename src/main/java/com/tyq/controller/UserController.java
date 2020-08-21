package com.tyq.controller;

import com.tyq.entity.User;
import com.tyq.service.UserService;
import com.tyq.utils.VerifyCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Console;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
//@RestController
@Controller
@CrossOrigin //允许跨域
@RequestMapping("user")
@Slf4j

public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用来处理用户注册方法
     */
    //@RequestMapping(value = "register", method = {RequestMethod.POST})            //JSON转换user对象      前台验证码信息
     @RequestMapping("register")
     @ResponseBody
    public Map<String, Object> register( @RequestBody User user, String code, HttpServletRequest request) {
        log.info("用户信息:[{}]",user.toString());
        log.info("用户输入的验证码信息:[{}]",code);
        Map<String, Object> map = new HashMap<>();
        try {
            String key = (String) request.getServletContext().getAttribute("code");
            if (key.equalsIgnoreCase(code)) {//不区分大小写
                //1.调用业务方法
                userService.register(user);
                map.put("state", true);
                map.put("msg", "提示: 注册成功!");
            } else {
                throw new RuntimeException("验证码出现错误!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", false);
            map.put("msg", "提示:"+e.getMessage());
        }
        return map;
    }

    /**
     * 用来处理用户登录
     */
    @RequestMapping(value = "login",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> login(User user){
        log.info("当前登录用户的信息: [{}]",user.toString());
        //log.info("username:[{}]",username.toString());
        Map<String, Object> map =  new HashMap<>();
        try {
            User userDB = userService.login(user);
            map.put("state",true);
            map.put("msg","登录成功!");
            map.put("user",userDB);//这些信息被打印在console上
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state",false);
            map.put("msg",e.getMessage());
        }
        return map;
    }

    /**
     * 生成验证码图片
     */
    @RequestMapping("getImage")
    @ResponseBody
    //@GetMapping("getImage")
    public String getImageCode(HttpServletRequest request) throws IOException {
        //1.使用工具类生成验证码
        //String code = VerifyCodeUtils.generateVerifyCode(4);
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //2.将验证码放入servletContext作用域
       request.getServletContext().setAttribute("code", code);
        //3.将图片转为base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(120, 30, byteArrayOutputStream, code);
        return "data:image/png;base64," + Base64Utils.encodeToString(byteArrayOutputStream.toByteArray());

    }
}
