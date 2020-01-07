package com.nsc.Amoski.dao.im.impl;

import com.nsc.Amoski.dao.im.JGIMResourceDao;
import com.nsc.Amoski.entity.TJGIMResourceEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author 李阳
 * @date 2019/12/19 16:46
 */
@Repository
public class JGIMResourceDaoImpl implements JGIMResourceDao {

    @Resource(name = "activityJdbcTemplate", type = JdbcTemplate.class)
    private JdbcTemplate activityJdbcTemplate;

    @Override
    public long insertImage(TJGIMResourceEntity tjgimResourceEntity) {
        return activityJdbcTemplate.update("INSERT INTO T_JGIM_RESOURCE(ID, MEMBER_ID, DATA_TYPE, MEDIA_ID, MEDIA_CRC32, WIDTH, HEIGHT, FORMAT, FSIZE, FILEPATH, FILENAME, CTIME) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp )",
                tjgimResourceEntity.getId(),
                tjgimResourceEntity.getMember_id(),
                tjgimResourceEntity.getData_type(),
                tjgimResourceEntity.getMedia_id(),
                tjgimResourceEntity.getMedia_crc32(),
                tjgimResourceEntity.getWidth(),
                tjgimResourceEntity.getHeight(),
                tjgimResourceEntity.getFormat(),
                tjgimResourceEntity.getFsize(),
                tjgimResourceEntity.getFilepath(),
                tjgimResourceEntity.getFilename()
        );
    }

}
