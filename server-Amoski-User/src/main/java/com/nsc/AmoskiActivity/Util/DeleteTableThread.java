package com.nsc.AmoskiActivity.Util;

import com.nsc.Amoski.repository.GenericRepositoryImpl;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class DeleteTableThread  extends GenericRepositoryActivityImpl implements Callable<Map<String,List<String>>> {
    public List<String> list;
    public Map<String,Object> parentMap;
    public DeleteTableThread(){}
    public DeleteTableThread(NamedParameterJdbcTemplate jdbcTemplate,String table,Map<String,Object> parentMap){
        this.jdbcTemplate=jdbcTemplate;
        list= new ArrayList<>();
        list.add(table);
        this.parentMap=parentMap;
    }
    public DeleteTableThread(NamedParameterJdbcTemplate jdbcTemplate,List<String> list,Map<String,Object> parentMap){
        this.jdbcTemplate=jdbcTemplate;
        this.list=list;
        this.parentMap=parentMap;
    }
    @Override
    public Map<String,List<String>> call() throws Exception {
        Map<String,List<String>> map = new HashMap<>();
        map.put("success",new ArrayList<>());
        map.put("error",new ArrayList<>());
        for (int i=0;i<list.size();i++){
            try {
                this.jdbcTemplate.update("delete "+list.get(i),this.parentMap);
                map.get("success").add(list.get(i));
            } catch (Exception e) {
                map.get("error").add(list.get(i));
                e.printStackTrace();
            }
        }
        return map;
    }
}
