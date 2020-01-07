package com.nsc.Amoski.dto;



/**
 *@Author: hf
 *@Desciption: 所有socket处理类型
 *@Date:14:00 2019/6/18
 *@param:No such property: code for class: Script1
 */
public enum SocketDealType {
    ///公用的socket处理类型
    JOINTEAM("1000","创建或加入队伍"),
    SENDPOINT("1001","发送队友点数据"),
    OFFLINE("1002","队友离线"),
    HEARTBEAT("1003","心跳"),
    LEAVETEAM("1004","队友离开队伍"),
    REJECTTEAM("1005","队友被踢出队伍"),
    GETTEAMINFO("1006","查询队伍信息"),
    UPDATETEAMINFO("1007","修改队伍信息"),
    DISMISSTEAM("1008","解散队伍"),
    UPDROLEINFO("1009","更新队伍角色信息"),
    MOVETEAMAUTHOR("1010","转移队伍权限"),
    DRIVERNIVI("1011","队伍导航"),
    CANCELDRIVERNIVI("1012","队伍取消导航"),

    LOG_DEFAULT_CLASSNAME("默认日志输出文件名", "CWA_Client");

    private String message;
    private String code ;

    SocketDealType(String code, String message) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
