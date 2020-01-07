<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="math.jsp"%>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>AMOSKI系统管理</title>

    <link rel="shortcut icon" href="/AmoskiWebUser/images/logo.ico">
    <link rel="stylesheet" type="text/css" href="/AmoskiWebUser/css/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="/AmoskiWebUser/css/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="/AmoskiWebUser/css/Hui-iconfont/1.0.8/iconfont.css" />
    <link rel="stylesheet" href="/AmoskiWebUser/js/jquery/1.9.1/jquery.mCustomScrollbar.min.css" />
    <link rel="stylesheet" href="/AmoskiWebUser/js/layui/css/layui.css">

    <script type="text/javascript" src="/AmoskiWebUser/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/jquery/1.9.1/ajaxhook.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/jquery/1.9.1/jquery.mCustomScrollbar.concat.min.js"></script>
    <script type="text/javascript" src="/AmoskiWebHtmlUser/js/errorLogin.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/layer/3.0/layer.js"></script>
    <script src="/AmoskiWebUser/js/layui/layui.js"></script>

    <style>
        .pageSign{
            display: inline-block;
            width: 14px;
            height: 14px;
            margin: -2px 0px 0px -16px;
        }
        #layuiMCustomScrollbar a {
            display: inline !important;
        }
        #tabzu{
            border-style: none!important;
            box-shadow: none!important;
        }
    </style>
</head>
<body class="layui-layout-body">


<div class="layui-side layui-bg-black">
    <div class="layui-side-scroll" style="height: 95%;">
        <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
        <ul class="layui-nav layui-nav-tree" lay-filter="test" id="memus">




            <c:forEach items="${MENULIST}" var="a">
                <shiro:hasPermission name="menu${a.menu_code}">
                    ${a.start_menu}
                    <c:if test="${not empty a.next_menu && !(a.next_menu eq null) }">
                        <c:forEach items="${a.next_menu}" var="b">
                            <shiro:hasPermission name="menu${b.menu_code}">
                                ${b.start_menu}
                                <c:if test="${not empty b.next_menu && !(b.next_menu eq null) }">
                                    <c:forEach items="${b.next_menu}" var="c">
                                        <shiro:hasPermission name="menu${c.menu_code}">
                                            ${c.start_menu}
                                            <c:if test="${not empty c.next_menu && !(c.next_menu eq null) }">
                                                <c:forEach items="${c.next_menu}" var="d">
                                                    <shiro:hasPermission name="menu${d.menu_code}">
                                                        ${d.start_menu}
                                                        <c:if test="${not empty d.next_menu && !(d.next_menu eq null) }">
                                                            <c:forEach items="${d.next_menu}" var="e">
                                                                <shiro:hasPermission name="menu${e.menu_code}">
                                                                    ${e.start_menu}
                                                                    <c:if test="${not empty e.next_menu && !(e.next_menu eq null) }">

                                                                    </c:if>
                                                                    ${e.end_menu}
                                                                </shiro:hasPermission>
                                                            </c:forEach>
                                                        </c:if>
                                                        ${d.end_menu}
                                                    </shiro:hasPermission>
                                                </c:forEach>
                                            </c:if>
                                            ${c.end_menu}
                                        </shiro:hasPermission>
                                    </c:forEach>
                                </c:if>
                                ${b.end_menu}
                            </shiro:hasPermission>
                        </c:forEach>
                    </c:if>
                    ${a.end_menu}
                </shiro:hasPermission>
            </c:forEach>




            <%--<li class="layui-nav-item">
                <a class=""  href="javascript:;">系统管理</a>
                <dl class="layui-nav-child">
                    <dd><a href="javascript:;" onclick="showPageIframe(this)" data-href="/AmoskiWebUser/Department/DepartmentManagement" data-name="部门管理" data-nameMenu="bmgl">
                    <dd><a href="javascript:;" onclick="showPageIframe(this)" data-href="/AmoskiWebUser/AMOSKI/UserManageList" data-name="用户管理" data-nameMenu="yhgl">用户管理</a></dd>
                    <dd><a href="javascript:;" onclick="showPageIframe(this)" data-href="/AmoskiWebUser/MemberManage/MemberManage" data-name="会员管理" data-nameMenu="hygl" >会员管理</a></dd>
                    <dd><a href="javascript:;" onclick="showPageIframe(this)" data-href="/AmoskiWebUser/AMOSKI/RoleManageList" data-name="角色管理" data-nameMenu="jsgl">角色管理</a></dd>
                    <dd><a href="javascript:;" onclick="showPageIframe(this)" data-href="/AmoskiWebUser/AMOSKI/MenuManageList" data-name="菜单管理" data-nameMenu="cdgl">菜单管理</a></dd>
                    <dd><a href="javascript:;" onclick="showPageIframe(this)" data-href="/AmoskiWebUser/AMOSKI/DictTypeList" data-name="字典类型管理" data-nameMenu="zdlxgl">字典类型管理</a></dd>
                    <dd><a href="javascript:;" onclick="showPageIframe(this)" data-href="/AmoskiWebUser/AMOSKI/DictList" data-name="字典管理" data-nameMenu="zdgl">字典管理</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item">
                <a>活动管理</a>
                <dl class="layui-nav-child">
                    <dd>
                        &lt;%&ndash;<ul   lay-filter="test">&ndash;%&gt;
                            <li class="layui-nav-item">
                                <a class="" href="javascript:;">活动任务</a>
                                <dl class="layui-nav-child">

                                    <dd><a href="javascript:;">打卡任务</a></dd>
                                    <dd><a href="javascript:;">到点位置任务</a></dd>
                                    <dd><a href="javascript:;">轨迹任务</a></dd>
                                    <dd><a href="javascript:;">组合任务</a></dd>
                                </dl>
                            </li>
                        &lt;%&ndash;</ul>&ndash;%&gt;
                    </dd>
                    <dd><a href="javascript:;" onclick="showPageIframe(this)" data-href="/AmoskiWebActivity/layui/html/watermakeManage.html" data-name="水印管理" data-nameMenu="sygl">水印管理</a></dd>
                    <dd><a href="javascript:;" onclick="showPageIframe(this)" data-href="/AmoskiWebUser/" data-name="咨询管理" data-nameMenu="zxgl">咨询管理</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item">
                <a>商城管理</a>
                <dl class="layui-nav-child">
                    <dd><a href="javascript:;" onclick="showPageIframe(this)" data-href="/AmoskiWebUser/" data-name="库存管理" data-nameMenu="kzgl">库存管理</a></dd>
                    <dd><a href="javascript:;" onclick="showPageIframe(this)" data-href="/AmoskiWebUser/" data-name="货柜管理" data-nameMenu="hggl">货柜管理</a></dd>
                    <dd><a href="javascript:;" onclick="showPageIframe(this)" data-href="/AmoskiWebUser/" data-name="商品管理" data-nameMenu="spgl">商品管理</a></dd>
                    <dd><a href="javascript:;" onclick="showPageIframe(this)" data-href="/AmoskiWebUser/" data-name="订单管理" data-nameMenu="ddgl">订单管理</a></dd>
                    <dd>
                        <ul   lay-filter="test">
                            <li class="layui-nav-item">
                                <a class="" href="javascript:;">服务提醒管理</a>
                                <dl class="layui-nav-child">
                                    <dd><a href="javascript:;">售后服务提醒</a></dd>
                                    <dd><a href="javascript:;">节假日服务提醒</a></dd>
                                    <dd><a href="javascript:;">员工服务提醒</a></dd>
                                </dl>
                            </li>
                        </ul>
                    </dd>
                </dl>
            </li>--%>
        </ul>
    </div>
</div>
<div id="mCustomScrollbarMenu" class="layui-body">
    <!-- 动态选项卡 -->
    <div id="tabzu" class="layui-tab layui-tab-card layui-tab-brief" lay-filter="tabDemo" lay-allowclose="true">
        <ul class="layui-tab-title"></ul>
        <div class="layui-tab-content"></div>
    </div>
</div>
<script>
    var layer_index = null;
    function ShowLayerWait(type){
        if(typeof(type)=="undefined"){
            type=1;
        }
        layer_index = layer.load(type, {
            shade: [0.1,'#fff']
        });
    }
    function ShowLayerClose(){
        layer.close(layer_index);
    }
    function ShowLayerMsg(message){
        ShowLayerClose();
        //layer.alert(message, {icon: type,skin: 'layer-ext-moon',time:3000})
    }


    function windowLocationUrlLogin(){
        location.href="/AmoskiWebUser/AMOSKI/loginNameUser";
    }

    function adjustmentIframe(heightTab,widthTab){
        $(".layui-show iframe").css("height",(heightTab+50)+"px");
        $(".layui-show iframe").css("width",(widthTab)+"px");
    }

    function adjustmentIframe2(heightTab,widthTab){
        $(".layui-show iframe").css("height",heightTab);
        $(".layui-show iframe").css("width",widthTab);
    }

    function adjustmentIframe3(heightTab,widthTab){
        $(".layui-show iframe").css("height",(window.screen.height-70)+"px");
        $(".layui-show iframe").css("width",widthTab);
    }


    function showPageIframe(node){
        var dataAll = $(node).data();
        addTab(dataAll['name'],dataAll['href']);
    }

    layui.use('element', function() {
        function checkLastItem(arr, i) {
            return arr.length == (i + 1);
        }

        function getAhtml(obj){
            return "<a href=\"javascript:;\" onclick=\"addTab('" + obj.name + "','" + obj.url + "')\" >" + obj.name + "</a>";
        }
        element = layui.element;
    });

    //添加选项卡
    function addTab(name, url) {
        if(layui.jquery(".layui-tab-title li[lay-id='" + name + "']").length > 0) {
            //选项卡已经存在
            layui.element.tabChange('tabDemo', name);
            layer.msg('切换-' + name)
        } else {
            //动态控制iframe高度
            var tabheight = layui.jquery(window).height()+20;
            contentTxt = '<iframe src="' + url + '" scrolling="no" width="100%" height="' + (tabheight) + 'PX" style="border:0px" ></iframe>';
            //新增一个Tab项
            layui.element.tabAdd('tabDemo', {
                title: name,
                content: contentTxt,
                id: name
            })
            //切换刷新
            layui.element.tabChange('tabDemo', name)
        }
    }

    var nerisScrolling = function(){};
    $(function () {
        $("#mCustomScrollbarMenu").mCustomScrollbar({
            axis: "y",
            theme: "dark",
            autoHideScrollbar: true,
            scrollInertia:500,//å¹³æ»ææ
            mouseWheelPixels:100,//æ»å¨éåº¦
            callbacks:{
                whileScrolling:nerisScrolling
            },
            advanced:{
                updateOnImageLoad:false,
                autoExpandHorizontalScroll:true
            }
        });
    })
</script>
</body>
</html>