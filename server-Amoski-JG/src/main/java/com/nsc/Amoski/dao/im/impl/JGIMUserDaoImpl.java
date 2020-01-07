package com.nsc.Amoski.dao.im.impl;

import com.nsc.Amoski.dao.im.JGIMUserDao;
import com.nsc.Amoski.entity.TJGIMMemberEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 李阳
 * @date 2019/12/19 15:11
 */
@Repository
public class JGIMUserDaoImpl implements JGIMUserDao {

    @Resource(name = "userJdbcTemplate", type = JdbcTemplate.class)
    private JdbcTemplate userJdbcTemplate;

    @Override
    public long insert(TJGIMMemberEntity tjgimMemberEntity) {
        return userJdbcTemplate.update("INSERT INTO T_JGIM_MEMBER(ID, MEMBER_ID, USERNAME, PASSWORD, CTIME) VALUES(?,?,?,?,current_timestamp )", tjgimMemberEntity.getId(), tjgimMemberEntity.getMember_id(), tjgimMemberEntity.getUsername(), tjgimMemberEntity.getPassword());
    }

    @Override
    public long deleteById(TJGIMMemberEntity tjgimMemberEntity) {
        return userJdbcTemplate.update("DELETE FROM T_JGIM_MEMBER WHERE ID = ?", tjgimMemberEntity.getId());
    }

    @Override
    public TJGIMMemberEntity findByUsername(TJGIMMemberEntity tjgimMemberEntity) {
        List<TJGIMMemberEntity> query = userJdbcTemplate.query("SELECT * FROM T_JGIM_MEMBER WHERE USERNAME = ?", new BeanPropertyRowMapper<>(TJGIMMemberEntity.class), tjgimMemberEntity.getUsername());
        return query.isEmpty() ? null : query.get(0);
    }

    @Override
    public long byIdUpdatePassword(TJGIMMemberEntity tjgimMemberEntity) {
        return userJdbcTemplate.update("UPDATE T_JGIM_MEMBER SET PASSWORD = ? WHERE ID = ?", tjgimMemberEntity.getPassword(), tjgimMemberEntity.getId());
    }

    @Override
    public long byIdUpdateUidAndPassword(TJGIMMemberEntity tjgimMemberEntity) {
        return userJdbcTemplate.update("UPDATE T_JGIM_MEMBER SET MEMBER_ID = ?, PASSWORD = ? WHERE ID = ?", tjgimMemberEntity.getMember_id(), tjgimMemberEntity.getPassword(), tjgimMemberEntity.getId());
    }

}
