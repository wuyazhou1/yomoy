package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.WaterMarkManageDao;
import com.nsc.Amoski.dto.WaterMakeInfoDto;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TbPhotoPic;
import com.nsc.Amoski.entity.TbWaterMakeInfo;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.RegUtil;
import lombok.experimental.var;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WaterMarkManageDaoImpl  extends GenericRepositoryImpl implements WaterMarkManageDao {

    /**
     * 根据部门id查询所有的水印(包括下级部门)
     * @param param 查询条件 默认当前用户部门及下级部门所有id
     * @return  所有水印信息
     */
    public PagingBean queryWaterMakeByDept(WaterMakeInfoDto param){
        RegUtil regUtil=RegUtil.getSingleton();

        StringBuilder sql=new StringBuilder("select * from TB_WATERMAKE_INFO");
        StringBuilder countSql=new StringBuilder("select count(*) from TB_WATERMAKE_INFO");
        StringBuilder whereSql=new StringBuilder(" where dept_id=-1 ");

        Map<String,Object> map=new HashMap<String, Object>();
        map.put("pageSize",param.getPageSize());
        map.put("start",param.getStart());
        if(!regUtil.isNull(param.getDeptId())){//所有部门id
            String [] split =param.getDeptId().split(",");
            whereSql.append(" and exists (select * from (select regexp_substr(:deptIds,'[^,]+',1,rownum) did from dual" +
                            " connect by rownum<=length(regexp_replace(:deptIds,'[^,]+'))) b where dept_id=b.did)");
            map.put("deptIds",param.getDeptId());
        }
        if(!regUtil.isNull(param.getCreateUser())){//根据创建用户查询
            whereSql.append(" and create_user = :createUser");
            map.put("createUser",param.getCreateUser());
        }
        if(!regUtil.isNull(param.getCreateTime())){//根据创建时间查询
            whereSql.append(" and create_time = :createTime");
            map.put("createTime",param.getCreateTime());
        }
        if(!regUtil.isNull(param.getStatus())){//根据状态查询
            whereSql.append(" and status = :status");
            map.put("status",param.getStatus());
        }
        String querySql=sql.append(whereSql).toString(),queryCountSql=countSql.append(whereSql).toString();
        querySql=querySql+" order by status,create_time desc";
        logger.info(">>>>queryWaterMakeByDept>>>>> querySql:"+querySql+">>>>>>>paramMap:"+map.toString());
        logger.info(">>>>queryWaterMakeByDept>>>>> queryCountSql:"+queryCountSql+">>>>>>>");
        int count= this.jdbcTemplate.queryForObject(queryCountSql,map,Integer.class);
        List<TbWaterMakeInfo> list=this.jdbcTemplate.query(pageSql(querySql,map), map,new BeanPropertyRowMapper<TbWaterMakeInfo>(TbWaterMakeInfo.class));
        /*
         , new RowMapper<TbWaterMakeInfo>() {
            @Override
            public TbWaterMakeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                TbWaterMakeInfo entity = new TbWaterMakeInfo();
                entity.setId(rs.getInt("id"));
                entity.setCreateTime(rs.getDate("create_time"));
                entity.setCreateUser(rs.getString("create_user"));
                entity.setImgUrl(rs.getString("img_url"));
                entity.setDeptId(rs.getString("dept_id"));
                entity.setStatus(rs.getString("status"));
                entity.setSmallImgUrl(rs.getString("SMALL_IMG_URL"));
                entity.setUpdateUser(rs.getString("UPDATE_USER"));
                entity.setUpdateTime(rs.getDate("UPDATE_TIME"));
                entity.setRemake(rs.getString("remake"));
                return entity;
            }
        }
         */
        PagingBean bean=new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryWaterMakeByDept>>>>>where"+whereSql+">>>>>>>paramMap:"+bean.toString());
        return bean;
    }

    /**
     * 更新水印信息
     * @param info  要更新的水印信息
     */
    public void updateWaterMarkInfo(TbWaterMakeInfo info){
        this.update(info);
    }

    /**
     * 新增水印信息
     * @param info  要新增的水印信息
     */
    public void saveWaterMarkInfo(TbWaterMakeInfo info){
        this.save(info);
    }

    /**
     * 删除水印信息
     * @param id  要删除的水印id
     */
    public void deleteWaterMarkInfo(String id){
    }
}
