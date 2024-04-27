package com.sdut.supermarket.controller;

import com.sdut.supermarket.pojo.Dept;
import com.sdut.supermarket.pojo.Emp;
import com.sdut.supermarket.pojo.query.EmpQuery;
import com.sdut.supermarket.service.IDeptService;
import com.sdut.supermarket.service.IEmpService;
import com.sdut.supermarket.service.impl.DeptServiceImpl;
import com.sdut.supermarket.service.impl.EmpServiceImpl;
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

@WebServlet("/emp")
public class EmpServlet extends HttpServlet {
    private IEmpService empService = new EmpServiceImpl();
    private IDeptService deptService = new DeptServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        switch (method) {
            //查询所有员工
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
            case "getEmpUpdatePage":
                getEmpUpdatePage(req, resp);
                break;
            case "update":
                update(req, resp);
                break;
            case "getEmpAddPage":
                getEmpAddPage(req, resp);
                break;
        }
    }

    private void getEmpAddPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("EmpServlet.getEmpAddPage");
        List<Dept> list = deptService.selectAll();
        req.setAttribute("list", list);
        req.getRequestDispatcher("/emp/emp_add.jsp").forward(req, resp);
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("EmpServlet.update");
        Emp emp = new Emp();
        emp.setId(Integer.parseInt(req.getParameter("id")));
        emp.setName(req.getParameter("name"));
        emp.setDeptId(Integer.parseInt(req.getParameter("deptId")));
        emp.setEmail(req.getParameter("email"));
        emp.setPhone(req.getParameter("phone"));

        boolean isSuccess = empService.update(emp);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("修改成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("修改失败"), resp);
        }
    }


    private void getEmpUpdatePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("EmpServlet.getEmpUpdatePage");
        Emp emp = empService.selectById(Integer.parseInt(req.getParameter("id")));
        req.setAttribute("emp", emp);
        req.getRequestDispatcher("/emp/emp_update.jsp").forward(req, resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("EmpServlet.add");
        Emp emp = new Emp();
        emp.setName(req.getParameter("name"));
        emp.setDeptId(Integer.parseInt(req.getParameter("deptId")));
        emp.setEmail(req.getParameter("email"));
        emp.setPhone(req.getParameter("phone"));

        boolean isSuccess = empService.add(emp);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("添加成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("添加失败"), resp);
        }
    }

    private void deleteAll(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("EmpServlet.deleteAll");
        //"14,15"
        String ids = req.getParameter("ids");
        String[] array = ids.split(",");
        boolean isSuccess = empService.deleteAll(array);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("批量删除成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("批量删除失败"), resp);
        }
    }

    private void deleteById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("EmpServlet.deleteById");
        String id = req.getParameter("id");
        boolean isSuccess = empService.deleteById(Integer.parseInt(id));
        //删除完了之后应该重定向到查找所有界面  /hotel/emp?method=selectAll
        // resp.sendRedirect(req.getContextPath() + "/emp?method=selectAll");
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("删除成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("删除失败"), resp);
        }
    }

    //http://localhost:8080/sm/emp?method=selectByPage&page=1&limit=10
    private void selectByPage(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("EmpServlet.selectByPage");
        EmpQuery empQuery = new EmpQuery(
                Integer.parseInt(req.getParameter("page")),
                Integer.parseInt(req.getParameter("limit")),
                req.getParameter("name"),
                req.getParameter("email"),
                req.getParameter("phone")
        );

        LayUITableResult layUITableResult = empService.selectByPage(empQuery);
        JSONUtil.obj2Json(layUITableResult, resp);
    }
}
