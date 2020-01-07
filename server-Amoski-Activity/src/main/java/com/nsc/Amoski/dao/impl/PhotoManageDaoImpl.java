package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.PhotoManageDao;
import com.nsc.Amoski.dto.ActivityCalendarImagesDto;
import com.nsc.Amoski.dto.PhotoPicDto;
import com.nsc.Amoski.dto.UserPhotoDto;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.RegUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PhotoManageDaoImpl extends GenericRepositoryImpl implements PhotoManageDao {

    /**
     * 根据会员id 查询会员相册
     * @param userId 查询条件  用户id
     * @return  所有相册信息
     */
    public List<UserPhotoDto> queryUserPhotoByUserId(String userId){

        StringBuilder sql=new StringBuilder("select ph.*,wpi.base_url baseUrl,wpi.small_url smallUrl from TB_USER_PHOTO ph left join (select * from TB_PHOTO_PIC pi inner join  \n" +
                "(select max(rowid) rid from TB_PHOTO_PIC where MEMBER_ID=:userId group by MEMBER_ID,photo_id) ppi on pi.rowid=ppi.rid" +
                ") wpi on wpi.member_id=ph.member_id and ph.id=wpi.photo_id");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and ph.MEMBER_ID=:userId order by ph.create_time desc");
        String querySql=sql.append(whereSql).toString();
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userId",userId);
        logger.info(">>>>queryUserPhotoByUserId>>>>>where"+whereSql+">>>>>>>paramMap:"+map.toString());
        List<UserPhotoDto> list=this.jdbcTemplate.query(querySql, map,new BeanPropertyRowMapper<UserPhotoDto>(UserPhotoDto.class));
        logger.info(">>>>queryUserPhotoByUserId>>>>>resultList:"+list);
        return list;
    }

    /**
     * 根据相册id 查询会员相册
     * @param ids 查询条件  相册id
     * @return  所有相册信息
     */
    public List<TbUserPhoto> queryUserPhotoByPhotoId(String userId,String ids){

        StringBuilder sql=new StringBuilder("select * from TB_USER_PHOTO");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and member_id=:userId and exists (select * from (select regexp_substr(:ids,'[^,]+',1,rownum) did from dual\" +\n" +
                "                \" connect by rownum<=length(regexp_replace(:ids,'[^,]+'))) b where id=b.did)");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("ids",ids);
        map.put("userId",userId);
        logger.info(">>>>queryUserPhotoByPhotoId>>>>>where"+whereSql+">>>>>>>paramMap:"+map.toString());
        List<TbUserPhoto> list=this.jdbcTemplate.query(sql.append(whereSql).toString(), map,new BeanPropertyRowMapper<TbUserPhoto>(TbUserPhoto.class));
        /*
        , new RowMapper<TbUserPhoto>() {
            @Override
            public TbUserPhoto mapRow(ResultSet rs, int rowNum) throws SQLException {
                TbUserPhoto entity = new TbUserPhoto();
                entity.setId(rs.getInt("id"));
                entity.setCreateTime(rs.getDate("create_time"));
                entity.setCreateUser(rs.getString("create_user"));
                entity.setStatus(rs.getString("status"));
                entity.setPhotoName(rs.getString("photo_name"));
                return entity;
            }
        }
         */
        logger.info(">>>>queryUserPhotoByUserId>>>>>resultList:"+list);
        return list;
    }

    /**
     * 删除数据(相册或相片)
     * @param list  要删除的数据集合
     */
    public void deleteData(List<Object> list){
        this.updateAll(list);
    }

    /**
     * 查询用户相册信息
     * @param dto 查询信息
     * @return
     */
    public PagingBean queryUserPicByPhotoId(PhotoPicDto dto){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select * from TB_PHOTO_PIC ");
        StringBuilder countSql=new StringBuilder("select count(*) from TB_PHOTO_PIC ");
        Map<String,Object> map=new HashMap<String, Object>();
        //map.put("userId",dto.getMemberId());
        //map.put("photoId",dto.getPhotoId());
        map.put("pageSize",dto.getPageSize());
        map.put("start",dto.getStart());
        StringBuilder whereSql=new StringBuilder(" where 1=1 ");//and PHOTO_ID=:photoId
        if(!regUtil.isNull(dto.getMemberId())){
            whereSql.append(" and MEMBER_ID=:userId ");
            map.put("userId",dto.getMemberId());
        }
        String querySql=sql.append(whereSql).toString();
        String queryCountSql=countSql.append(whereSql).toString();
        querySql=querySql+" order by create_time desc";
        logger.info(">>>>queryUserPicByPhotoId>>>>>where"+queryCountSql+">>>>>>>paramMap:"+map.toString());
        logger.info(">>>>queryUserPicByPhotoId>>>>>where"+pageSql(querySql,map)+">>>>>>>>>.>>");
        int count= this.jdbcTemplate.queryForObject(queryCountSql,map,Integer.class);
        List<TbPhotoPic> list=this.jdbcTemplate.query(pageSql(querySql,map), map,new BeanPropertyRowMapper<TbPhotoPic>(TbPhotoPic.class));
        /*
        , new RowMapper<TbPhotoPic>() {
            @Override
            public TbPhotoPic mapRow(ResultSet rs, int rowNum) throws SQLException {
                TbPhotoPic entity = new TbPhotoPic();
                entity.setId(rs.getInt("id"));
                entity.setCreateTime(rs.getDate("create_time"));
                entity.setCreateUser(rs.getString("create_user"));
                entity.setStatus(rs.getString("status"));
                entity.setBaseUrl(rs.getString("base_url"));
                entity.setImgUrl(rs.getString("img_url"));
                entity.setMemberId(rs.getInt("member_id"));
                entity.setPhotoId(rs.getInt("photo_id"));
                entity.setSmallUrl(rs.getString("small_url"));
                return entity;
            }
        }
         */
        PagingBean bean=new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryUserPicByPhotoId>>>>>where"+whereSql+">>>>>>>result:"+bean.toString());
        return bean;
    }

    /**
     * 查询所有用户信息
     * @param dto
     * @return
     */
    public PagingBean queryAllMemberInfo(MemberView dto){
        return new PagingBean();
    }
    /**
     * 查询用户相片  根据id
     * @param ids 相片ids
     * @return
     */
    public List<TbPhotoPic> queryUserPicById(String userId,String ids){
        StringBuilder sql=new StringBuilder("select * from TB_PHOTO_PIC");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("ids",ids);
        map.put("userId",userId);
        StringBuilder whereSql=new StringBuilder(" where 1=1 and member_id=:userId and exists (select * from (select regexp_substr(:ids,'[^,]+',1,rownum) did from dual\" +\n" +
                "                \" connect by rownum<=length(regexp_replace(:ids,'[^,]+'))) b where id=b.did)");

        logger.info(">>>>queryUserPicById>>>>>where"+whereSql+">>>>>>>paramMap:"+map.toString());
        List<TbPhotoPic> list=this.jdbcTemplate.query(sql.append(whereSql).toString(), map,new BeanPropertyRowMapper<TbPhotoPic>(TbPhotoPic.class));
        /*
        , new RowMapper<TbPhotoPic>() {
            @Override
            public TbPhotoPic mapRow(ResultSet rs, int rowNum) throws SQLException {
                TbPhotoPic entity = new TbPhotoPic();
                entity.setId(rs.getInt("id"));
                entity.setCreateTime(rs.getDate("create_time"));
                entity.setCreateUser(rs.getString("create_user"));
                entity.setStatus(rs.getString("status"));
                entity.setBaseUrl(rs.getString("base_url"));
                entity.setImgUrl(rs.getString("img_url"));
                entity.setMemberId(rs.getInt("member_id"));
                entity.setPhotoId(rs.getInt("photo_id"));
                entity.setSmallUrl(rs.getString("small_url"));
                return entity;
            }
        }
         */
        logger.info(">>>>queryUserPicById>>>>>resultList:"+list);
        return list;
    }

    /**
     * 查询用户相片  根据id
     * @param imgageDto 相片ids
     * @return
     */
    public PagingBean queryActivityImg(ActivityCalendarImagesDto imgageDto){
        logger.info(">>>>queryActivityImg>>>>>param:"+imgageDto);
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select   ID,  CODE,  ORG_CODE,  BASICS_ID,  SCHEDULE_ID,  STATE,  PROJECT_URL,  FILE_PATH_URL,  FILE_PATH,  IMG_COMPRESS,  FILE_NAME_URL,  SHOW_FILE_NAME,  to_char(UPLOAD_TIME,'yyyy-MM-dd') UPLOAD_TIME,  CREATE_DATE from ACTIVITY_CALENDAR_IMAGES p ");
        StringBuilder countSql=new StringBuilder("select count(*) from ACTIVITY_CALENDAR_IMAGES p ");
        StringBuilder whereSql=new StringBuilder(" where state=2 ");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("pageSize",imgageDto.getPageSize());
        map.put("start",imgageDto.getStart());
        map.put("basicsId",imgageDto.getBasicsId());
        whereSql.append(" and p.BASICS_ID = :basicsId ");
        /*if(!regUtil.isNull(imgageDto.getBasicsId())){
            map.put("basicsId",imgageDto.getBasicsId());
            whereSql.append(" and p.BASICS_ID = :basicsId ");
        }*/
        whereSql.append(" order by p.UPLOAD_TIME desc,id desc");
        logger.info(">>>>>>>>querysql:"+whereSql+"===map:"+map);
        int count= this.jdbcTemplate.queryForObject(countSql.append(whereSql).toString(),map,Integer.class);
        List<ActivityCalendarImagesDto> list=this.jdbcTemplate.query(pageSql(sql.append(whereSql).toString(),map), map, new BeanPropertyRowMapper<ActivityCalendarImagesDto>(ActivityCalendarImagesDto.class));
        PagingBean bean=new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryActivityImg>>>>>where"+whereSql+">>>>>>>result:"+bean.toString());
        return bean;
    }

}
