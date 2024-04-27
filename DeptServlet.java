package com.sdut.supermarket.controller;

import com.sdut.supermarket.pojo.Dept;
import com.sdut.supermarket.pojo.query.DeptQuery;
import com.sdut.supermarket.service.IDeptService;
import com.sdut.supermarket.service.impl.DeptServiceImpl;
import com.sdut.supermarket.utils.JSONResult;
import com.sdut.supermarket.utils.JSONUtil;
import com.sdut.supermarket.utils.LayUITableResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/dept")
public class DeptServlet extends HttpServlet {

    private IDeptService deptService = new DeptServiceImpl();

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
            case "getDeptUpdatePage":
                getDeptUpdatePage(req, resp);
                break;
            case "update":
                update(req, resp);
                break;
            case "selectDeptCount":
                selectDeptCount(req, resp);
                break;
        }
    }

    private void selectDeptCount(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("DeptServlet.selectDeptCount");
        JSONResult jsonResult = deptService.selectDeptCount();
        JSONUtil.obj2Json(jsonResult, resp);
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) {
        Dept dept = new Dept();
        dept.setId(Integer.parseInt(req.getParameter("id")));
        dept.setName(req.getParameter("name"));
        dept.setAddr(req.getParameter("addr"));

        boolean isSuccess = deptService.update(dept);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("修改成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("修改失败"), resp);
        }
    }


    private void getDeptUpdatePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Dept dept = deptService.selectById(Integer.parseInt(id));
        req.setAttribute("dept", dept);
        req.getRequestDispatcher("/dept/dept_update.jsp").forward(req, resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) {
        Dept dept = new Dept();
        dept.setName(req.getParameter("name"));
        dept.setAddr(req.getParameter("addr"));

        boolean isSuccess = deptService.add(dept);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("添加成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("添加失败"), resp);
        }
    }

    private void deleteAll(HttpServletRequest req, HttpServletResponse resp) {
        String ids = req.getParameter("ids");
        String[] array = ids.split(",");
        boolean isSuccess = deptService.deleteAll(array);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("批量删除成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("批量删除失败"), resp);
        }
    }

    private void selectByPage(HttpServletRequest req, HttpServletResponse resp) {
        DeptQuery deptQuery = new DeptQuery(Integer.parseInt(req.getParameter("page")),
                Integer.parseInt(req.getParameter("limit")),
                req.getParameter("name"),
                req.getParameter("addr"));

        LayUITableResult layUITableResult = deptService.selectByPage(deptQuery);
        JSONUtil.obj2Json(layUITableResult, resp);
    }

    private void deleteById(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        boolean isSuccess = deptService.deleteById(Integer.parseInt(id));
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("删除成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("删除失败"), resp);
        }
    }

    private void selectAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DeptServlet.selectAll");
        List<Dept> list = deptService.selectAll();
        //把数据放到req这个域对象
        req.setAttribute("list", list);
        //转发到dept_list.jsp页面进行展示
        req.getRequestDispatcher("/dept/dept_list.jsp").forward(req, resp);
    }

}
