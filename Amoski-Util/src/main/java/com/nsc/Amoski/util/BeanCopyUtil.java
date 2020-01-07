package com.nsc.Amoski.util;

import com.nsc.Amoski.entity.BaseUdfEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BeanCopyUtil {
    public static void main(String[] args) {
        BaseUdfEntity baseUdfEntity = new BaseUdfEntity();

        baseUdfEntity.setId(9999999);
        baseUdfEntity.setCreateName("1");
        baseUdfEntity.setCreateDate("2");
        baseUdfEntity.setUpdateName("3");
        baseUdfEntity.setUpdateDate("4");
        Map<String, String> updateSql = getUpdateSqlAll(baseUdfEntity);
        System.out.println(updateSql.get("update"));
        System.out.println(updateSql.get("where"));
    }

    public static Map<String,String> getUpdateSqlAll(Object obj) {
        Map<String,String> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        String table = clazz.getAnnotation(Table.class).name();
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer update = new StringBuffer("UPDATE "+table +" SET ");
        StringBuffer where = new StringBuffer("where ");
        for (Field field : fields) {
            Column column = field.getDeclaredAnnotation(Column.class);
            String Column="";
            if(column==null){
                Id id = field.getDeclaredAnnotation(Id.class);
                Transient declaredAnnotation = field.getDeclaredAnnotation(Transient.class);
                if(declaredAnnotation!=null&&declaredAnnotation.toString().equals("@javax.persistence.Transient()")){
                    continue;
                }
                if(field.getDeclaredAnnotation(Id.class)!=null&&field.getDeclaredAnnotation(Id.class).toString().equals("@javax.persistence.Id()")){
                    where.append("ID=:"+field.getName());
                }
            }else{
                update.append(column.name()+"=:"+field.getName()+",");
            }
        }
        map.put("update",update.toString().substring(0,update.length()-1)+" ");
        map.put("where",where.toString());
        return map;
    }

    public static String getSaveSqlAll(Object obj) {
        Class<?> clazz = obj.getClass();
        String table = clazz.getAnnotation(Table.class).name();
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer inert = new StringBuffer("INSERT INTO "+table +"(");
        StringBuffer value = new StringBuffer("VALUES(");
        for (Field field : fields) {
            Column column = field.getDeclaredAnnotation(Column.class);
            String Column="";
            if(column==null){
                Id id = field.getDeclaredAnnotation(Id.class);
                Transient declaredAnnotation = field.getDeclaredAnnotation(Transient.class);
                if(declaredAnnotation!=null&&declaredAnnotation.toString().equals("@javax.persistence.Transient()")){
                    continue;
                }
                if(field.getDeclaredAnnotation(Id.class)!=null && field.getDeclaredAnnotation(Id.class).toString().equals("@javax.persistence.Id()")){
                    inert.append("ID,");
                    value.append(table+"_SEQUENCE.NEXTVAL,");
                }
            }else{
                inert.append(column.name()+",");
                value.append(":"+field.getName()+"  ,");
            }
        }
        return inert.toString().substring(0,inert.length()-1)+"）"+value.toString().substring(0,value.length()-1)+"）";
    }

    public static String getSaveSql(Object obj) {
        Class<?> clazz = obj.getClass();
        String table = clazz.getAnnotation(Table.class).name();
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer inert = new StringBuffer("INSERT INTO "+table +"(");
        StringBuffer value = new StringBuffer("VALUES(");
        for (Field field : fields) {
            Column column = field.getDeclaredAnnotation(Column.class);
            String Column="";
            if(column==null){
                Transient declaredAnnotation = field.getDeclaredAnnotation(Transient.class);
                if(declaredAnnotation!=null&&declaredAnnotation.toString().equals("@javax.persistence.Transient()")){
                    continue;
                }
                if(field.getDeclaredAnnotation(Id.class).toString().equals("@javax.persistence.Id()")){
                    inert.append("id,");
                    value.append(":"+field.getName()+"  ,");
                }
            }else{
                inert.append(column.name()+",");
                value.append(":"+field.getName()+"  ,");
            }
        }
        return inert.toString().substring(0,inert.length()-1)+"）"+value.toString().substring(0,value.length()-1)+"）";
    }


    public static String getQuerySql(Object obj) {
        Class<?> clazz = obj.getClass();
        String table = clazz.getAnnotation(Table.class).name();
        return "select * from "+table;
    }

    public static String getQuerySql(Object obj,String dataBase) {
        Class<?> clazz = obj.getClass();
        String table = clazz.getAnnotation(Table.class).name();
        return "select * from "+dataBase+table;
    }
}
