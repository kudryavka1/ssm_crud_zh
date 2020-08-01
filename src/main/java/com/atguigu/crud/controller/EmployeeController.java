package com.atguigu.crud.controller;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理员工crud请求
 */
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 删除 单个 批量二合一
     *
     * 单个删除:1
     * 批量删除:1-2-3
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{ids}" ,method = RequestMethod.DELETE)
    public Msg deleteEmp(@PathVariable("ids") String ids){
        //批量删除
        if(ids.contains("-")){
            String[] str_ids = ids.split("-");
            List<Integer> del_ids = new ArrayList<>();
            for (String id : str_ids) {
                del_ids.add(Integer.parseInt(id));
            }

            employeeService.deleteBatch(del_ids);
        }
        //单个删除
        else {
            int id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);
        }

        return Msg.success();
    }


    /**
     * 员工更新方法
     * @param employee
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{empId}",method = RequestMethod.PUT)
    public Msg saveEmp(Employee employee){

        employeeService.updateEmp(employee);
        return Msg.success();
    }


    /**
     * 根据员工id查询一个员工
     * @param id
     * @return
     */

    @RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id){
        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp",employee);
    }


    @ResponseBody
    @RequestMapping("/checkuser")
    public Msg checkuser(@RequestParam("empName") String empName){
        //先判断用户名是否是合法的表达式
        String regx="([0-9a-zA-Z\\u4E00-\\u9FA5]+)";
        if (!empName.matches(regx)) {
            return Msg.fail().add("va_msg","用户名必须是4-10个字符");
        }

        //数据库用户名重复校验
        boolean b = employeeService.checkUser(empName);
        if (b){
            return Msg.success();
        }else {
            return Msg.fail().add("va_msg","用户名不可用");
        }
    }


    /**
     * 员工保存
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp",method = RequestMethod.POST)
    public Msg saveEmp(@Valid Employee employee, BindingResult result){
        if(result.hasErrors()){
            //校验失败
            Map<String,Object> map = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError fieldError : errors) {
                System.out.println("错误的字段名："+fieldError.getField());
                System.out.println("错误信息："+fieldError.getDefaultMessage());

                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return Msg.fail().add("errorFields",errors);
        }else {
            employeeService.saveEmp(employee);
            return Msg.success();
        }

    }

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
