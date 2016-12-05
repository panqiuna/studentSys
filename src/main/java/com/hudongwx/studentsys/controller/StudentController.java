package com.hudongwx.studentsys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hudongwx.studentsys.common.BaseController;
import com.hudongwx.studentsys.exceptions.ServiceException;
import com.hudongwx.studentsys.model.Mapping;
import com.hudongwx.studentsys.model.Student;
import com.hudongwx.studentsys.service.ClassService;
import com.hudongwx.studentsys.service.StudentService;
import com.hudongwx.studentsys.service.UserService;
import com.hudongwx.studentsys.util.Common;
import com.hudongwx.studentsys.util.RenderKit;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhongxu on 2016/9/19 0019.
 */
public class StudentController extends BaseController {
    public StudentService studentService;
    public UserService userService;
    public ClassService classService;

    @Override
    public void index() {

        if ("pageJump".equals(getPara(0))) {
            pageJump();
            return;
        }
        super.index();
//        List<Student> students = studentService.getAllStudent();
//        setAttr("students", students);
//        List<User> defaultTeacher = userService.getUsersByRole(roleService.getRoleByName(Common.getMainProp().get("defaultTeacher")));
//        setAttr("users", defaultTeacher);
//        List<Class> allClass = classService.getAllClass();
//        setAttr("classes", allClass);
    }

    private void pageJump() {
        setMapping(mappingService.getMappingByUrl("/classManager"));
        fillHeaderAndFooter();
        fillContentParent();
        Integer p = getParaToInt("p",1);
        Integer classId = getParaToInt("classId");
        Page<Student> studentPageList = studentService.getStudentPageByClassId(classId, p);
        System.out.println("studentPageListSize="+studentPageList.getList().size()+"classId="+classId);
        List<Mapping> views = new ArrayList<>();
        Mapping mapping = mappingService.getMappingByUrl("/studentManager/showStudentInfo.ftl");
        views.add(mapping);
        setAttr(Common.LABEL_VIEWS, views);
        setAttr("students",studentPageList);
        fillContentChild();
        render("/index.ftl");
    }


    /**
     * 显示学生详细信息
     */
    public void showStudentInfo(){
        Integer studentId = getParaToInt("studentId");
        Student student = studentService.getStudentById(studentId);
        setAttr("student",student);
        render("/studentManager/studentInfo.ftl");
    }

    /**
     * @return 返回一级菜单的mapping
     */
    @Override
    public Mapping init() {
        return mappingService.getMappingByUrl("/studentManager");
    }

    @Before(POST.class)
    public void addStudent() throws ServiceException {
        Student model = getModel(Student.class);
        if (studentService._save(model)) {
            RenderKit.renderSuccess(this);
            return;
        }

        RenderKit.renderError(this);
    }

    /**
     * 显示学生详细信息
     */
    public void showUpdateStudentInfo(){
        Integer studentId = getParaToInt("studentId");
        Student student = studentService.getStudentById(studentId);
        setAttr("student",student);
        render("/studentManager/updateStudentInfo.ftl");
    }
    /**
     * 更新学生信息[需要前台参数：stu(json数组)]
     */
    public void updateStudentInfo() {
        String jsonArrayStu = getPara("stu");
        JSONArray stuArray = JSON.parseArray(jsonArrayStu);
        for (Object o : stuArray) {
            JSONObject jsonObject = JSON.parseObject(o.toString());
            Student stu = new Student();
            stu.setId(jsonObject.getInteger("id"));
            stu.setBonus(jsonObject.getBigDecimal("bonus"));
            studentService._updateStudentById(stu);
        }
        RenderKit.renderSuccess(this);
    }

    public void getStudent() {
        Integer cid = getParaToInt("classId");
        Integer status = getParaToInt("status");
        if (status == null) {
            RenderKit.renderSuccess(this, JsonKit.toJson(studentService.getAllStudentByClassId(cid)));
        } else {
            RenderKit.renderSuccess(this, JsonKit.toJson(studentService.getStudentByClassId(cid, status)));
        }
    }

    public void getInputUrl() {
        super.index();
        mappingService.getMappingByUrl("/studentManager/inputStudentInfo.ftl");
    }
}
