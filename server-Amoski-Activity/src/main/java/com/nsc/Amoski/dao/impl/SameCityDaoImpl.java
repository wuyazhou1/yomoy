package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.SameCityDao;
import com.nsc.Amoski.entity.DynamicImageEntity;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.parent.DynamicCommentParent;
import com.nsc.Amoski.parent.DynamicSpotFabulousParent;
import com.nsc.Amoski.parent.ReleaseDynamicParent;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SameCityDaoImpl extends GenericRepositoryImpl implements SameCityDao {
    @Override
    public List getFabulousList(MemberView memberView) {
        SqlParameterSource parameter = new BeanPropertySqlParameterSource(memberView);
        List list = new ArrayList();
        List<DynamicSpotFabulousParent> resultList = this.jdbcTemplate.query(
                "SELECT  " +
                        " (select x.HEAD_IMG_FILE from amoski_user.t_member_dail x where x.member_id=w.member_id) member_Images," +
                        " (select x.name from amoski_user.t_member x where x.id=w.member_id) member_Name," +
                        " w.*  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                        " select a.* from DYNAMIC_SPOT_FABULOUS a " +
                        " left join RELEASE_DYNAMIC b on b.id = a.DYNAMIC_ID " +
                        " left join DYNAMIC_COMMENT c on a.COMMENT_ID=c.id" +
                        " where (b.member_id = :id  and nvl(a.COMMENT_ID,'-1')='-1')" +
                        " or ( c.member_id= :id  and nvl(a.COMMENT_ID,'-1')!='-1')" +
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
            }else{
                SqlParameterSource parent = new BeanPropertySqlParameterSource(DynamicSpotFabulousParent);
                List<DynamicCommentParent> query = this.jdbcTemplate.query(
                                " select c.HEAD_IMG_PROJECT||c.HEAD_IMG_FILE member_images,nvl(b.name,b.tel) member_name,a.ID," +
                                        "a.DYNAMIC_ID,a.REPLY_COMMENT_ID," +
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
            }
        }
        //点赞应ios请求改成值返回动态
        return resultList;
        //return list;
    }

    @Override
    public int getFabulousCount(MemberView memberView) {
        SqlParameterSource Parameters = new BeanPropertySqlParameterSource(memberView);
        Integer count = this.jdbcTemplate.queryForObject("select count(1) from DYNAMIC_SPOT_FABULOUS a " +
                " left join RELEASE_DYNAMIC b on b.id = a.DYNAMIC_ID " +
                " left join DYNAMIC_COMMENT c on a.COMMENT_ID=c.id" +
                " where (b.member_id = :id  and nvl(a.COMMENT_ID,'-1')='-1')" +
                " or ( c.member_id= :id  and nvl(a.COMMENT_ID,'-1')!='-1')",
                Parameters,
                Integer.class);
        return count;
    }

    @Override
    public Integer queryFabulousByMemberView(MemberView dto, MemberView memberView) {
        Map<String,Object> parentMap = new HashMap<>();
        parentMap.put("memberId",dto.getId());
        parentMap.put("fansMemberId",memberView.getId());
        Integer integer = this.jdbcTemplate.
                queryForObject("select count(1) from FANS_ATTENTION x " +
                        "where x.FANS_MEMBER_ID = :fansMemberId  and  x.member_id = :memberId",
                        parentMap,
                        Integer.class);
        return integer;
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
