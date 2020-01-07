<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="math.jsp"%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>角色授权用户</title>
    <link rel="shortcut icon" href="/AmoskiWebUser/images/logo.ico">
    <link rel="stylesheet" type="text/css" href="/AmoskiWebUser/css/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="/AmoskiWebUser/css/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="/AmoskiWebUser/css/Hui-iconfont/1.0.8/iconfont.css" />
    <link href="/AmoskiWebUser/js/jquery/1.9.1/xzym.css" rel="stylesheet" type="text/css" />

    <link href="/AmoskiWebUser/js/jquery/1.9.1/multi-select.css" rel="stylesheet" type="text/css" />


    <script type="text/javascript" src="/AmoskiWebUser/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/jquery/1.9.1/ajaxhook.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/layer/3.0/layer.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/public.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/common.js"></script>

    <script type="text/javascript" src="/AmoskiWebUser/js/jquery/1.9.1/jquery.quicksearch.js"></script>

    <script type="text/javascript" src="/AmoskiWebUser/js/jquery/1.9.1/jquery.multi-select.js"></script>






    <style>
        .libackground{
            background:#d9edf7;
        }
        ul{
            height: 300px!important;
            text-align: left;
        }
    </style>
    <link href="/AmoskiWebUser/js/jquery/1.9.1/all.css" rel="stylesheet" type="text/css" />
    <script>
        $(function(){
            var userCode='<%=request.getParameter("code")%>';
            $("#userCode").val(userCode);
            $.ajax({
                url : "/AmoskiUser/UserManage/UserImpowerRole",
                type : "post",
                data : {'userCode':userCode},
                async : false,
                dataType : "json",
                success : function(data) {
                    for(var str in data){
                        if(data[str].SELECTED=='false'){
                            $("#RoleNoImpowerUser").append("<option value='"+data[str].CODE+"'>"+data[str].NAME+"</option>");
                        }else if(data[str].SELECTED=='true'){
                            $("#RoleNoImpowerUser").append("<option value='"+data[str].CODE+"' selected='selected'>"+data[str].NAME+"</option>");
                        }
                    }
                }
            });
            $('#RoleNoImpowerUser').multiSelect({
                selectableHeader: "<div style='background: #56A6E5;color: white;border-radius: 4px 4px 0px 0px;'>未授权角色</div>"+"<input type='text' class='search-input' autocomplete='off' placeholder='try \"12\"' style='width: 162px;'>",
                selectionHeader: "<div style='background: #56A6E5;color: white;border-radius: 4px 4px 0px 0px;'>已授权角色</div>"+"<input type='text' class='search-input' autocomplete='off' placeholder='try \"4\"' style='width: 162px;'>",
                afterInit: function(ms){
                    var that = this,
                        $selectableSearch = that.$selectableUl.prev(),
                        $selectionSearch = that.$selectionUl.prev(),
                        selectableSearchString = '#'+that.$container.attr('id')+' .ms-elem-selectable:not(.ms-selected)',
                        selectionSearchString = '#'+that.$container.attr('id')+' .ms-elem-selection.ms-selected';

                    that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
                        .on('keydown', function(e){
                            if (e.which === 40){
                                that.$selectableUl.focus();
                                return false;
                            }
                        });
                    that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
                        .on('keydown', function(e){
                            if (e.which == 40){
                                that.$selectionUl.focus();
                                return false;
                            }
                        });
                },
                afterSelect: function(){
                    this.qs1.cache();
                    this.qs2.cache();
                },
                afterDeselect: function(){
                    this.qs1.cache();
                    this.qs2.cache();
                }
            });
        });

        function saveRoleUserRel(){
            var roleCode="";
            if($("#RoleNoImpowerUser").val()!=null)
                for(var i=0;i<$("#RoleNoImpowerUser").val().length;i++){
                    if(i==0){
                        roleCode=roleCode+$("#RoleNoImpowerUser").val()[i];
                    }else{
                        roleCode=roleCode+","+$("#RoleNoImpowerUser").val()[i];
                    }
                }
            $.ajax({
                url : "/AmoskiUser/UserManage/setUserImpowerRole",
                type : "post",
                data : {
                    'roleCode':roleCode,
                    'userCode':$("#userCode").val()
                },
                async : false,
                cache : false,
                dataType : "json",
                beforeSend:function(){
                    layer_wait();
                },
                success : function(data) {
                    if(data.code!=null){
                        layer.msg(data.message, {
                            icon : 6,
                            time : 2000
                        },function(){
                            window.parent["CloseRoleImppowerUser"]();
                        });
                    }
                }
            });
        }
    </script>
    <style>
    </style>
</head>
<body class="window_body" scrolling='no' align="center">
<input type="hidden" id="userCode" value="">
<div style="padding-left: 116px;padding-top: 20px;">
    <select id="RoleNoImpowerUser" multiple='multiple'>
        <%--<c:forEach items="${noUser}" var="li">
        <option value="${li.ID}">${li.ROLENAME}</option>
        </c:forEach>
        <c:forEach items="${yesUsesr}" var="li">
        <option value="${li.ID}" selected="selected">${li.ROLENAME}</option>
        </c:forEach>--%>
    </select>

</div>
<br/><br/>
<input type="button" class="btn  search" onclick="saveRoleUserRel()" value="保存">
<input type="button" class="btn  qx" onclick="parent.closeWin()" value="关闭">
</body>
<script>
    if(!!window.ActiveXObject || "ActiveXObject" in window) {
        //此时是ie浏览器
        $(".search").css("margin-left","200px");
    } else {
    }
</script>
</html>