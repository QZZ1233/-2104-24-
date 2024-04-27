package com.sdut.supermarket.controller;


import com.sdut.supermarket.pojo.User;
import com.sdut.supermarket.pojo.query.UserQuery;
import com.sdut.supermarket.service.IUserService;
import com.sdut.supermarket.service.impl.UserServiceImpl;
import com.sdut.supermarket.utils.DateUtil;
import com.sdut.supermarket.utils.JSONResult;
import com.sdut.supermarket.utils.JSONUtil;
import com.sdut.supermarket.utils.LayUITableResult;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        switch (method) {
            case "selectAll":
                selectAll(req, resp);
                break;

            case "selectByPage":
                selectByPage(req, resp);
                break;
            case "deleteById":
                deleteById(req, resp);
                break;
            case "deleteAll":
                deleteAll(req, resp);
                break;
            case "add":
                add(req, resp);
                break;
            case "getUserUpdatePage":
                getUserUpdatePage(req, resp);
                break;
            case "update":
                update(req, resp);
                break;
            case "updateStatus":
                updateStatus(req, resp);
                break;
        }
    }

    private void updateStatus(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("UserServlet.updateStatus");

        boolean isSuccess = userService.updateStatus(
                req.getParameter("id"),
                Integer.parseInt(req.getParameter("status")));

        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("修改状态成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("修改状态失败"), resp);
        }
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("UserServlet.update");

        User user = new User();
        user.setId(Integer.parseInt(req.getParameter("id")));
        user.setName(req.getParameter("name"));
        user.setPassword(req.getParameter("password"));
        user.setEmail(req.getParameter("email"));
        user.setPhone(req.getParameter("phone"));
        user.setAvatar("");

        boolean isSuccess = userService.update(user);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("修改成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("修改失败"), resp);
        }
    }


    private void getUserUpdatePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("UserServlet.getUserUpdatePage");
        String id = req.getParameter("id");
        User user = userService.selectById(Integer.parseInt(id));
        req.setAttribute("user", user);
        req.getRequestDispatcher("/user/user_update.jsp").forward(req, resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("UserServlet.add");
        User user = new User();
        user.setName(req.getParameter("name"));
        user.setPassword(req.getParameter("password"));
        user.setEmail(req.getParameter("email"));
        user.setPhone(req.getParameter("phone"));
        user.setAvatar("");
        user.setType(Integer.parseInt(req.getParameter("type")));

        boolean isSuccess = userService.add(user);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("添加成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("添加失败"), resp);
        }
    }

    private void deleteAll(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("UserServlet.deleteAll");
        //"14,15"
        String ids = req.getParameter("ids");
        String[] array = ids.split(",");
        boolean isSuccess = userService.deleteAll(array);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("批量删除成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("批量删除失败"), resp);
        }
    }

    private void selectByPage(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("UserServlet.selectByPage");
        int page = Integer.parseInt(req.getParameter("page"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String typeStr = req.getParameter("type");
        Integer type = null;
        //判断是否为空
        if (!StringUtils.isEmpty(typeStr)) {
            type = Integer.parseInt(typeStr);
        }
        String beginDate = req.getParameter("beginDate");
        String endDate = req.getParameter("endDate");
        UserQuery userQuery = new UserQuery(page, limit, name, email,
                phone, type, DateUtil.parse(beginDate),
                DateUtil.parse(endDate));

        LayUITableResult layUITableResult = userService.selectByPage(userQuery);
        JSONUtil.obj2Json(layUITableResult, resp);
    }

    private void deleteById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("UserServlet.deleteById");
        String id = req.getParameter("id");
        boolean isSuccess = userService.deleteById(Integer.parseInt(id));
        //删除完了之后应该重定向到查找所有界面  /hotel/user?method=selectAll
        // resp.sendRedirect(req.getContextPath() + "/user?method=selectAll");
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("删除成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("删除失败"), resp);
        }
    }

    private void selectAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("UserServlet.selectAll");
        List<User> list = userService.selectAll();
        //把数据放到req这个域对象
        req.setAttribute("list", list);
        //转发到user_list.jsp页面进行展示
        req.getRequestDispatcher("/user/user_list.jsp").forward(req, resp);
    }
}