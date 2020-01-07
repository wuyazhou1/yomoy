package com.nsc.Amoski.dao.impl;

import com.alibaba.fastjson.JSON;
import com.nsc.Amoski.dao.DynamicManageDao;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.parent.*;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DynamicManageDaoImpl extends GenericRepositoryImpl implements DynamicManageDao {
    Logger log = LoggerFactory.getLogger(DynamicManageDaoImpl.class);
    @Override
    public ReleaseDynamicEntity saveEntityByReleaseDynamicEntity(ReleaseDynamicEntity releaseDynamicEntity) {
        return this.saveEntity(releaseDynamicEntity);
    }

    @Override
    public void saveObject(DynamicCallParent dynamicCallMeEntity) {
        this.save(dynamicCallMeEntity);
    }

    @Override
    public List<ReleaseDynamicParent> queryDynamicList(ReleaseDynamicParent ReleaseDynamicParent, MemberView dto) {
        log.info("动态列表请求参数类型ReleaseDynamicParent==》"+ReleaseDynamicParent);
        log.info("动态列表请求参数类型MemberView==》"+dto);
        log.info("动态列表请求类型ReleaseDynamicParent.getType()==》"+ReleaseDynamicParent.getType());
        String type = ReleaseDynamicParent.getType().trim();
        ReleaseDynamicParent.InfoQueryParent();
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(ReleaseDynamicParent);
        List<ReleaseDynamicParent> resultList = null;
        if(type.equals("1")){//1关注  parametersTen会员id
            if(StringUtils.isEmpty(ReleaseDynamicParent.getMemberId())){
                log.info("会员id空值异常List");
                throw new RuntimeException("会员id为空List");
            }
            log.info(ReleaseDynamicParent.getMemberId()+"会员进入关注查询List");
            resultList = this.jdbcTemplate.query("select a.*,decode(a.type,'1','','2','','3',b.title,'4',d.title,'未知类型') type_Title," +
                            "decode(a.type,'1','','2','','3',c.FILE_NAME_URL,'4',d.BILL,'未知类型') type_Image," +
                            "(select name from amoski_user.t_member x where x.id=a.member_id) MEMBER_NAME," +
                            "(select HEAD_IMG_FILE from amoski_user.T_MEMBER_DAIL x where x.MEMBER_ID=a.MEMBER_ID) MEMBER_IMAGE_URL," +
                            "(select count(x.id) from DYNAMIC_COMMENT x where x.DYNAMIC_ID=a.id and nvl(x.REPLY_COMMENT_ID,'-1') = '-1') COMMENT_count, " +//app评论
                            "(select count(x.id) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id) COLLECTION_count, " +//app收藏
                            "(select count(x.id) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and nvl(x.COMMENT_ID,'-1')='-1') FABULOUS_count, " +
                            "(select count(1) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and x.member_id = :memberId and nvl(x.COMMENT_ID,'-1')='-1') is_Like," +//是否点赞
                            "(select count(1) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id and x.member_id = :memberId) has_Collection," +//是否收藏
                            "decode(a.member_id, :memberId ,'2',(select count(1) from FANS_ATTENTION x where x.FANS_MEMBER_ID=a.member_id and x.member_id = :memberId)) followed" +//是否关注
                            " from (" +
                            "SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                            " select " +
                            " ID,PARENT_DYNAMIN_ID,RELATION_TYPE_ID,TYPE,MEMBER_ID,RELEASE_ADDRESS,PUBLISH_CONTENT, " +
                            " Y_AXIS,X_AXIS,SCORE,STATE, to_char(CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,UPDATE_DATE " +
                            " from RELEASE_DYNAMIC a  where exists " +
                            " (select * from (select FANS_MEMBER_ID  from FANS_ATTENTION  where MEMBER_ID = :memberId ) b where a.MEMBER_ID=b.FANS_MEMBER_ID ) " +
                            " and (STATE = 1 or (MEMBER_ID = "+dto.getId()+" and STATE in (1,2)))" +
                            " and not exists (select * from DYNAMIC_shield x where x.shield_MEMBER_ID=a.member_id and x.member_id="+dto.getId()+" )"+
                            " order by CREATE_DATE desc" +
                            " ) q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength" +
                            " ) a left join TB_ACTIVITY_BASICS b on a.RELATION_TYPE_ID=b.code" +
                            " left join TB_ACTIVITY_BILL_IMAGE c on b.id=c.BASICS_ID and c.TYPE=1" +
                            " left join amoski_riding.TB_RIDING_GUIDE_INFO d on a.RELATION_TYPE_ID=d.id",
                    Parameter, new BeanPropertyRowMapper(ReleaseDynamicParent.class));
        }else if(type.equals("2")){//2热门
            log.info(ReleaseDynamicParent.getMemberId()+"会员进入热门查询List");
            resultList = this.jdbcTemplate.query("select a.*,decode(a.type,'1','','2','','3',b.title,'4',d.title,'未知类型') type_Title," +
                            "decode(a.type,'1','','2','','3',c.FILE_NAME_URL,'4',d.BILL,'未知类型') type_Image," +
                            "(select name from amoski_user.t_member x where x.id=a.member_id) MEMBER_NAME," +
                            "(select HEAD_IMG_FILE from amoski_user.T_MEMBER_DAIL x where x.MEMBER_ID=a.MEMBER_ID) MEMBER_IMAGE_URL," +
                            "(select count(x.id) from DYNAMIC_COMMENT x where x.DYNAMIC_ID=a.id and nvl(x.REPLY_COMMENT_ID,'-1') = '-1') COMMENT_count, " +//app评论
                            "(select count(x.id) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id) COLLECTION_count, " +//app收藏
                            "(select count(x.id) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and nvl(x.COMMENT_ID,'-1')='-1' ) FABULOUS_count, " +
                            "(select count(1) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and x.member_id = :memberId and nvl(x.COMMENT_ID,'-1')='-1') is_Like," +//是否点赞
                            "(select count(1) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id and x.member_id = :memberId) has_Collection," +//是否收藏
                            "decode(a.member_id, :memberId ,'2',(select count(1) from FANS_ATTENTION x where x.FANS_MEMBER_ID=a.member_id and x.member_id = :memberId)) followed" +//是否关注
                            " from (" +
                    "SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                            " select * from (" +
                            " select " +
                            " a.ID,a.PARENT_DYNAMIN_ID,a.RELATION_TYPE_ID,a.TYPE,a.MEMBER_ID,a.RELEASE_ADDRESS,a.PUBLISH_CONTENT," +
                            " a.Y_AXIS,a.X_AXIS,a.SCORE,a.STATE, to_char(a.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,a.UPDATE_DATE" +
                            " from RELEASE_DYNAMIC a where to_char(sysdate,'yyyy-MM-dd') >= to_char(a.CREATE_DATE,'yyyy-MM-dd')" +
                            " and to_char(sysdate-10,'yyyy-MM-dd') <= to_char(a.CREATE_DATE,'yyyy-MM-dd')" +
                            " and (STATE = 1 or (MEMBER_ID = "+dto.getId()+" and STATE in (1,2)))" +
                            " and not exists (select * from DYNAMIC_shield x where x.shield_MEMBER_ID=a.member_id and x.member_id="+dto.getId()+" )"+
                            " order by nvl(a.SCORE,'-1') desc)" +
                            " union all " +
                            " select * from (select " +
                            " b.ID,b.PARENT_DYNAMIN_ID,b.RELATION_TYPE_ID,b.TYPE,b.MEMBER_ID,b.RELEASE_ADDRESS,b.PUBLISH_CONTENT," +
                            " b.Y_AXIS,b.X_AXIS,b.SCORE,b.STATE, to_char(b.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,b.UPDATE_DATE" +
                            " from RELEASE_DYNAMIC b where to_char(sysdate-10,'yyyy-MM-dd') >= to_char(b.CREATE_DATE,'yyyy-MM-dd')" +
                            " and (STATE = 1 or (MEMBER_ID = "+dto.getId()+" and STATE in (1,2)))" +
                            " and not exists (select * from DYNAMIC_shield x where x.shield_MEMBER_ID=b.member_id and x.member_id="+dto.getId()+" )"+
                            " order by nvl(b.SCORE,'-1') desc) " +
                            ") q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength"+
                            ") a left join TB_ACTIVITY_BASICS b on a.RELATION_TYPE_ID=b.code" +
                            " left join TB_ACTIVITY_BILL_IMAGE c on b.id=c.BASICS_ID and c.TYPE=1" +
                            " left join amoski_riding.TB_RIDING_GUIDE_INFO d on a.RELATION_TYPE_ID=d.id",
                    Parameter, new BeanPropertyRowMapper(ReleaseDynamicParent.class));
        }else if(type.equals("3")){//3附近
            if(StringUtils.isEmpty(ReleaseDynamicParent.getYAxis()) || StringUtils.isEmpty(ReleaseDynamicParent.getXAxis())){
                log.info("附近查询经纬度异常List");
                throw new RuntimeException("附近查询经纬度异常List");
            }
            log.info(ReleaseDynamicParent.getMemberId()+"会员进入附近动态查询List");
            resultList = this.jdbcTemplate.query("select a.*,decode(a.type,'1','','2','','3',b.title,'4',d.title,'未知类型') type_Title," +
                            "decode(a.type,'1','','2','','3',c.FILE_NAME_URL,'4',d.BILL,'未知类型') type_Image," +
                            "(select name from amoski_user.t_member x where x.id=a.member_id) MEMBER_NAME," +
                            "(select HEAD_IMG_FILE from amoski_user.T_MEMBER_DAIL x where x.MEMBER_ID=a.MEMBER_ID) MEMBER_IMAGE_URL," +
                            "(select count(x.id) from DYNAMIC_COMMENT x where x.DYNAMIC_ID=a.id and nvl(x.REPLY_COMMENT_ID,'-1') = '-1') COMMENT_count, " +//app评论
                            "(select count(x.id) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id) COLLECTION_count, " +//app收藏
                            "(select count(x.id) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and  nvl(x.COMMENT_ID,'-1')='-1') FABULOUS_count, " +//app动态点赞总数
                            "(select count(1) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and x.member_id = :memberId  and nvl(x.COMMENT_ID,'-1')='-1') is_Like," +//是否点赞
                            "(select count(1) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id and x.member_id = :memberId) has_Collection," +//是否收藏
                            "decode(a.member_id, :memberId ,'2',(select count(1) from FANS_ATTENTION x where x.FANS_MEMBER_ID=a.member_id and x.member_id = :memberId)) followed" +//是否关注
                            " from (" +
                            " SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                            " select * from (" +
                            " select * from (select " +
                            " ID,PARENT_DYNAMIN_ID,RELATION_TYPE_ID,TYPE,MEMBER_ID,RELEASE_ADDRESS,PUBLISH_CONTENT," +
                            " Y_AXIS,X_AXIS,SCORE,STATE, to_char(CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,UPDATE_DATE," +
                            " SQRT( +" +
                            "     ((( :yAxis - Y_AXIS) * ACOS(-1) * 12656 *  " +
                            "     COS((( :yAxis + Y_AXIS) / 2) * ACOS(-1) / 180) / 180) *  " +
                            "     (( :yAxis - Y_AXIS) * ACOS(-1) * 12656 *  " +
                            "     COS((( :yAxis + Y_AXIS) / 2) * ACOS(-1) / 180) / 180)) +   " +
                            "     ((( :xAxis - X_AXIS) * ACOS(-1) * 12656 / 180) *  " +
                            "     (( :xAxis - X_AXIS) * ACOS(-1) * 12656 / 180))) sqrtValue" +
                            " from RELEASE_DYNAMIC a " +
                            " where " +
                            "  (STATE = 1 or (MEMBER_ID = "+dto.getId()+" and STATE in (1,2)))" +
                            " and not exists (select * from DYNAMIC_shield x where x.shield_MEMBER_ID=a.member_id and x.member_id="+dto.getId()+" )"+
                            " ) a" +
                            " WHERE ((nvl(SCORE,'-1') > 10 and to_char( ADD_MONTHS(sysdate, -1),'yyyy-MM-dd') > to_char(to_date(a.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss'),'yyyy-MM-dd'))" +
                            " or to_char( ADD_MONTHS(sysdate, -1),'yyyy-MM-dd') < to_char(to_date(a.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss'),'yyyy-MM-dd'))  and  sqrtValue < 50 "  +
                            " order by id desc) " +
                            " union all " +
                            " select * from (select * from (select " +
                            " ID,PARENT_DYNAMIN_ID,RELATION_TYPE_ID,TYPE,MEMBER_ID,RELEASE_ADDRESS,PUBLISH_CONTENT," +
                            " Y_AXIS,X_AXIS,SCORE,STATE, to_char(CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,UPDATE_DATE," +
                            " SQRT( +" +
                            "     ((( :yAxis - Y_AXIS) * ACOS(-1) * 12656 *  " +
                            "     COS((( :yAxis + Y_AXIS) / 2) * ACOS(-1) / 180) / 180) *  " +
                            "     (( :yAxis - Y_AXIS) * ACOS(-1) * 12656 *  " +
                            "     COS((( :yAxis + Y_AXIS) / 2) * ACOS(-1) / 180) / 180)) +   " +
                            "     ((( :xAxis - X_AXIS) * ACOS(-1) * 12656 / 180) *  " +
                            "     (( :xAxis - X_AXIS) * ACOS(-1) * 12656 / 180))) sqrtValue" +
                            " from RELEASE_DYNAMIC a \n" +
                            " where " +
                            "  (STATE = 1 or (MEMBER_ID = "+dto.getId()+" and STATE in (1,2)))" +
                            " and not exists (select * from DYNAMIC_shield x where x.shield_MEMBER_ID=a.member_id and x.member_id="+dto.getId()+" )"+
                            " ) a" +
                            " WHERE ((nvl(SCORE,'-1') > 10 and to_char( ADD_MONTHS(sysdate, -1),'yyyy-MM-dd') > to_char(to_date(a.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss'),'yyyy-MM-dd'))" +
                            " or to_char( ADD_MONTHS(sysdate, -1),'yyyy-MM-dd') < to_char(to_date(a.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss'),'yyyy-MM-dd'))  and  sqrtValue > 50 "  +
                            " order by id desc) " +
                            ") q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength"+
                            ") a left join TB_ACTIVITY_BASICS b on a.RELATION_TYPE_ID=b.code" +
                            " left join TB_ACTIVITY_BILL_IMAGE c on b.id=c.BASICS_ID and c.TYPE=1" +
                            " left join amoski_riding.TB_RIDING_GUIDE_INFO d on a.RELATION_TYPE_ID=d.id",
                    Parameter, new BeanPropertyRowMapper(ReleaseDynamicParent.class));
        }else if(type.equals("4")){//4自己发的动态
            if(StringUtils.isEmpty(ReleaseDynamicParent.getMemberId())){
                log.info("会员id空值异常List");
                throw new RuntimeException("会员id为空List");
            }
            log.info(ReleaseDynamicParent.getMemberId()+"会员进入自己发的动态List");
            resultList = this.jdbcTemplate.query("select a.*,decode(a.type,'1','','2','','3',b.title,'4',d.title,'未知类型') type_Title," +
                            "decode(a.type,'1','','2','','3',c.FILE_NAME_URL,'4',d.BILL,'未知类型') type_Image," +
                            "(select name from amoski_user.t_member x where x.id=a.member_id) MEMBER_NAME," +
                            "(select HEAD_IMG_FILE from amoski_user.T_MEMBER_DAIL x where x.MEMBER_ID=a.MEMBER_ID) MEMBER_IMAGE_URL," +
                            "(select count(x.id) from DYNAMIC_COMMENT x where x.DYNAMIC_ID=a.id and nvl(x.REPLY_COMMENT_ID,'-1') = '-1') COMMENT_count, " +//app评论
                            "(select count(x.id) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id) COLLECTION_count, " +//app收藏
                            "(select count(x.id) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and nvl(x.COMMENT_ID,'-1')='-1') FABULOUS_count, " +
                            "(select count(1) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and x.member_id = :memberId and nvl(x.COMMENT_ID,'-1')='-1') is_Like," +//是否点赞
                            "(select count(1) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id and x.member_id = :memberId) has_Collection," +//是否收藏
                            "decode(a.member_id, "+dto.getId()+" ,'2',(select count(1) from FANS_ATTENTION x where x.FANS_MEMBER_ID=a.member_id and x.member_id = '"+dto.getId()+"')) followed" +//是否关注
                            " from (" +
                            "SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                            " select " +
                            " ID,PARENT_DYNAMIN_ID,RELATION_TYPE_ID,TYPE,MEMBER_ID,RELEASE_ADDRESS,PUBLISH_CONTENT, " +
                            " Y_AXIS,X_AXIS,SCORE,STATE, to_char(CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,UPDATE_DATE " +
                            " from RELEASE_DYNAMIC a  where  a.MEMBER_ID = :memberId" +
                            " and STATE in (1,2) " +
                            " and not exists (select * from DYNAMIC_shield x where x.shield_MEMBER_ID=a.member_id and x.member_id="+dto.getId()+" )"+
                            " order by CREATE_DATE desc" +
                            " ) q  WHERE ROWNUM <= :endLength and  STATE = 1 or (MEMBER_ID = "+dto.getId()+" and STATE in (1,2))  ) w  WHERE w.rn > :startLength" +
                            " ) a left join TB_ACTIVITY_BASICS b on a.RELATION_TYPE_ID=b.code" +
                            " left join TB_ACTIVITY_BILL_IMAGE c on b.id=c.BASICS_ID and c.TYPE=1" +
                            " left join amoski_riding.TB_RIDING_GUIDE_INFO d on a.RELATION_TYPE_ID=d.id",
                    Parameter, new BeanPropertyRowMapper(ReleaseDynamicParent.class));
        }else if(type.equals("5")){//指定动态查询
            log.info(ReleaseDynamicParent.getMemberId()+"会员进入指定动态查询List");
            resultList = this.jdbcTemplate.query("select a.*,decode(a.type,'1','','2','','3',b.title,'4',d.title,'未知类型') type_Title," +
                            "decode(a.type,'1','','2','','3',c.FILE_NAME_URL,'4',d.BILL,'未知类型') type_Image," +
                            "(select name from amoski_user.t_member x where x.id=a.member_id) MEMBER_NAME," +
                            "(select HEAD_IMG_FILE from amoski_user.T_MEMBER_DAIL x where x.MEMBER_ID=a.MEMBER_ID) MEMBER_IMAGE_URL," +
                            "(select count(x.id) from DYNAMIC_COMMENT x where x.DYNAMIC_ID=a.id and nvl(x.REPLY_COMMENT_ID,'-1') = '-1') COMMENT_count, " +//app评论
                            "(select count(x.id) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id) COLLECTION_count, " +//app收藏
                            "(select count(x.id) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and nvl(x.COMMENT_ID,'-1')='-1') FABULOUS_count, " +
                            "(select count(1) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and x.member_id = :memberId and nvl(x.COMMENT_ID,'-1')='-1') is_Like," +//是否点赞
                            "(select count(1) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id and x.member_id = :memberId) has_Collection," +//是否收藏
                            "decode(a.member_id, :memberId ,'2',(select count(1) from FANS_ATTENTION x where x.FANS_MEMBER_ID=a.member_id and x.member_id = :memberId)) followed" +//是否关注
                            " from (" +
                            "SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                            " select " +
                            " ID,PARENT_DYNAMIN_ID,RELATION_TYPE_ID,TYPE,MEMBER_ID,RELEASE_ADDRESS,PUBLISH_CONTENT, " +
                            " Y_AXIS,X_AXIS,SCORE,STATE, to_char(CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,UPDATE_DATE " +
                            " from RELEASE_DYNAMIC a  where  a.id = :id" +
                            " order by CREATE_DATE desc" +
                            " ) q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength" +
                            " ) a left join TB_ACTIVITY_BASICS b on a.RELATION_TYPE_ID=b.code" +
                            " left join TB_ACTIVITY_BILL_IMAGE c on b.id=c.BASICS_ID and c.TYPE=1" +
                            " left join amoski_riding.TB_RIDING_GUIDE_INFO d on a.RELATION_TYPE_ID=d.id",
                    Parameter, new BeanPropertyRowMapper(ReleaseDynamicParent.class));
        }
        for(ReleaseDynamicParent ReleaseDynamic:resultList){
            SqlParameterSource Parameters = new BeanPropertySqlParameterSource(ReleaseDynamic);
            //log.info("查询动态"+ReleaseDynamic.getId()+"id上级List");
            if(ReleaseDynamic.getParentDynaminId()!=null){
                List<ReleaseDynamicParent> query = this.jdbcTemplate.query(
                        "select ID,PARENT_DYNAMIN_ID,RELATION_TYPE_ID,TYPE,MEMBER_ID," +
                                "(select name from amoski_user.t_member x where x.id=a.member_id) MEMBER_NAME," +
                                "(select HEAD_IMG_FILE from amoski_user.T_MEMBER_DAIL x where x.MEMBER_ID=a.MEMBER_ID) MEMBER_IMAGE_URL," +
                                "RELEASE_ADDRESS,PUBLISH_CONTENT,Y_AXIS,X_AXIS,SCORE,STATE,CREATE_DATE,UPDATE_DATE from RELEASE_DYNAMIC a where id = :parentDynaminId ",
                        Parameters, new BeanPropertyRowMapper(ReleaseDynamicParent.class));
                if(query!=null&&query.size()>0){
                    ReleaseDynamicParent releaseDynamicEntity = query.get(0);
                    SqlParameterSource Paramet = new BeanPropertySqlParameterSource(releaseDynamicEntity);
                    releaseDynamicEntity.setDynamicImageList(
                            this.jdbcTemplate.query("select * from DYNAMIC_IMAGE where dynamic_Id = :id ",
                                    Paramet, new BeanPropertyRowMapper(DynamicImageEntity.class)));
                    ReleaseDynamic.setParentDynamin(releaseDynamicEntity);
                }else{
                    ReleaseDynamic.setParentDynaminId(null);
                }
            }
            //log.info("查询动态"+ReleaseDynamic.getId()+"id图片List");
            ReleaseDynamic.setDynamicImageList(
                    this.jdbcTemplate.query("select * from DYNAMIC_IMAGE where dynamic_Id = :id ",
                            Parameters, new BeanPropertyRowMapper(DynamicImageEntity.class)));
            //log.info("查询动态"+ReleaseDynamic.getId()+"id点赞List");
            DynamicSpotFabulousParent DynamicSpotFabulousParent = new DynamicSpotFabulousParent();
            DynamicSpotFabulousParent.setDynamicId(ReleaseDynamic.getId());
            DynamicSpotFabulousParent.setPageSize("1");
            DynamicSpotFabulousParent.setLength("10");
            List<DynamicSpotFabulousParent> list = dynamicFabulousList(DynamicSpotFabulousParent);
            ReleaseDynamic.setDynamicSpotFabulousList(list);
            //log.info("查询动态点赞列表"+list.size());
        }
        return resultList;
    }

    @Override
    public int queryDynamicCount(ReleaseDynamicParent ReleaseDynamicParent, MemberView dto) {
        String type = ReleaseDynamicParent.getType().trim();
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(ReleaseDynamicParent);
        Integer count = null;
        if(type.equals("1")){//1关注  parametersTen会员id
            if(StringUtils.isEmpty(ReleaseDynamicParent.getMemberId())){
                log.info("会员id空值异常Count");
                throw new RuntimeException("会员id为空Count");
            }
            log.info(ReleaseDynamicParent.getMemberId()+"会员进入关注查询Count");
            count = this.jdbcTemplate.queryForObject(
                            "select count(1) from RELEASE_DYNAMIC a where exists " +
                            "(select * from (select MEMBER_ID from FANS_ATTENTION  where FANS_MEMBER_ID = :memberId ) b where a.MEMBER_ID=b.MEMBER_ID ) "+
                                    " and STATE = 1 " ,
                    Parameter, Integer.class);
        }else if(type.equals("2")){//2热门
            log.info(ReleaseDynamicParent.getMemberId()+"会员进入热门查询Count");
            count = this.jdbcTemplate.queryForObject(
                            "select count(1) from RELEASE_DYNAMIC a where  STATE = 1   order by nvl(SCORE,'-1') desc ",
                    Parameter, Integer.class);
        }else if(type.equals("3")){//3附近
            if(StringUtils.isEmpty(ReleaseDynamicParent.getYAxis()) || StringUtils.isEmpty(ReleaseDynamicParent.getXAxis())){
                log.info("附近查询经纬度异常Count");
                throw new RuntimeException("附近查询经纬度异常Count");
            }
            log.info(ReleaseDynamicParent.getMemberId()+"会员进入动态附近查询Count");
            count = this.jdbcTemplate.queryForObject(
                            "SELECT count(1) " +
                            "  FROM RELEASE_DYNAMIC a" +
                            "  WHERE (nvl(SCORE,'-1') > 10  " +
                                    " and to_char( ADD_MONTHS(sysdate, -1),'yyyy-MM-dd') > to_char(a.CREATE_DATE,'yyyy-MM-dd'))  " +
                                    " or to_char( ADD_MONTHS(sysdate, -1),'yyyy-MM-dd') < to_char(a.CREATE_DATE,'yyyy-MM-dd')   " +
                                    " and  STATE = 1 " ,
                    Parameter, Integer.class);
        }else if(type.equals("4")){//自己发布的动态
            log.info(ReleaseDynamicParent.getMemberId()+"会员进入自己发布的动态Count");
            count = this.jdbcTemplate.queryForObject(
                    " select count(1) from RELEASE_DYNAMIC a  " +
                            "where  a.MEMBER_ID = :memberId" +
                            " and STATE in (1,2)" ,
                    Parameter, Integer.class);
        }else if(type.equals("5")){//指定id查询
            log.info(ReleaseDynamicParent.getMemberId()+"会员进入动态指定id查询Count");
            count = this.jdbcTemplate.queryForObject(
                    " select count(1) from RELEASE_DYNAMIC a  " +
                            "where  a.id = :id" ,
                    Parameter, Integer.class);
        }
        return count;
    }

    @Override
    public void insertSqlUpdate(String sql, Object releaseDynamicParent) {
        //SqlParameterSource Parameter = new BeanPropertySqlParameterSource(releaseDynamicParent);
        Map<String,Object> parentMap = new HashMap<>();
        parentMap = (Map<String,Object>) JSON.parseObject(JSON.toJSONString(releaseDynamicParent));
        this.jdbcTemplate.update(sql,parentMap);
    }

    @Override
    public void saveDynamicImageList(List<DynamicImageEntity> dynamicImageList,String dynamicId) {
        SqlParameterSource[] Parameter = SqlParameterSourceUtils.createBatch(dynamicImageList.toArray());
        jdbcTemplate.batchUpdate("insert into DYNAMIC_IMAGE(ID,DYNAMIC_ID,PROJECT_URL,FILE_PATH_URL,FILE_PATH,IMG_COMPRESS,FILE_NAME_URL,width,height) " +
                "values(DYNAMIC_IMAGE_SEQUENCE.nextval,'"+dynamicId+"', :projectUrl , :filePathUrl , :filePath , :imgCompress , :fileNameUrl ,:width , :height )",
                Parameter);
    }

    @Override
    public void savePhotoPic(String sql, List<DynamicImageEntity> dynamicImageList) {
        SqlParameterSource[] Parameter = SqlParameterSourceUtils.createBatch(dynamicImageList.toArray());
        jdbcTemplate.batchUpdate(sql,Parameter);
    }

    @Override
    public void dynamicComment(DynamicCommentEntity dynamicCommentEntity) {
        saveEntity(dynamicCommentEntity);
    }

    @Override
    public void bonusPointsDynamic(String dynamicId, String bonusPoints) {
        Map<String,String> parentMap = new HashMap<>();
        parentMap.put("dynamicId",dynamicId);
        parentMap.put("bonusPoints",bonusPoints);
        this.jdbcTemplate.update("update RELEASE_DYNAMIC set score = nvl(score,0) + :bonusPoints where id = :dynamicId",parentMap);
    }

    @Override
    public void dynamicFabulous(Object DynamicSpotFabulousEntity) {
        save(DynamicSpotFabulousEntity);
    }

    @Override
    public Integer queryObjectByEntityById(String s, Object dynamicSpotFabulousParent) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(dynamicSpotFabulousParent);
        return this.jdbcTemplate.queryForObject(s,Parameter,Integer.class);
    }

    @Override
    public void dynamicCollection(DynamicCollectionEntity dynamicCollectionEntity) {
        save(dynamicCollectionEntity);
    }

    @Override
    public void dynamicFansAttention(FansAttentionEntity fansAttentionEntity) {
        save(fansAttentionEntity);
    }

    @Override
    public List dynamicFollowList(FansAttentionParent FansAttentionParent) {
        FansAttentionParent.InfoQueryParent();
        StringBuffer whereSql = new StringBuffer("");
        if(!StringUtils.isEmpty(FansAttentionParent.getMemberName())){
            whereSql.append(" and  a.FANS_MEMBER_ID in (select id from amoski_user.t_member where name like '%'|| :memberName ||'%' or tel like '%'|| :memberName ||'%')");
        }
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(FansAttentionParent);
        List<FansAttentionParent> resultList = null;
        resultList = this.jdbcTemplate.query("SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                        " select a.*,b.name member_name,c.HEAD_IMG_PROJECT||c.HEAD_IMG_FILE member_image from FANS_ATTENTION a " +
                        " left join amoski_user.t_member b on a.FANS_MEMBER_ID=b.id " +
                        " left join amoski_user.t_member_dail c on c.member_id=b.id" +
                        " where a.MEMBER_ID = :memberId " +whereSql.toString() +
                        " order by a.id desc ) q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength",
                Parameter, new BeanPropertyRowMapper(FansAttentionParent.class));
        return resultList;
    }

    @Override
    public int dynamicFollowCount(FansAttentionParent FansAttentionParent) {
        StringBuffer whereSql = new StringBuffer("");
        if(!StringUtils.isEmpty(FansAttentionParent.getMemberName())){
            whereSql.append(" and  a.FANS_MEMBER_ID in (select id from amoski_user.t_member where name like '%'|| :memberName ||'%' or  tel like '%'|| :memberName ||'%')");
        }
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(FansAttentionParent);
        Integer count = this.jdbcTemplate.queryForObject(
                "select count(1) from FANS_ATTENTION a where a.MEMBER_ID = :memberId "+whereSql,Parameter,Integer.class);
        return count;
    }

    @Override
    public Integer updateObjectByEntityById(String sql, Object obj) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(obj);
        return this.jdbcTemplate.update(sql,Parameter);
    }

    @Override
    public List dynamicFabulousList(DynamicSpotFabulousParent dynamicSpotFabulousParent) {
        dynamicSpotFabulousParent.InfoQueryParent();
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(dynamicSpotFabulousParent);
        List<DynamicSpotFabulousParent> resultList = null;
        resultList = this.jdbcTemplate.query("SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                        " select c.HEAD_IMG_FILE member_images,b.name member_name,a.* " +
                        " from DYNAMIC_SPOT_FABULOUS a " +
                        " left join amoski_user.t_member b on a.member_id=b.id " +
                        " left join amoski_user.t_member_dail c on b.id=c.member_id " +
                        " where a.dynamic_id = :dynamicId  and  nvl(a.COMMENT_ID,'-1')='-1'  order by a.CREATE_DATE" +
                        " ) q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength",
                Parameter, new BeanPropertyRowMapper(DynamicSpotFabulousParent.class));
        return resultList;
    }

    @Override
    public int dynamicFabulousCount(DynamicSpotFabulousParent dynamicSpotFabulousParent) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(dynamicSpotFabulousParent);
        Integer count = this.jdbcTemplate.queryForObject(
                " select count(1) from DYNAMIC_SPOT_FABULOUS a " +
                        " where a.dynamic_id = :dynamicId  and  nvl(a.COMMENT_ID,'-1')='-1'",Parameter,Integer.class);
        return count;
    }

    @Override
    public List dynamicCommentList(DynamicCommentParent dynamicCommentParent) {
        dynamicCommentParent.InfoQueryParent();
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(dynamicCommentParent);
        List<DynamicCommentParent> resultList = null;
        StringBuffer whereSql = new StringBuffer("");
        if(!StringUtils.isEmpty(dynamicCommentParent.getId())){
            whereSql.append(" and a.REPLY_COMMENT_ID = :id");
        }else{
            whereSql.append(" and nvl(a.REPLY_COMMENT_ID,'-1') = '-1'");
        }
        resultList = this.jdbcTemplate.query("SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                        " select c.HEAD_IMG_FILE member_images,nvl(b.name,b.tel) member_name,a.ID,a.DYNAMIC_ID,a.REPLY_COMMENT_ID," +
                        " a.COMMENT_CONTENT,a.MEMBER_ID,a.IS_READ," +
                        " to_char(a.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,a.REPLY_MEMBER_ID," +
                        " (select count(1) from DYNAMIC_SPOT_FABULOUS x where nvl(x.COMMENT_ID,'-1')=a.id) like_Count," +
                        " (select count(1) from DYNAMIC_SPOT_FABULOUS x where nvl(x.COMMENT_ID,'-1')=a.id and x.member_id = :memberId) is_Like," +
                        " (select count(1) from DYNAMIC_COMMENT x where x.REPLY_COMMENT_ID=a.id) REPLY_COMMENT_Count," +
                        " e.HEAD_IMG_PROJECT||e.HEAD_IMG_FILE reply_member_images,d.name reply_member_name " +
                        " from DYNAMIC_COMMENT a " +
                        " left join amoski_user.t_member b on a.member_id=b.id " +
                        " left join amoski_user.t_member_dail c on b.id=c.member_id" +
                        " left join amoski_user.t_member d on a.reply_Member_Id=d.id" +
                        " left join amoski_user.t_member_dail e on d.id=e.member_id" +
                        " where a.dynamic_id = :dynamicId  "+whereSql+"  order by a.CREATE_DATE " +
                        " ) q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength",
                Parameter, new BeanPropertyRowMapper(DynamicCommentParent.class));
        if(StringUtils.isEmpty(dynamicCommentParent.getId())){
            for(DynamicCommentParent DynamicComment:resultList){
                if(!StringUtils.isEmpty(DynamicComment.getReplyCommentCount())&&Integer.parseInt(DynamicComment.getReplyCommentCount())>0){
                    SqlParameterSource Paramet = new BeanPropertySqlParameterSource(DynamicComment);
                    DynamicComment.setDynamicCommentList(this.jdbcTemplate.query("SELECT q.*, ROWNUM AS rn FROM (" +
                                    " select c.HEAD_IMG_FILE member_images,nvl(b.name,b.tel) member_name," +
                                    " a.ID,a.DYNAMIC_ID,a.REPLY_COMMENT_ID," +
                                    " a.COMMENT_CONTENT,a.MEMBER_ID,a.IS_READ," +
                                    " to_char(a.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,a.REPLY_MEMBER_ID," +
                                    " (select count(1) from DYNAMIC_SPOT_FABULOUS x where nvl(x.COMMENT_ID,'-1')=a.id) like_Count," +
                                    " (select count(1) from DYNAMIC_SPOT_FABULOUS x where nvl(x.COMMENT_ID,'-1')=a.id and x.member_id = :memberId) is_Like," +
                                    " (select count(1) from DYNAMIC_COMMENT x where x.REPLY_COMMENT_ID=a.id) REPLY_COMMENT_Count," +
                                    " e.HEAD_IMG_PROJECT||e.HEAD_IMG_FILE reply_member_images,d.name reply_member_name" +
                                    " from DYNAMIC_COMMENT a " +
                                    " left join amoski_user.t_member b on a.member_id=b.id " +
                                    " left join amoski_user.t_member_dail c on b.id=c.member_id " +
                                    " left join amoski_user.t_member d on a.reply_Member_Id=d.id " +
                                    " left join amoski_user.t_member_dail e on d.id=e.member_id " +
                                    " where a.dynamic_id = :dynamicId   and a.REPLY_COMMENT_ID = :id  order by a.CREATE_DATE " +
                                    " ) q  WHERE ROWNUM <= 5 ",
                            Paramet, new BeanPropertyRowMapper(DynamicCommentParent.class)));
                }
            }
        }
        return resultList;
    }

    @Override
    public int dynamicCommentCount(DynamicCommentParent dynamicCommentParent) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(dynamicCommentParent);
        StringBuffer whereSql = new StringBuffer("");
        if(!StringUtils.isEmpty(dynamicCommentParent.getId())){
            whereSql.append(" and a.REPLY_COMMENT_ID = :id");
        }else{
            whereSql.append(" and nvl(a.REPLY_COMMENT_ID,'-1') = '-1'");
        }
        Integer count = this.jdbcTemplate.queryForObject(
                " select count(1) from DYNAMIC_COMMENT a " +
                        " where a.dynamic_id = :dynamicId "+whereSql,Parameter,Integer.class);
        return count;
    }

    @Override
    public void saveObjectEntity(Object obj) {
        save(obj);
    }

    @Override
    public Object queryObjectByEntityByIdResult(String s, Object o) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(o);
        return this.jdbcTemplate.queryForList(s,Parameter);
    }

    @Override
    public void deleteDynamic(ReleaseDynamicParent releaseDynamicParent) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(releaseDynamicParent);
        this.jdbcTemplate.update("update RELEASE_DYNAMIC set state=3 where id = :id and member_id = :memberId",Parameter);
    }

    @Override
    public void dynamicShieldMember(DynamicShieldEntity dynamicShieldEntity) {
        this.save(dynamicShieldEntity);
    }
}
