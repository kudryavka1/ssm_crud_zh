package com.atguigu.crud.test;


import com.atguigu.crud.bean.Department;
import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.dao.DepartmentMapper;
import com.atguigu.crud.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

/***
 * 测试dao层
 * 1 使用spring的单元测试
 * 2 @ContextConfiguration指定Spring配置文件的位置
 * 3 直接Autowired要使用的组件
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})

public class MapperTest {
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    SqlSession sqlSession;
    /**
     * 测试DepartmentMapper
     */
    @Test
    public void testCRUD(){
     /*   //1.创建SpringIOC容器
        ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
        //2.从容器中获取mapper
        DepartmentMapper bean = ioc.getBean(DepartmentMapper.class);
        //使用spring的单元测试*/
        System.out.println(departmentMapper);
        //插入几个部门
       /* departmentMapper.insertSelective(new Department(null,"开发部"));
        departmentMapper.insertSelective(new Department(null,"测试部"));

        //生成员工数据
        employeeMapper.insertSelective(new Employee(null,"Jerry","M","jerry@qq.com",1));*/

        //批量插入多个员工 使用可以执行批量操作的sqlSession
//        for(){
//            employeeMapper.insertSelective(new Employee(null,"Jerry","M","jerry@qq.com",1));
//        }
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        for(int i = 0;i<1000;i++){
            String uid = UUID.randomUUID().toString().substring(0, 5)+i;

            mapper.insertSelective(new Employee(null,uid,"M",uid+"@qq.com",1));
        }
    }
}
