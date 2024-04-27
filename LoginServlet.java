package com.sdut.supermarket.controller;


import com.sdut.supermarket.pojo.User;
import com.sdut.supermarket.service.IUserService;
import com.sdut.supermarket.service.impl.UserServiceImpl;
import com.sdut.supermarket.utils.JSONResult;
import com.sdut.supermarket.utils.JSONUtil;
import org.springframework.util.StringUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String method = req.getParameter("method");
        switch (method) {
            //跳转到登录方法
            case "login":
                login(req, resp);
                break;
            //跳转到登出方法
            case "logout":
                logout(req, resp);


                break;
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("name");
        System.out.println("username=>" + username);
        String password = req.getParameter("password");
        System.out.println("password=>" + password);
        String code = req.getParameter("code");
        System.out.println("verifyCode=>" + password);

        HttpSession session = req.getSession();
        String verifyCode = (String) session.getAttribute("codeInSession");
        System.out.println("verifyCode=>" + verifyCode);
        if (StringUtils.isEmpty(code) || !verifyCode.equalsIgnoreCase(code)) {
            JSONUtil.obj2Json(JSONResult.error("验证码为空或错误"), resp);
            return;
        }

        User user = userService.login(username, password);
        if (user != null) {
            //虽然查出这个用户，但是要判断这个用户是不是可用status==1
            if (user.getStatus() != 1) {
                JSONUtil.obj2Json(JSONResult.error("该用户已经被禁用"), resp);
                return;
            }
            //登录上之后，把这个凭证保存到Session里面
            session.setAttribute("user", user);
            JSONUtil.obj2Json(JSONResult.ok("登录成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("用户名或密码错误"), resp);
        }
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("LoginServlet.logout");
        //退出原理：删除在Session中的凭证；
        HttpSession session = req.getSession();
        session.invalidate();

        //然后重定向到登录界面
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}
