
<%@ page import="com.nsc.Amoski.entity.ShiroUser,org.apache.shiro.subject.Subject,java.text.DateFormat,org.apache.shiro.web.filter.authc.FormAuthenticationFilter,org.apache.shiro.SecurityUtils"%>
<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<html>
<head>
    <%
        //SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
        String error = (String) request.getParameter("username");
        String username = (String) request.getParameter("username");
        username = username == null ? "" : username;
    %>
    <link rel="shortcut icon" href="/AmoskiWebUser/images/logo.ico">
    <link rel="stylesheet" type="text/css" href="/AmoskiWebUser/css/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="/AmoskiWebUser/css/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="/AmoskiWebUser/css/Hui-iconfont/1.0.8/iconfont.css" />

    <script type="text/javascript" src="/AmoskiWebUser/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/layer/3.0/layer.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/public.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/common.js"></script>


    <script type="text/javascript" src="/AmoskiWebUser/js/datatables/1.10.15/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/jquery.validation/1.14.0/jquery.validate.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/jquery.validation/1.14.0/validate-methods.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/jquery.validation/1.14.0/messages_zh.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/laydate/laydate.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/jquery.form.js"></script>

    <title>Title</title>
    <style>
        label {
            float: inherit!important;
            font-size: 12px;
            width: 14%;
            display: inline-block;
        }
        input{
            border-radius:3px;
            border:1px solid #ccc
        }
        button{
            margin-top: 10px;
            background: #306fd0;
            border:none;
            color:#fff;
            font-size:12px;
            padding: 1.7% 3%;
            border-radius: 4px;
            width: 21%;
            margin-left: 15%;
        }
        .loginNameDiv{
            margin:0 auto;
            position:absolute;
            left:50%;
            top:50%;
            transform:translate(-50%,-50%);
            width: 20%;
            border:1px solid #ccc;
            padding:20px;
            background:#f2f2f2;
            border-radius:5px;
        }
        body{
            background-image: url('/AmoskiWebUser/images/loginNameImage.jpg');
            background-size: 100% 100%;
        }
    </style>


</head>
<body>
    <div style="position:relative;width:100%;height:100%;">
        <div style="">
            <div class="loginNameDiv">
                <div>
                    <label>登入名</label>
                    <input type="text" id="loginName" name="loginName"  value="<%=username%>" >
                </div>
                <div style="margin-top:5px;">
                    <label>密码</label>
                    <input type="password" id="password" name="password" >
                </div>
                <button onclick="submit()">登入</button>
            </div>
        </div>
    </div>
<script>
    function submit(){
        $.ajax({
            type: 'POST',
            dateType: 'JSON',
            data : {"loginName":$("#loginName").val(),
                    "password":$("#password").val()
            },
            url: "/AmoskiUser/AMOSKI/LoginUserCheck.html",
            beforeSend:function(){
                layer_wait();
            },
            success: function(json){
                location.href=json;
                //layer_close();
            },
            error: function(){
                layer_close();
            }
        });
    }
</script>

</body>
</html>

