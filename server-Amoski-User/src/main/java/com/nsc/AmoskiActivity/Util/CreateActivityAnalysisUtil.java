package com.nsc.AmoskiActivity.Util;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateActivityAnalysisUtil {
    public static Object ActivityAnalysisUtil(JSONObject jsonObject,Class<?> clazz){
        Object newClass=null;
        try {
            //创建类
            newClass = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Column column = field.getDeclaredAnnotation(Column.class);
                field.setAccessible(true);
                if(column==null){
                    Transient declaredAnnotation = field.getDeclaredAnnotation(Transient.class);
                    if(declaredAnnotation!=null&&declaredAnnotation.toString().equals("@javax.persistence.Transient()")){
                        continue;
                    }
                    if( field.getDeclaredAnnotation(Id.class).toString().equals("@javax.persistence.Id()") && jsonObject.get(field.getName())!=null && !jsonObject.get(field.getName()).toString().trim().equals("") ){
                        field.set(newClass,getVlues(field,jsonObject.get(field.getName()).toString().replace("#","").replace("!","")));
                    }
                }else{
                    try {
                        field.set(newClass,getVlues(field, jsonObject.get(field.getName().trim())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newClass;
    }

    private static Object getVlues(Field field,Object obj){
        String value=null;
        if(obj==null || obj.toString().trim().equals("")){
            return null;
        }else{
            value=obj.toString();
        }
        String type = field.getType().toString().split(" ")[1];
        if(type.equals("java.lang.String")){
            return value;
        }else if(type.equals("java.util.Date")){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(value.indexOf("-")==-1){
                return new Date(Long.parseLong(value));
            }
            try {
                return format.parseObject(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(type.equals("java.lang.Integer")||type.equals("int")){
            value.replace("#","");
            return Integer.parseInt(value);
        }else if(type.equals("java.lang.Double")||type.equals("double")){
            return Double.parseDouble(value);
        }else if(type.equals("java.math.BigDecimal")){
            return new BigDecimal(value);
        }else{
            return null;
        }
        return null;
    }
}
