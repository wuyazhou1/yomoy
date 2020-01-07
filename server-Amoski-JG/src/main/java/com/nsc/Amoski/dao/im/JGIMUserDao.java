package com.nsc.Amoski.dao.im;

import com.nsc.Amoski.entity.TJGIMMemberEntity;

/**
 * @author 李阳
 * @date 2019/12/18 17:03
 */
public interface JGIMUserDao {

    long insert(TJGIMMemberEntity tjgimMemberEntity);

    long deleteById(TJGIMMemberEntity tjgimMemberEntity);

    TJGIMMemberEntity findByUsername(TJGIMMemberEntity tjgimMemberEntity);

    long byIdUpdatePassword(TJGIMMemberEntity tjgimMemberEntity);

    long byIdUpdateUidAndPassword(TJGIMMemberEntity tjgimMemberEntity);

}
