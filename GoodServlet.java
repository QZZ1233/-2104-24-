package com.sdut.supermarket.controller;


import com.sdut.supermarket.pojo.Good;
import com.sdut.supermarket.pojo.Supplier;
import com.sdut.supermarket.pojo.query.GoodQuery;
import com.sdut.supermarket.service.IGoodService;
import com.sdut.supermarket.service.ISupplierService;
import com.sdut.supermarket.service.impl.GoodServiceImpl;
import com.sdut.supermarket.service.impl.SupplierServiceImpl;
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
import java.util.Date;
import java.util.List;

@WebServlet("/good")
public class GoodServlet extends HttpServlet {
    private ISupplierService supplierService = new SupplierServiceImpl();
    private IGoodService goodService = new GoodServiceImpl();

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
            case "getGoodUpdatePage":
                getEmpUpdatePage(req, resp);
                break;
            case "update":
                update(req, resp);
                break;
            case "getGoodAddPage":
                getEmpAddPage(req, resp);
                break;
        }
    }

    //查询可以选择的供应商部门
    private void getEmpAddPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("getEmpAddPage");
        List<Supplier> list = supplierService.selectAll();
        req.setAttribute("list", list);
        req.getRequestDispatcher("/good/good_add.jsp").forward(req, resp);
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) {
        Good good = new Good();
        good.setId(Integer.parseInt(req.getParameter("id")));
        good.setName(req.getParameter("name"));
        good.setNum(Integer.parseInt(req.getParameter("num")));
        good.setPrice(Float.parseFloat(req.getParameter("price")));
        good.setQgp(DateUtil.parse(req.getParameter("qgp")));
        good.setSid(Integer.parseInt(req.getParameter("sid")));
        boolean isSuccess = goodService.update(good);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("修改成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("修改失败"), resp);
        }
    }


    private void getEmpUpdatePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("getEmpUpdatePage");
        Good good = goodService.selectById(Integer.parseInt(req.getParameter("id")));
        req.setAttribute("emp", good);
        req.getRequestDispatcher("/good/good_update.jsp").forward(req, resp);
    }

    //添加功能
    private void add(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("add");
        String name = req.getParameter("name");
        Integer num = Integer.parseInt(req.getParameter("num"));
        float price = 0F;
        if (!StringUtils.isEmpty(req.getParameter("price"))) {
            price = Float.parseFloat(req.getParameter("price"));
        }
        Date qgp = DateUtil.parse(req.getParameter("qgp"));
        Integer sid = Integer.parseInt(req.getParameter("sid"));
        Good good = new Good();
        good.setName(name);
        good.setNum(num);
        good.setPrice(price);
        good.setQgp(qgp);
        good.setSid(sid);
        boolean isSuccess = goodService.add(good);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("添加成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("添加失败"), resp);
        }
    }

    //删除全部
    private void deleteAll(HttpServletRequest req, HttpServletResponse resp) {
        String ids = req.getParameter("ids");
        String[] array = ids.split(",");
        boolean isSuccess = goodService.deleteAll(array);
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("批量删除成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("批量删除失败"), resp);
        }
    }

    //id删除
    private void deleteById(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        boolean isSuccess = goodService.deleteById(Integer.parseInt(id));
        if (isSuccess) {
            JSONUtil.obj2Json(JSONResult.ok("删除成功"), resp);
        } else {
            JSONUtil.obj2Json(JSONResult.error("删除失败"), resp);
        }
    }

    private void selectByPage(HttpServletRequest req, HttpServletResponse resp) {
        GoodQuery goodQuery = new GoodQuery(
                Integer.parseInt(req.getParameter("page")),
                Integer.parseInt(req.getParameter("limit")),
                req.getParameter("name"),
                req.getParameter("supplier")
        );
        LayUITableResult layUITableResult = goodService.selectByPage(goodQuery);
        JSONUtil.obj2Json(layUITableResult, resp);
    }
}
