package com.nsc.Amoski.FeignClient;

import com.nsc.Amoski.entity.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "AmoskiWebHtmlUser")//
@Component
public interface UserServerApi {
    /*@RequestMapping(value = "Member/checkMember" , method = RequestMethod.POST)
    public boolean checkMember(String loginName,String password,String loginIdentification);*/

    /**
     * 查询部门信息
     * @param id
     * @param loginIdentification
     * @return
     */
    @RequestMapping(value = "DepartmentManage/getZtreeDatail" , method = RequestMethod.POST)
    Object queryUserDeptInfo(@RequestParam(name = "id") String id, @RequestParam(name = "loginIdentification") String loginIdentification);

    /**
     * 检查手机号是否已存在
     * @param tel
     * @param loginName
     * @return
     */
    @RequestMapping(value = "MemberWeiXinManage/checkedLoginRepeat" , method = RequestMethod.POST)
    Object checkedLoginRepeat(@RequestParam(name = "tel") String tel,@RequestParam(name = "loginName") String loginName);

    /**
     * 修改用户信息
     * @param userDto
     * @return
     */
    @RequestMapping(value = "MemberWeiXinManage/updateMemberView" , method =RequestMethod.POST)
    MemberView updateMemberView(@RequestBody MemberView userDto);

    /**
     * 电话号码注册
     * @param memberView
     * @return
     */
    @RequestMapping(value = "MemberWeiXinManage/TelRegister" , method = RequestMethod.POST)
    TMemberEntity TelRegister(@RequestBody MemberView memberView);

    /**
     * 微信注册
     * @param memberView
     * @return
     */
    @RequestMapping(value = "MemberWeiXinManage/WeiXinRegister" , method = RequestMethod.POST)
    MemberView WeiXinRegister(@RequestBody MemberView memberView);

    /**
     *
     * @param type 登入类型0账号手机登入，1微信登入
     * @param openId 微信唯一标识
     * @param loginName 登入会员名
     * @param password 登入密码
     * @return
     */
    @RequestMapping(value = "MemberWeiXinManage/MemberLogin" , method = RequestMethod.POST)
    MemberView MemberLogin(@RequestParam(name = "type") Integer type,@RequestParam(name = "openId") String openId,@RequestParam(name = "loginName") String loginName,@RequestParam(name = "password") String password);

    /**
     * 查询用户信息
     * @param openId
     * @param tel
     * @param loginName
     * @return
     */
    @RequestMapping(value = "MemberWeiXinManage/findMemberView" , method = RequestMethod.POST)
    MemberView findMemberView(@RequestParam(name = "openId") String openId ,@RequestParam(name = "tel") String tel, @RequestParam(name = "loginName") String loginName,@RequestParam(name = "id") String id);

    /**
     *查询数据字典
     * @param parentCode
     * @return
     */
    @RequestMapping(value = "Dict/GetDictZtree" , method = RequestMethod.POST)
    Object GetDictZtree(@RequestParam(name = "parentCode") String parentCode);

    /**
     * 更新会员地理位置经纬度
     * @param dto 会员视图类
     */
    @RequestMapping(value = "SameCity/updateMemberYXAxis" , method = RequestMethod.POST)
    Object updateMemberYXAxis(@RequestBody MemberView dto);

    @RequestMapping(value = "SameCity/queryMemberByYXAxis" , method = RequestMethod.POST)
    Object queryMemberByYXAxis(@RequestBody MemberView dto);

    @RequestMapping(value = "SameCity/queryMemberByYXAxisList" , method = RequestMethod.POST)
    Object queryMemberByYXAxisList(MemberView dto);

    @RequestMapping(value = "SameCity/queryMemberByMemberName" , method = RequestMethod.POST)
    PagingBean<MemberDto> queryMemberByMemberName(@RequestBody MemberView dto);

    @RequestMapping(value = "SameCity/queryMemberRanking" , method = RequestMethod.POST)
    List<MemberDto> queryMemberRanking(@RequestBody MemberView dto);


    @RequestMapping(value = "SameCity/saveBackgroundImages" , method = RequestMethod.POST)
    Object saveBackgroundImages(@RequestBody MemberView dto);

    /**
     * 根据俱乐部名称模糊查询
     *
     * @param orgName
     * @return
     */
    @RequestMapping(value = "/acitityDepartment/findByOrgName", method = RequestMethod.GET)
    Result findByOrgName(@RequestParam(value = "orgName") String orgName);

}
