package com.nsc.Amoski.util;

import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;

public class ClassVariableCreateUtil extends GenericRepositoryImpl implements Callable<Map<String , Object>> {
    public HttpServletRequest request;
    public String classPath;
    public String FilePath;
    public ShiroUser shiroUser;

    public ClassVariableCreateUtil(HttpServletRequest request,String classPath,String FilePath,NamedParameterJdbcTemplate jdbcTemplate,ShiroUser shiroUser){
        this.request=request;
        this.classPath=classPath;
        this.FilePath=FilePath;
        this.jdbcTemplate=jdbcTemplate;
        this.shiroUser=shiroUser;
    }

    @Override
    public Map<String , Object> call() throws Exception {
        //加载类
        Class<?> aClass = Class.forName(classPath);
        //获取类参数
        String table = request.getParameter(aClass.getSimpleName());
        //判断类是否有值
        if(table==null||table.equals("")){
            return null;
        }
        //分隔类
        String[] split = table.split(",");
        //创建类集合
        Map<String , Object> mapParent = new HashMap<>();
        //便利分隔类
        for(int i=0 ; i < split.length; i++){
            //判断分隔类是否有值，没有跳过循环
            if(split[i].trim().equals("")){
                continue;
            }
            //创建类
            Object newClass = aClass.newInstance();
            //获取类中的所有属性
            Field[] fields = aClass.getDeclaredFields();
            //便利属性
            for (Field field:fields){
                //解锁私有属性
                field.setAccessible(true);
                //获取传递属性值
                String value = request.getParameter(aClass.getSimpleName() + "[" + (i + 1) + "]." + field.getName());
                //判断传递的值是否为空,为空则跳过此次循环
                if(value==null||value.equals("")){
                    continue;
                }else if(field.getName().equals("orgCode")){
                    field.set(newClass,shiroUser.getOrgCode());
                }
                //类赋值属性
                field.set(newClass,findValue(getTypeSplit(field.getType().toString()),value));
            }
            //添加类
            mapParent.put("add",newClass);
        }
        return mapParent;
    }


    public static void main(String[] args){
        try {
            Class<?> aClass = Class.forName("com.nsc.Amoski.entity.TbActivityBasicsEntity");

            Object newClass = aClass.newInstance();
            Field[] fields = aClass.getDeclaredFields();
            for (Field field:fields){
                field.setAccessible(true);
                System.out.println(field.getName());
                System.out.println(aClass.getSimpleName()+"."+getTypeSplit(field.getType().toString()));
                //field.set(newClass,);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Object findValue(String type , String value) throws ParseException {
        if(type.equals("java.lang.String")){
            return value;
        }else if(type.equals("java.util.Date")){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            return format.parseObject(value);
        }else if(type.equals("java.lang.Integer")||type.equals("int")){
            return Integer.parseInt(value);
        }else if(type.equals("java.lang.Double")||type.equals("double")){
            return Double.parseDouble(value);
        }else{
            return null;
        }
    }

    public static String getTypeSplit(String type){
        String[] typeSplit = type.split(" ");
        if(typeSplit.length==1){
            return typeSplit[0];
        }else{
            return typeSplit[1];
        }
    }
}
