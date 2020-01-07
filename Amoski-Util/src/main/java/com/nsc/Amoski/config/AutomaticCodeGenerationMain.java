package com.nsc.Amoski.config;

import com.nsc.Amoski.entity.BaseUdfEntity;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AutomaticCodeGenerationMain {

    public static void main(String[] args){


        String sql = "select b.COMMENTS table_name, a.TABLE_NAME tables, c.comments column_name, c.column_name columns " +
                " from user_tables a " +
                " left join user_tab_comments b on a.TABLE_NAME = b.TABLE_NAME " +
                " left join user_col_comments c on b.TABLE_NAME = c.TABLE_NAME " +
                " where a.TABLE_NAME='TB_ACTIVITY_SYNOPSIS'";
        List<Map<String,String>> namedParameterJdbcTemplate = getNamedParameterJdbcTemplate(sql);
        String tableName = "";
        String tables = "" ;
        StringBuffer strbug = new StringBuffer("");
        StringBuffer jsStrBug = new StringBuffer("");
        StringBuffer douHaoClumn = new StringBuffer("");
        for(Map<String,String> resultMap:namedParameterJdbcTemplate){
            tableName = resultMap.get("TABLE_NAME");//获取表中文名称
            tables = resultMap.get("TABLES");//获取表明
            Set<String> strings = resultMap.keySet();
            String columnName = resultMap.get("COLUMN_NAME");//获取中文列名
            String columns = resultMap.get("COLUMNS");//获取英文列名
            strbug.append("\t//"+columnName+"\n");
            jsStrBug.append("\t\t//"+columnName+"\n");
            //判断是否id
            if (columns.trim().equals("ID")){
                strbug.append("\t@Id\n");
            }else {
                strbug.append("\t@Column(name = \""+columns+"\")\n");
            }
            //strbug.append("\t@@MyValidator(name= \""+getColuntStr(columns)+"\",ColumnName=\""+columnName.trim()+"\")\n");
            strbug.append("\t@ColumnName(\""+columnName.trim()+"\")\n");
            strbug.append("\tprivate String "+getColuntStr(columns)+";\n");
            jsStrBug.append("\t\tthis."+getColuntStr(columns)+"="+getColuntStr(columns)+";\n");
            douHaoClumn.append(","+getColuntStr(columns));
            System.out.println("@ApiImplicitParam(name=\""+getColuntStr(columns)+"\",value=\""+columnName+"\",dataType=\"string\", paramType = \"query\"),");
        }
        String tablesStr = getColuntStr(tables);

        //后端实体类
        System.out.println("\n\n\n\n\n\nimport javax.persistence.Column;\n" +
                "import lombok.Data;\n" +
                "import javax.persistence.Entity;\n" +
                "import javax.persistence.Id;\n" +
                "import javax.persistence.Table;\n" +
                "import java.io.Serializable;");
        System.out.println("//"+tableName);
        System.out.println("@Entity\n@Table(name = \""+tables+"\")\n@Data");
        System.out.println("public class "+tablesStr.substring(0,1).toUpperCase()+tablesStr.substring(1)+"Entity  implements  Serializable{");
        System.out.println(strbug.toString());
        System.out.println("}\n\n\n\n\n\n\n\n\n");

        //js前端保存实体类
        System.out.println("//"+tableName);
        System.out.println("var "+ tablesStr + " = new " + tablesStr.substring(0,1).toUpperCase() + tablesStr.substring(1) + "();");
        System.out.println("class "+tablesStr.substring(0,1).toUpperCase()+tablesStr.substring(1)+"Entity{");
        System.out.println("\tconstructor("+douHaoClumn.substring(1)+") {");
        System.out.println(jsStrBug.toString());
        System.out.println("\t}");
        System.out.println("}\n\n\n\n\n\n\n\n\n");

    }


    public static List getNamedParameterJdbcTemplate(String sql){
        DataSource dataSource = getUserDataSource();
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        //List query = jdbcTemplate.query(sql, new HashMap<>(), new BeanPropertyRowMapper(List<Map<String,String>>.class));
        //List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, new HashMap<>());
        //jdbcTemplate.query(sql, new HashMap<>(), ResultSet);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, new HashMap<>());
        return list;
    }

    public static DataSource getUserDataSource(){
        DataSource ds = new DataSource();
        ds.setUrl("jdbc:oracle:thin:@192.168.5.163:1521:orcl");
        ds.setUsername("amoski_activity");
        ds.setPassword("amoski_activity");
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

    public static String getColuntStr(String str ){
        String[] split = str.toLowerCase().split("_");
        return getColunt(split);
    }

    public static String getColunt(String[] strAll){
        StringBuffer strbug = new StringBuffer("");
        strbug.append(strAll[0]);
        for(int i=1 ;i<strAll.length ;i++ ){
            String s = strAll[i];
            strbug.append(s.substring(0,1).toUpperCase()+s.substring(1));
        }
        return strbug.toString();
    }
}
