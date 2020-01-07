<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
    //************ 可以在这里做一些全局变量 ${sessionScope} *************//
    if("${sessionScope.shiro.ShiroUser.loginName}" == "" ){
        window.location.href = '/AmoskiWebUser/AMOSKI/loginNameUser';
    }
</script>