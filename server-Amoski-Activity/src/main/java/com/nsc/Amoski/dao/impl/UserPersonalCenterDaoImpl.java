package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.UserPersonalCenterDao;
import com.nsc.Amoski.entity.DynamicCollectionEntity;
import com.nsc.Amoski.entity.DynamicImageEntity;
import com.nsc.Amoski.parent.*;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserPersonalCenterDaoImpl extends GenericRepositoryImpl implements UserPersonalCenterDao {

    /**
     * 查看动态总条数
     * @param dto
     * @return
     */
    @Override
    public Integer queryDynamicCount(SqlParameterSource dto) {
        return this.jdbcTemplate.queryForObject(
                "select count(1) from RELEASE_DYNAMIC where MEMBER_ID = :id and state in (1,2)",dto,Integer.class);
    }

    /**
     * 关注数量查询
     * @param dto
     * @return
     */
    @Override
    public Integer queryFollowCount(SqlParameterSource dto) {
        return this.jdbcTemplate.queryForObject(
                "select count(1) from FANS_ATTENTION where MEMBER_ID = :id ",dto,Integer.class);
    }

    /**
     * 粉丝数量
     * @param dto
     * @return
     */
    @Override
    public Integer queryFansCount(SqlParameterSource dto) {
        return this.jdbcTemplate.queryForObject(
                "select count(1) from FANS_ATTENTION where FANS_MEMBER_ID = :id ",
                dto,
                Integer.class);
    }

    /**
     * 查询点赞总数量
     * @param dto
     * @return
     */
    @Override
    public Integer queryFabulousCount(SqlParameterSource dto) {
        return this.jdbcTemplate.queryForObject(
                "select " +
                        "(select count(1) from DYNAMIC_SPOT_FABULOUS where MEMBER_ID = :id and nvl(COMMENT_ID,'-1')='-1')" +
                        //"+(select count(1) from COMMENT_SPOT_FABULOUS where MEMBER_ID = :id ) " +
                        "from dual ",dto,Integer.class);
    }

    /**
     * 查询动态列表
     * @param Parameter
     * @return
     */
    @Override
    public List queryDynamicList(SqlParameterSource Parameter) {
        return this.jdbcTemplate.query("select a.*,decode(a.type,'1','','2','','3',b.title,'4',d.title,'未知类型') type_Title," +
                        "decode(a.type,'1','','2','','3',c.FILE_NAME_URL,'4',d.BILL,'未知类型') type_Image," +
                        "(select count(x.id) from DYNAMIC_COMMENT x where x.DYNAMIC_ID=a.id) COMMENT_count, " +//app评论
                        "(select count(x.id) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id) COLLECTION_count, " +//app收藏
                        "(select count(x.id) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id) FABULOUS_count, " +
                        "(select count(1) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and x.member_id = :memberId) is_Like," +//是否点赞
                        "(select count(1) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id and x.member_id = :memberId) has_Collection," +//是否收藏
                        "decode(a.member_id, :memberId ,'2',(select count(1) from FANS_ATTENTION x where x.FANS_MEMBER_ID=a.member_id and x.member_id = :memberId)) followed" +//是否关注
                        " from (" +
                        "SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                        " select " +
                        " ID,PARENT_DYNAMIN_ID,RELATION_TYPE_ID,TYPE,MEMBER_ID,MEMBER_NAME,MEMBER_IMAGE_URL,RELEASE_ADDRESS,PUBLISH_CONTENT, " +
                        " Y_AXIS,X_AXIS,SCORE,STATE, to_char(CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,UPDATE_DATE " +
                        " from RELEASE_DYNAMIC a  where member_id = :memberId " +
                        " order by CREATE_DATE desc" +
                        ") q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength" +
                        ") a left join TB_ACTIVITY_BASICS b on a.RELATION_TYPE_ID=b.code" +
                        " left join TB_ACTIVITY_BILL_IMAGE c on b.id=c.BASICS_ID and c.TYPE=1" +
                        " left join amoski_riding.TB_RIDING_GUIDE_INFO d on a.RELATION_TYPE_ID=d.id",
                Parameter, new BeanPropertyRowMapper(ReleaseDynamicParent.class));
    }

    /**
     * 查询我关注列表
     * @param parameter
     * @return
     */
    @Override
    public List queryFollowList(SqlParameterSource parameter) {
        return this.jdbcTemplate.query("SELECT  w.*  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                " select a.*,nvl(b.name,b.tel) member_name,c.HEAD_IMG_PROJECT||c.HEAD_IMG_FILE member_image," +
                " (select count(1) from FANS_ATTENTION x where a.FANS_MEMBER_ID=x.member_id and a.member_id=x.FANS_MEMBER_ID) followed from FANS_ATTENTION a " +
                " left join amoski_user.t_member b on a.FANS_MEMBER_ID=b.id " +
                " left join amoski_user.t_member_dail c on c.member_id=b.id" +
                " where a.MEMBER_ID = :memberId " +
                " ) q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength",
                parameter, new BeanPropertyRowMapper(FansAttentionParent.class));
    }

    /**
     * 查询我的粉丝列表
     * @param parameter
     * @return
     */
    @Override
    public List queryFansList(SqlParameterSource parameter) {
        return this.jdbcTemplate.query("SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                        " select a.*,nvl(b.name,b.tel) member_name,c.HEAD_IMG_PROJECT||c.HEAD_IMG_FILE member_image," +
                        "decode(a.member_id, :memberId ,'2',(select count(1) from FANS_ATTENTION x where x.FANS_MEMBER_ID=a.member_id and x.member_id = :memberId)) followed " +
                        "from FANS_ATTENTION a " +
                        " left join amoski_user.t_member b on a.member_id=b.id " +
                        " left join amoski_user.t_member_dail c on c.member_id=b.id" +
                        " where a.FANS_MEMBER_ID = :memberId " +
                        " ) q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength",
                parameter, new BeanPropertyRowMapper(FansAttentionParent.class));
    }

    @Override
    public List queryFabulousList(SqlParameterSource parameter) {
        List list = new ArrayList();
        List<DynamicSpotFabulousParent> resultList = this.jdbcTemplate.query("SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                        " select a.* from DYNAMIC_SPOT_FABULOUS a " +
                        " left join RELEASE_DYNAMIC b on b.id = a.DYNAMIC_ID " +
                        " left join DYNAMIC_COMMENT c on a.COMMENT_ID=c.id" +
                        " where (a.member_id = :memberId  and nvl(a.COMMENT_ID,'-1')='-1')" +
                        //" or ( c.member_id= :memberId  and nvl(a.COMMENT_ID,'-1')!='-1')" +
                        " order by a.id desc"+
                        " ) q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength",
                parameter, new BeanPropertyRowMapper(DynamicSpotFabulousParent.class));
        logger.info("查询点赞总条数"+resultList.size()+"条");
        for(DynamicSpotFabulousParent DynamicSpotFabulousParent:resultList){
            if(StringUtils.isEmpty(DynamicSpotFabulousParent.getCommentId())){
                SqlParameterSource Parameter = new BeanPropertySqlParameterSource(DynamicSpotFabulousParent);
                List<ReleaseDynamicParent> query = this.jdbcTemplate.query(
                        "select a.*,decode(a.type,'1','','2','','3',b.title,'4',d.title,'未知类型') type_Title," +
                                "decode(a.type,'1','','2','','3',c.FILE_NAME_URL,'4',d.BILL,'未知类型') type_Image," +
                                "(select count(x.id) from DYNAMIC_COMMENT x where x.DYNAMIC_ID=a.id) COMMENT_count, " +//app评论
                                "(select count(x.id) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id) COLLECTION_count, " +//app收藏
                                "(select count(x.id) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id) FABULOUS_count, " +
                                "(select count(1) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and x.member_id = :memberId) is_Like," +//是否点赞
                                "(select count(1) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id and x.member_id = :memberId) has_Collection," +//是否收藏
                                "decode(a.member_id, :memberId ,'2',(select count(1) from FANS_ATTENTION x where x.FANS_MEMBER_ID=a.member_id and x.member_id = :memberId)) followed" +//是否关注
                                " from (" +
                                " select " +
                                " ID,PARENT_DYNAMIN_ID,RELATION_TYPE_ID,TYPE,MEMBER_ID,MEMBER_NAME,MEMBER_IMAGE_URL,RELEASE_ADDRESS,PUBLISH_CONTENT, \n" +
                                " Y_AXIS,X_AXIS,SCORE,STATE, to_char(CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,UPDATE_DATE \n" +
                                " from RELEASE_DYNAMIC a  where a.id = :dynamicId" +
                                ") a left join TB_ACTIVITY_BASICS b on a.RELATION_TYPE_ID=b.code" +
                                " left join TB_ACTIVITY_BILL_IMAGE c on b.id=c.BASICS_ID and c.TYPE=1" +
                                " left join amoski_riding.TB_RIDING_GUIDE_INFO d on a.RELATION_TYPE_ID=d.id",
                        Parameter, new BeanPropertyRowMapper(ReleaseDynamicParent.class));
                if(query!=null && query.size()>0){
                    logger.info("查询动态点赞"+DynamicSpotFabulousParent.getId()+"动态数据为==》"+query.get(0));
                    list.add(queryDynamic(query.get(0)));
                    DynamicSpotFabulousParent.setReleaseDynamicParent(queryDynamic(query.get(0)));
                }
            }/*else{
                SqlParameterSource parent = new BeanPropertySqlParameterSource(DynamicSpotFabulousParent);
                List<DynamicCommentParent> query = this.jdbcTemplate.query(
                                " select c.HEAD_IMG_PROJECT||c.HEAD_IMG_FILE member_images,nvl(b.name,b.tel) member_name,a.ID,a.DYNAMIC_ID,a.REPLY_COMMENT_ID," +
                                " a.COMMENT_CONTENT,a.MEMBER_ID,a.IS_READ," +
                                " to_char(a.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,a.REPLY_MEMBER_ID," +
                                " (select count(1) from DYNAMIC_SPOT_FABULOUS x where nvl(x.COMMENT_ID,'-1')=a.id) like_Count," +
                                " (select count(1) from DYNAMIC_SPOT_FABULOUS x where nvl(x.COMMENT_ID,'-1')=a.id and x.member_id = :memberId ) is_Like," +
                                " (select count(1) from DYNAMIC_COMMENT x where x.REPLY_COMMENT_ID=a.id) REPLY_COMMENT_Count," +
                                " e.HEAD_IMG_PROJECT||e.HEAD_IMG_FILE reply_member_images,nvl(d.name,d.tel) reply_member_name " +
                                " from DYNAMIC_COMMENT a " +
                                " left join amoski_user.t_member b on a.member_id=b.id " +
                                " left join amoski_user.t_member_dail c on b.id=c.member_id" +
                                " left join amoski_user.t_member d on a.reply_Member_Id=d.id" +
                                " left join amoski_user.t_member_dail e on d.id=e.member_id" +
                                " where a.id = :commentId  order by a.CREATE_DATE " ,
                        parent, new BeanPropertyRowMapper(DynamicCommentParent.class));
                logger.info("查询评论点赞"+DynamicSpotFabulousParent.getId()+"评论数据为==》"+query.get(0));
                DynamicSpotFabulousParent.setDynamicCommentParent(query.get(0));
            }*/
        }
        //点赞应ios请求改成值返回动态
        //return resultList;
        return list;
    }


    @Override
    public List queryFabulousListTo(SqlParameterSource parameter) {
        List list = new ArrayList();
        List<DynamicSpotFabulousParent> resultList = this.jdbcTemplate.query("SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                        " select a.* from DYNAMIC_SPOT_FABULOUS a " +
                        " left join RELEASE_DYNAMIC b on b.id = a.DYNAMIC_ID " +
                        " left join DYNAMIC_COMMENT c on a.COMMENT_ID=c.id" +
                        " where (a.member_id = :memberId  and nvl(a.COMMENT_ID,'-1')='-1')" +
                        //" or ( c.member_id= :memberId  and nvl(a.COMMENT_ID,'-1')!='-1')" +
                        " order by a.id desc"+
                        " ) q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength",
                parameter, new BeanPropertyRowMapper(DynamicSpotFabulousParent.class));
        logger.info("查询点赞总条数"+resultList.size()+"条");
        for(DynamicSpotFabulousParent DynamicSpotFabulousParent:resultList){
            if(StringUtils.isEmpty(DynamicSpotFabulousParent.getCommentId())){
                SqlParameterSource Parameter = new BeanPropertySqlParameterSource(DynamicSpotFabulousParent);
                List<ReleaseDynamicParent> query = this.jdbcTemplate.query(
                        "select a.*,decode(a.type,'1','','2','','3',b.title,'4',d.title,'未知类型') type_Title," +
                                "decode(a.type,'1','','2','','3',c.FILE_NAME_URL,'4',d.BILL,'未知类型') type_Image," +
                                "(select count(x.id) from DYNAMIC_COMMENT x where x.DYNAMIC_ID=a.id) COMMENT_count, " +//app评论
                                "(select count(x.id) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id) COLLECTION_count, " +//app收藏
                                "(select count(x.id) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id) FABULOUS_count, " +
                                "(select count(1) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and x.member_id = :memberId) is_Like," +//是否点赞
                                "(select count(1) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id and x.member_id = :memberId) has_Collection," +//是否收藏
                                "decode(a.member_id, :memberId ,'2',(select count(1) from FANS_ATTENTION x where x.FANS_MEMBER_ID=a.member_id and x.member_id = :memberId)) followed" +//是否关注
                                " from (" +
                                " select " +
                                " ID,PARENT_DYNAMIN_ID,RELATION_TYPE_ID,TYPE,MEMBER_ID,MEMBER_NAME,MEMBER_IMAGE_URL,RELEASE_ADDRESS,PUBLISH_CONTENT, \n" +
                                " Y_AXIS,X_AXIS,SCORE,STATE, to_char(CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,UPDATE_DATE \n" +
                                " from RELEASE_DYNAMIC a  where a.id = :dynamicId" +
                                ") a left join TB_ACTIVITY_BASICS b on a.RELATION_TYPE_ID=b.code" +
                                " left join TB_ACTIVITY_BILL_IMAGE c on b.id=c.BASICS_ID and c.TYPE=1" +
                                " left join amoski_riding.TB_RIDING_GUIDE_INFO d on a.RELATION_TYPE_ID=d.id",
                        Parameter, new BeanPropertyRowMapper(ReleaseDynamicParent.class));
                if(query!=null && query.size()>0){
                    logger.info("查询动态点赞"+DynamicSpotFabulousParent.getId()+"动态数据为==》"+query.get(0));
                    list.add(queryDynamic(query.get(0)));
                    DynamicSpotFabulousParent.setReleaseDynamicParent(queryDynamic(query.get(0)));
                }
            }/*else{
                SqlParameterSource parent = new BeanPropertySqlParameterSource(DynamicSpotFabulousParent);
                List<DynamicCommentParent> query = this.jdbcTemplate.query(
                                " select c.HEAD_IMG_PROJECT||c.HEAD_IMG_FILE member_images,nvl(b.name,b.tel) member_name,a.ID,a.DYNAMIC_ID,a.REPLY_COMMENT_ID," +
                                " a.COMMENT_CONTENT,a.MEMBER_ID,a.IS_READ," +
                                " to_char(a.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,a.REPLY_MEMBER_ID," +
                                " (select count(1) from DYNAMIC_SPOT_FABULOUS x where nvl(x.COMMENT_ID,'-1')=a.id) like_Count," +
                                " (select count(1) from DYNAMIC_SPOT_FABULOUS x where nvl(x.COMMENT_ID,'-1')=a.id and x.member_id = :memberId ) is_Like," +
                                " (select count(1) from DYNAMIC_COMMENT x where x.REPLY_COMMENT_ID=a.id) REPLY_COMMENT_Count," +
                                " e.HEAD_IMG_PROJECT||e.HEAD_IMG_FILE reply_member_images,nvl(d.name,d.tel) reply_member_name " +
                                " from DYNAMIC_COMMENT a " +
                                " left join amoski_user.t_member b on a.member_id=b.id " +
                                " left join amoski_user.t_member_dail c on b.id=c.member_id" +
                                " left join amoski_user.t_member d on a.reply_Member_Id=d.id" +
                                " left join amoski_user.t_member_dail e on d.id=e.member_id" +
                                " where a.id = :commentId  order by a.CREATE_DATE " ,
                        parent, new BeanPropertyRowMapper(DynamicCommentParent.class));
                logger.info("查询评论点赞"+DynamicSpotFabulousParent.getId()+"评论数据为==》"+query.get(0));
                DynamicSpotFabulousParent.setDynamicCommentParent(query.get(0));
            }*/
        }
        //点赞应ios请求改成值返回动态
        return resultList;
        //return list;
    }

    @Override
    public Object queryBePraisedCount(SqlParameterSource parameter) {
        Integer count = this.jdbcTemplate.queryForObject("select count(1) from DYNAMIC_SPOT_FABULOUS a " +
                        " left join RELEASE_DYNAMIC b on b.id = a.DYNAMIC_ID " +
                        " left join DYNAMIC_COMMENT c on a.COMMENT_ID=c.id" +
                        " where (b.member_id = :id  and nvl(a.COMMENT_ID,'-1')='-1')" +
                        " or ( c.member_id= :id  and nvl(a.COMMENT_ID,'-1')!='-1')",
                parameter,
                Integer.class);
        return count;
    }

    @Override
    public List<DynamicCollectionParent> dynamicCollectionList(SqlParameterSource parameter) {
        List resultList = new ArrayList();
        List<DynamicCollectionParent> list = this.jdbcTemplate.query("SELECT  w.*," +
                        " (select name from amoski_user.t_member x where w.member_id=x.id) member_name," +
                        " (select HEAD_IMG_FILE from amoski_user.T_MEMBER_DAIL x where x.MEMBER_ID=w.MEMBER_ID) MEMBER_IMAGE" +
                        "  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                        " select * from DYNAMIC_COLLECTION where member_id = :id" +
                        " order by id desc" +
                        " ) q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength",
                parameter,
                new BeanPropertyRowMapper(DynamicCollectionParent.class));
        for(DynamicCollectionParent DynamicCollectionParent:list){
            SqlParameterSource Parameter = new BeanPropertySqlParameterSource(DynamicCollectionParent);
            logger.info("查询动态收藏"+DynamicCollectionParent.getId());
            List<ReleaseDynamicParent> query = this.jdbcTemplate.query(
                    "select a.*,decode(a.type,'1','','2','','3',b.title,'4',d.title,'未知类型') type_Title," +
                            "decode(a.type,'1','','2','','3',c.FILE_NAME_URL,'4',d.BILL,'未知类型') type_Image," +
                            "(select count(x.id) from DYNAMIC_COMMENT x where x.DYNAMIC_ID=a.id) COMMENT_count, " +//app评论
                            "(select count(x.id) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id) COLLECTION_count, " +//app收藏
                            "(select count(x.id) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id) FABULOUS_count, " +
                            "(select count(1) from DYNAMIC_SPOT_FABULOUS x where x.DYNAMIC_ID=a.id and x.member_id = :memberId) is_Like," +//是否点赞
                            "(select count(1) from DYNAMIC_COLLECTION x where x.DYNAMIC_ID=a.id and x.member_id = :memberId) has_Collection," +//是否收藏
                            "decode(a.member_id, :memberId ,'2',(select count(1) from FANS_ATTENTION x where x.FANS_MEMBER_ID=a.member_id and x.member_id = :memberId)) followed" +//是否关注
                            " from (" +
                            " select " +
                            " ID,PARENT_DYNAMIN_ID,RELATION_TYPE_ID,TYPE,MEMBER_ID,MEMBER_NAME,MEMBER_IMAGE_URL,RELEASE_ADDRESS,PUBLISH_CONTENT, \n" +
                            " Y_AXIS,X_AXIS,SCORE,STATE, to_char(CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,UPDATE_DATE \n" +
                            " from RELEASE_DYNAMIC a  where a.id = :dynamicId" +
                            ") a left join TB_ACTIVITY_BASICS b on a.RELATION_TYPE_ID=b.code" +
                            " left join TB_ACTIVITY_BILL_IMAGE c on b.id=c.BASICS_ID and c.TYPE=1" +
                            " left join amoski_riding.TB_RIDING_GUIDE_INFO d on a.RELATION_TYPE_ID=d.id",
                    Parameter, new BeanPropertyRowMapper(ReleaseDynamicParent.class));
            if(query!=null && query.size()>0){
                logger.info("动态数据为==》"+query.get(0));
                resultList.add(queryDynamic(query.get(0)));
                DynamicCollectionParent.setReleaseDynamicParent(queryDynamic(query.get(0)));
            }
        }
        //收藏应ios请求改成值返回动态(后有因前段要求改回去)
        //return resultList;
        return list;
    }

    @Override
    public Integer dynamicCollectionCount(SqlParameterSource parameter) {
        Integer count = this.jdbcTemplate.queryForObject(
                "select count(1) from DYNAMIC_COLLECTION where member_id = :id",
                parameter,
                Integer.class);
        return count;
    }
    public ReleaseDynamicParent queryDynamic(ReleaseDynamicParent ReleaseDynamic){
        SqlParameterSource Parameters = new BeanPropertySqlParameterSource(ReleaseDynamic);
        logger.info("查询动态"+ReleaseDynamic.getId()+"id上级List");
        if(ReleaseDynamic.getParentDynaminId()!=null){
            List<ReleaseDynamicParent> queryRelease = this.jdbcTemplate.query(
                    "select * from RELEASE_DYNAMIC where id = :parentDynaminId ",
                    Parameters, new BeanPropertyRowMapper(ReleaseDynamicParent.class));
            if(queryRelease!=null&&queryRelease.size()>0){
                ReleaseDynamicParent releaseDynamicEntity = queryRelease.get(0);
                SqlParameterSource Paramet = new BeanPropertySqlParameterSource(releaseDynamicEntity);
                releaseDynamicEntity.setDynamicImageList(
                        this.jdbcTemplate.query("select * from DYNAMIC_IMAGE where dynamic_Id = :id ",
                                Paramet, new BeanPropertyRowMapper(DynamicImageEntity.class)));
                ReleaseDynamic.setParentDynamin(releaseDynamicEntity);
            }else{
                ReleaseDynamic.setParentDynaminId(null);
            }
        }
        logger.info("查询动态"+ReleaseDynamic.getId()+"id图片List");
        ReleaseDynamic.setDynamicImageList(
                this.jdbcTemplate.query("select * from DYNAMIC_IMAGE where dynamic_Id = :id ",
                        Parameters, new BeanPropertyRowMapper(DynamicImageEntity.class)));
        logger.info("查询动态"+ReleaseDynamic.getId()+"id点赞List");
        DynamicSpotFabulousParent DynamicSpotFabulousParent = new DynamicSpotFabulousParent();
        DynamicSpotFabulousParent.setDynamicId(ReleaseDynamic.getId());
        DynamicSpotFabulousParent.setPageSize("1");
        DynamicSpotFabulousParent.setLength("10");
        List<DynamicSpotFabulousParent> list = dynamicFabulousList(DynamicSpotFabulousParent);
        ReleaseDynamic.setDynamicSpotFabulousList(list);
        logger.info("查询动态点赞列表"+list.size());
        return ReleaseDynamic;
    }

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
}
