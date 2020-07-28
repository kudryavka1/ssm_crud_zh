package com.atguigu.crud.controller;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 处理员工crud请求
 */
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 查询员工数据 分页查询
     * @param pn:页码
     * @return
     */
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn",defaultValue = "1") Integer pn){
        PageHelper.startPage(pn,5);
        //startPage后的查询就是分页查询
        List<Employee> emps = employeeService.getAll();

        //使用pageinfo包装查询后的结果
        //封装了详细的分页信息
        PageInfo pageInfo = new PageInfo(emps,5);//连续显示5页
        return Msg.success().add("pageInfo",pageInfo);

    }


    //@RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn",defaultValue = "1") Integer pn, Model model){
        //引入PageHelper插件
        //在查询之前调用
        PageHelper.startPage(pn,5);
        //startPage后的查询就是分页查询
        List<Employee> emps = employeeService.getAll();

        //使用pageinfo包装查询后的结果
        //封装了详细的分页信息
        PageInfo pageInfo = new PageInfo(emps,5);//连续显示5页
        model.addAttribute("pageInfo",pageInfo);


        return "list";
    }
}
