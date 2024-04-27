package com.sdut.supermarket.controller;

import com.sdut.supermarket.pojo.Supplier;
import com.sdut.supermarket.pojo.query.SupplierQuery;
import com.sdut.supermarket.service.ISupplierService;
import com.sdut.supermarket.service.impl.SupplierServiceImpl;
import com.sdut.supermarket.utils.JSONResult;
import com.sdut.supermarket.utils.JSONUtil;
import com.sdut.supermarket.utils.LayUITableResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/supply")
public class SupplierServlet extends HttpServlet {
    private ISupplierService supplyService = new SupplierServiceImpl();
    //getSupplierUpdatePage&id=11

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
            case "getSupplierUpdatePage":
                getEmpUpdatePage(req, resp);
                break;
            case "update":
                update(req, resp);
                break;
            case "getSupplierAddPage":
                getEmpAddPage(req, resp);
                break;
        }
    }

    //信息添加界面
    private void getEmpAddPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("skip to the supplier_add.jsp");
        req.getRequestDispatcher("/supplier/supplier_add.jsp").forward(req, resp);
    }

    //信息更新界面
    private void update(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("update");

        Supplier supplier = new Supplier(
                Integer.parseInt(req.getParameter("id")),
                req.getParameter("name"),
                req.getParameter("email"),
                req.getParameter("phone")
        );

        boolean isSuccess = supplyService.update(supplier);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("修改成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("修改失败"), resp);
        }
    }


    //信息编辑界面
    private void getEmpUpdatePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Supplier supplier = supplyService.selectById(Integer.parseInt(id));
        req.setAttribute("emp", supplier);
        req.getRequestDispatcher("/supplier/supplier_update.jsp").forward(req, resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("EmpServlet.add");
        Supplier supplier = new Supplier();
        supplier.setName(req.getParameter("name"));
        supplier.setEmail(req.getParameter("email"));
        supplier.setPhone(req.getParameter("phone"));

        boolean isSuccess = supplyService.add(supplier);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("添加成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("添加失败"), resp);
        }
    }

    private void deleteAll(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("deleteAll");
        String ids = req.getParameter("ids");
        String[] array = ids.split(",");
        boolean isSuccess = supplyService.deleteAll(array);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("批量删除成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("批量删除失败"), resp);
        }
    }

    private void deleteById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("deleteById");
        boolean isSuccess = supplyService.deleteById(Integer.parseInt(req.getParameter("id")));
        //删除完了之后应该重定向到查找所有界面  /hotel/emp?method=selectAll
        // resp.sendRedirect(req.getContextPath() + "/emp?method=selectAll");
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("删除成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("删除失败"), resp);
        }
    }

    private void selectByPage(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Supply.selectByPage");
        SupplierQuery supplierQuery = new SupplierQuery(
                Integer.parseInt(req.getParameter("page")),
                Integer.parseInt(req.getParameter("limit")),
                req.getParameter("name"),
                req.getParameter("email"),
                req.getParameter("phone")
        );

        LayUITableResult layUITableResult = supplyService.selectByPage(supplierQuery);
        JSONUtil.obj2Json(layUITableResult, resp);
    }
}
