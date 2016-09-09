package com.hudongwx.studentsys.common;

import com.hudongwx.studentsys.model.Mapping;
import com.hudongwx.studentsys.model.User;
import com.hudongwx.studentsys.service.MappingService;
import com.hudongwx.studentsys.service.RoleService;
import com.hudongwx.studentsys.util.Common;
import com.hudongwx.studentsys.util.LangConfig;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.log.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wuhongxu on 2016/8/30 0030.
 */
public class BaseController extends Controller {
    protected Log log = Log.getLog(this.getClass());

    public BaseController() {
        Field[] fields = this.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Class clazz = field.getType();
            if (Service.class.isAssignableFrom(clazz) && clazz != Service.class) {
                try {
                    field.setAccessible(true);
                    field.set(this, Service.getInstance(clazz));
                } catch (IllegalAccessException e) {
                    log.error(e.toString());
                }
            }
        }
    }
    public void fillHeader() {
        //三个地址：static用于找资源、servePath用于得到去掉参数的网址、holdPath为带参数网址
        String uri = getRequest().getRequestURI();
        String url = String.valueOf(getRequest().getRequestURL());
        String staticPath = url.substring(0, url.lastIndexOf(uri));
        String para = getRequest().getQueryString();
        if (null == para)
            para = "";
        else
            para = "?" + para;
        String actionKey = getAttr(Common.ACTION_KEY_LABEL);
        String servePath = staticPath + actionKey;
        /*if(staticPath.endsWith("/"))
            staticPath = staticPath.substring(0,staticPath.length()-1);
        if(servePath.endsWith("/"))
            servePath = servePath.substring(0,servePath.length()-1);
        if(url.endsWith("/"))
            url = url.substring(0,url.length()-1);*/
        url += para;

        /*log.info("static用于找资源:"+staticPath);
        log.info("servePath用于得到去掉参数的网址:"+servePath);
        log.info("holdPath为带参数网址:"+url);*/

        setAttr(Common.STATIC_SERVE_PATH_LABEL, staticPath);
        setAttr(Common.SERVE_PATH_LABEL, servePath);
        setAttr(Common.HOLD_PATH_LABEL, url);

        setAttr(Common.STATIC_RESOURCE_VERSION_LABEL, new Date().getTime());
        setAttr(Common.LOGIN_ROLE_LABEL, "学生");
        setAttr(Common.IS_LOGIN_LABEL, false);
        Prop langProp = LangConfig.getLangProp();
        setAttr(Common.LOGIN_NAME_ERROR_LABEL, langProp.get(Common.LOGIN_NAME_ERROR_LABEL));
        setAttr(Common.INVALID_PASSWORD_LABEL, langProp.get(Common.INVALID_PASSWORD_LABEL));
    }

    private void fillFooter() {
        /*Prop langProp = LangConfig.getLangProp();
        Enumeration<Object> elements = langProp.getProperties().elements();
        while(elements.hasMoreElements()) {
            Object o = elements.nextElement();
            //System.out.println(o);
        }*/
    }

    //页面测试
    protected void fillTest(MappingService mappingService) {

        List<Mapping> mappings = mappingService.getMappings();

        setAttr(Common.SIDES_LABEL,mappings);

    }
    public void fillContent(RoleService roleService){
        User user = getSessionAttr("user");
        if(user == null){
            forwardAction("/user/login");
            return ;
        }
        List<Mapping> roleTree = roleService.getRoleTree(roleService.getRoleByName(user.getUserRole()));

        List<Mapping> sides = new ArrayList<>();
        List<Mapping> content = new ArrayList<>();
        for(Mapping mapping : roleTree){
            if(mapping.getDegree() == 1)
                sides.add(mapping);
            if(mapping.getDegree() == 2)
                content.add(mapping);
        }
        setAttr(Common.SIDES_LABEL,sides);
        setAttr(Common.CONTENT_LABEL,content);
    }
    public void fillHeaderAndFooter() {
        fillHeader();
        fillFooter();
    }


}