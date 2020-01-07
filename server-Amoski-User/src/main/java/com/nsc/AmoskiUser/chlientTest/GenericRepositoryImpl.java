package com.nsc.AmoskiUser.chlientTest;


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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericRepositoryImpl {
    protected final Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /*@PersistenceContext
    private EntityManager em;*/
    @PersistenceContext
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    @Autowired
    @Qualifier("NamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate jdbcTemplate;

   /* @Autowired
    @Qualifier("ActivityNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate jdbcActivityTemplate;

    public NamedParameterJdbcTemplate getJdbcActivityTemplate() {
        return jdbcActivityTemplate;
    }
    public void setJdbcActivityTemplate(NamedParameterJdbcTemplate jdbcActivityTemplate) {
        this.jdbcActivityTemplate = jdbcActivityTemplate;
    }*/
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
        if(entity!=null){
            em.persist(entity);
            em.flush();
        }
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
            sql = "SELECT  "+selectColumn()+"  FROM ( SELECT q.*, ROWNUM AS rn FROM (" + sql + ") q  WHERE ROWNUM <= :pageSize ) w  WHERE w.rn > :start ";
        else{
            if(sql.toUpperCase().indexOf("ORDER")>-1)
                sql = "SELECT  "+selectColumn()+"  FROM ( SELECT q.*, ROWNUM AS rn FROM (" + sql + ") q  WHERE ROWNUM <= :pageSize) w  WHERE w.rn > :start";
            else
                sql = "SELECT  "+selectColumn()+"  FROM ( SELECT q.*, ROWNUM AS rn FROM (" + sql + " order by  "+params.get("orderColumn")+" "+params.get("orderDir")+") q  WHERE ROWNUM <= :pageSize ) w  WHERE w.rn > :start ";
        }
        return sql;
    }

    public String selectColumn(){
        return "*";
    }
}
