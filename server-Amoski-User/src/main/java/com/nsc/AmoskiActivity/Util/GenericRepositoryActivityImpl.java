package com.nsc.AmoskiActivity.Util;


import com.nsc.Amoski.entity.BaseUdfEntity;
import com.nsc.Amoski.util.BeanCopyUtil;
import com.nsc.Amoski.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericRepositoryActivityImpl {
    protected final Logger logger = LoggerFactory.getLogger(GenericRepositoryActivityImpl.class);
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /*@PersistenceContext
    private EntityManager em;*/
    @PersistenceContext
    //@Autowired
    //@Autowired
    //@Qualifier("entityManagerFactoryActivity")
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Autowired
    @Qualifier("ActivityNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate jdbcTemplate;

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     * 单行数据保存信息
     * @param entity
     * @return
     */
    public Object save(Object entity){
//        List<Object> list = new ArrayList<>();
//        list.add(entity);
        String sqlAll = BeanCopyUtil.getSaveSqlAll(entity);
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(entity);
        System.out.println(sqlAll);
        jdbcTemplate.update(sqlAll,Parameter);
        return entity;
    }

    public <T>T saveEntity(T entity){
        Class<?> aClass = entity.getClass();
        Integer id=null;
        try {
            String sql = aClass.getAnnotation(Table.class).name();
            sql="select "+sql+"_SEQUENCE.nextval from dual";
            id = jdbcTemplate.queryForObject(sql, new HashMap<>(), Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("id序列号生成错误，"+aClass.getAnnotation(Table.class).name()+"_SEQUENCE序列号不存在");
        }
        try {
            Field id1 = aClass.getDeclaredField("id");
            id1.setAccessible(true);
            id1.set(entity,id);
            try {
                Field code = aClass.getDeclaredField("code");
                if(code!=null){
                    code.setAccessible(true);
                    if(code.get(entity)==null)
                        code.set(entity,id.toString());
                }
            } catch (Exception e) {
                logger.info("累赋值"+aClass.getName()+"该类没有code字段");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sqlAll = BeanCopyUtil.getSaveSql(entity);
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(entity);
        System.out.println(sqlAll);
        jdbcTemplate.update(sqlAll,Parameter);
        return entity;
    }

    /**
     * 批量保存数据
     * @param list
     */
    public <T> void saveAll(List<T> list){
        for(Object obj:list){
            if(obj!=null){
                if(obj instanceof BaseUdfEntity){
                    BaseUdfEntity baseUdfEntity = (BaseUdfEntity)obj;
                    baseUdfEntity.setCreateDate(format.format(new Date()));
                }
            }
        }
        String sqlAll = BeanCopyUtil.getSaveSqlAll(list.get(0));
        SqlParameterSource[] Parameter = SqlParameterSourceUtils.createBatch(list.toArray());
        System.out.println(sqlAll);
        jdbcTemplate.batchUpdate(sqlAll,Parameter);
    }

    /**
     * 批量修改数据
     * @param object
     */
    public void update(Object object){
        Map<String, String> updateSqlAll = BeanCopyUtil.getUpdateSqlAll(object);
        String sql = updateSqlAll.get("update")+updateSqlAll.get("where");
        System.out.println(sql);
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(object);
        jdbcTemplate.update(sql,Parameter);
    }


    /**
     * 批量修改数据
     * @param list
     */
    public void updateAll(List<Object> list){
        for(Object obj:list){
            if(obj!=null){
                if(obj instanceof BaseUdfEntity){
                    BaseUdfEntity baseUdfEntity = (BaseUdfEntity)obj;
                    baseUdfEntity.setCreateDate(format.format(new Date()));
                }
            }
        }
        Map<String, String> updateSqlAll = BeanCopyUtil.getUpdateSqlAll(list.get(0));
        SqlParameterSource[] Parameter = SqlParameterSourceUtils.createBatch(list.toArray());
        String sql = updateSqlAll.get("update")+"  "+updateSqlAll.get("where");
        System.out.println(sql);
        jdbcTemplate.batchUpdate(sql,Parameter);
    }

    /**
     * 批量修改数据，自带筛选条件
     * @param list
     * @param where
     */
    public void updateAllWhere(List<Object> list,String where){
        for(Object obj:list){
            if(obj!=null){
                if(obj instanceof BaseUdfEntity){
                    BaseUdfEntity baseUdfEntity = (BaseUdfEntity)obj;
                    baseUdfEntity.setCreateDate(format.format(new Date()));
                }
            }
        }
        Map<String, String> updateSqlAll = BeanCopyUtil.getUpdateSqlAll(list.get(0));
        SqlParameterSource[] Parameter = SqlParameterSourceUtils.createBatch(list.toArray());
        String sql = updateSqlAll.get("update")+updateSqlAll.get("where")+where;
        System.out.println(sql);
        jdbcTemplate.batchUpdate(sql,Parameter);
    }

    public List queryFormatListMap(Object obj , String where){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(BeanCopyUtil.getQuerySql(obj) + " where " + where, new HashMap<>());
        return maps;
    }
    public List query(Object obj , String where){
        List list = jdbcTemplate.query(BeanCopyUtil.getQuerySql(obj) + " where " + where, new HashMap<>(), new BeanPropertyRowMapper(obj.getClass()));
        return list;
    }
    public <T> List<T> query(T obj , String where,Map<String,Object> map){
        List list = jdbcTemplate.query(BeanCopyUtil.getQuerySql(obj) + " where " + where,map, new BeanPropertyRowMapper(obj.getClass()));
        return list;
    }
    /**
     * sql执行数据
     * @param sql
     */
    public void update(String sql){
        jdbcTemplate.update(sql,new HashMap());
    }


    public String pageSql(String sql,Map<String, Object> params) {
        if(StringUtils.isEmpty(params.get("orderColumn")))
            sql = heavyLoadSql("SELECT  "+selectColumn()+"  FROM ( SELECT q.*, ROWNUM AS rn FROM (" + sql + ") q  WHERE ROWNUM <= :pageSize ) w  WHERE w.rn > :start ");
        else{
            if(sql.toUpperCase().indexOf("ORDER")>-1)
                sql = heavyLoadSql("SELECT  "+selectColumn()+"  FROM ( SELECT q.*, ROWNUM AS rn FROM (" + sql + ") q  WHERE ROWNUM <= :pageSize) w  WHERE w.rn > :start");
            else
                sql = heavyLoadSql("SELECT  "+selectColumn()+"  FROM ( SELECT q.*, ROWNUM AS rn FROM (" + sql + " order by  "+params.get("orderColumn")+" "+params.get("orderDir")+") q  WHERE ROWNUM <= :pageSize ) w  WHERE w.rn > :start ");
        }
        loggerInfo(sql);
        return sql;
    }
    public String pageSqlAll(String sql,Map<String, Object> params) {
        if(StringUtils.isEmpty(params.get("orderColumn")))
            sql = heavyLoadSql("SELECT  "+selectColumn(params)+"  FROM ( SELECT q.*, ROWNUM AS rn FROM (" + sql + ") q  WHERE ROWNUM <= :pageSize ) w  WHERE w.rn > :start ");
        else{
            if(sql.toUpperCase().indexOf("ORDER")>-1)
                sql = heavyLoadSql("SELECT  "+selectColumn(params)+"  FROM ( SELECT q.*, ROWNUM AS rn FROM (" + sql + ") q  WHERE ROWNUM <= :pageSize) w  WHERE w.rn > :start");
            else
                sql = heavyLoadSql("SELECT  "+selectColumn(params)+"  FROM ( SELECT q.*, ROWNUM AS rn FROM (" + sql + " order by  "+params.get("orderColumn")+" "+params.get("orderDir")+") q  WHERE ROWNUM <= :pageSize ) w  WHERE w.rn > :start ");
        }
        loggerInfo(sql);
        return sql;
    }
    public void loggerInfo(String sql){
        logger.info(sql);
    }
    public String selectColumn(Map<String, Object> params){
        return "*";
    }
    public String selectColumn(){
        return "*";
    }

    public String heavyLoadSql(String sql){
        return sql;
    }
}
