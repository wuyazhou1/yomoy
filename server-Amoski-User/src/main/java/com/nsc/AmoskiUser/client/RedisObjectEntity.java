package com.nsc.AmoskiUser.client;

import com.nsc.Amoski.entity.ShiroUser;
import lombok.Data;

import java.io.Serializable;

@Data
public class RedisObjectEntity  implements Serializable {
    private ShiroUser ShiroUser;
    private String md5Pwd;
    private String LoginName;
    private String name;
}
