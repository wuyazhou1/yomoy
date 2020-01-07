package com.nsc.Amoski.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

public class BackupsDocument {

    private static NamedParameterJdbcTemplate jdbcTemplate;
    private static Map<String,Object> sqlResultMap = new HashMap<>();

    public static void main(String[] args){
        getNamedParameterJdbcTemplate();
        List<Map<String, Object>> listMap = jdbcTemplate.queryForList("select t.table_name,a.commEnts from user_tables t\n" +
                "left join user_tab_comments a on t.table_name=a.table_name", new HashMap<>());
        for(int i=0;i<listMap.size();i++){
            Map<String, Object> map = listMap.get(i);
            List<Map<String, Object>> sqlListMap = jdbcTemplate.queryForList("select b.COMMENTS table_name, a.TABLE_NAME tables, c.comments column_name, c.column_name columns \n" +
                    "                 from user_tables a \n" +
                    "                 left join user_tab_comments b on a.TABLE_NAME = b.TABLE_NAME \n" +
                    "                 left join user_col_comments c on b.TABLE_NAME = c.TABLE_NAME \n" +
                    "                 where a.TABLE_NAME = :TABLE_NAME ", map);
            List<String> commentSql = new ArrayList<>();
            commentSql.add("COMMENT ON TABLE '"+sqlListMap.get(0).get("TABLES")+"'  IS '"+sqlListMap.get(0).get("TABLE_NAME")+"'");
            StringBuffer columns = new StringBuffer();
            for(int j=0;j<sqlListMap.size();j++){
                Map<String, Object> sqlMap = sqlListMap.get(j);
                if(sqlMap.get("COLUMN_NAME")!=null)
                    commentSql.add("COMMENT ON COLUMN  "+sqlMap.get("TABLES").toString()+"."+sqlMap.get("COLUMNS").toString()+"  IS  '"+sqlMap.get("COLUMN_NAME").toString()+"'");
                columns.append(","+sqlMap.get("COLUMNS").toString());
            }
            Map<String,Object> sqlMap = new HashMap<>();
            sqlMap.put("commentSql",commentSql);
            sqlMap.put("columns",columns);
            sqlResultMap.put(sqlListMap.get(0).get("TABLES").toString(),sqlMap);
        }
        //备份表数据
        backupString();
        //数据还原
        //删除备份
    }
    public static void backupString(){
        Set<String> strings = sqlResultMap.keySet();
        System.out.println("--数据备份"+sqlResultMap.size()+"个表");
        for(String str:strings){
            Map<String,Object> sqlMap = ( Map<String,Object>)sqlResultMap.get(str);
            System.out.println("create table backups"+str+" as select "+sqlMap.get("columns").toString().substring(1)+" from "+str+";\n");
        }
        System.out.println("\n\n\n\n\n\n");
        System.out.println("--删除表");
        for(String str:strings){
            System.out.println("drop table "+str+";\n");
        }
        System.out.println("\n\n\n\n\n\n");
        System.out.println("--数据还原");
        for(String str:strings){
            Map<String,Object> sqlMap = ( Map<String,Object>)sqlResultMap.get(str);
            System.out.println("insert into "+str+"("+sqlMap.get("columns").toString().substring(1)+") select "+sqlMap.get("columns").toString().substring(1)+"  from  backups"+str+";\n");
        }
        System.out.println("\n\n\n\n\n\n");
        System.out.println("--删除备份表");
        for(String str:strings){
            System.out.println("drop table backups"+str+";\n");
        }
    }

    public static DataSource getUserDataSource(){
        DataSource ds = new DataSource();
        ds.setUrl("jdbc:oracle:thin:@116.255.186.112:1521:orcl");
        ds.setUsername("amoski_activity");
        ds.setPassword("wusiyaomayuqinglongdan");
        ds.setDriverClassName("oracle.jdbc.OracleDriver");
        ds.setMaxActive(50);
        ds.setMaxIdle(50);
        ds.setMinIdle(60000);
        ds.setInitialSize(10);
        ds.setValidationQuery("select 1 from dual");
        ds.setTestOnBorrow(true);
        ds.setTestOnReturn(true);
        ds.setTestWhileIdle(true);
        return ds;
    }
    public static void getNamedParameterJdbcTemplate(){
        DataSource dataSource = getUserDataSource();
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
