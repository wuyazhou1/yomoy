<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <%@ include file="math.jsp"%>
    <title>菜单管理</title>
    <link rel="shortcut icon" href="/AmoskiWebUser/images/logo.ico">
    <link rel="stylesheet" type="text/css" href="/AmoskiWebUser/css/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="/AmoskiWebUser/css/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="/AmoskiWebUser/css/Hui-iconfont/1.0.8/iconfont.css" />

    <script type="text/javascript" src="/AmoskiWebUser/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/jquery/1.9.1/ajaxhook.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/layer/3.0/layer.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/public.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/common.js"></script>


    <script type="text/javascript" src="/AmoskiWebUser/js/datatables/1.10.15/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/jquery.validation/1.14.0/jquery.validate.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/jquery.validation/1.14.0/validate-methods.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/jquery.validation/1.14.0/messages_zh.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/laydate/laydate.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/jquery.form.js"></script>


    <script type="text/javascript" src="/AmoskiWebUser/js/ztree/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/ztree/js/jquery.ztree.exhide-3.5.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/ztree/js/jquery.ztree.excheck-3.5.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/ztree/js/jquery.ztree.exedit-3.5.js"></script>
    <%--<script type="text/javascript" src="/AmoskiWebUser/js/ztree/js/ztreev2.js"></script>--%>
    <link href="/AmoskiWebUser/js/ztree/css/zTreeStyle/ztree.css" rel="stylesheet" type="text/css" />
    <link href="/AmoskiWebUser/js/ztree/css/zTreeStyle/hpzTree.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="/AmoskiWebUser/js/wjs/addMenu.js"></script>
    <link href="/AmoskiWebUser/css/wcss/bootAll.css" rel="stylesheet" type="text/css" />
</head>
<script>
    var Resource = null;
    var resourceData = null;
    function ShouAddResource(node,data){
        resourceData = data;
        Resource=layer.open({
            type: 2,
            title: node.title,
            shadeClose: true,
            fix: false, //不固定
            hade:0.4,
            maxmin: true, //开启最大化最小化按钮
            area: ['70%', '90%'],
            content: "/AmoskiWebUser/AMOSKI/AddResourceManage?source="+node.meath+"&meath=CloseAddResource&getDate=getResourceWindow&id="+table.row('.selected').data().CODE
            //content: "/AmoskiWebUser/jsp/addDepartment?source="+node.meath+"&meath=CloseAddMemberCoupon&getDate=getMemberCoupon"
        });
    }
    function getResourceWindow(){
        return resourceData;
    }
    function CloseAddResource(data){
        $("#resourceManage").attr("src","/AmoskiWebUser/AMOSKI/ResourceManageList?menuCode="+table.row('.selected').data().CODE);
        layer.close(Resource);
    }

</script>

<body style="height: 100%;">
<ul id="treeDemoImm" class="ztree" style="margin: 10px 0px 0px 0px;float: left;height: 90%;border: 1px solid #5ca1dc;overflow-y: auto;width: 15%;" ></ul>
<div class="page-container" style="width: 83%;float: left;margin: 0px -100px 0px -10px;padding-top:0px;">


    <div class="cl pd-5 bg-1 bk-gray mt-10" >
        菜单名称：
        <input type="text" class="input-text" style="width: 180px"  id="menuName" name="menuName" maxlength="50">&nbsp;&nbsp;&nbsp;

        <shiro:hasPermission name="res19">
        <a class="btn btn-success radius" id="btnSearch">
            <i class="Hui-iconfont">&#xe665;</i>
            查询
        </a>
        </shiro:hasPermission>
    </div>

    <div class="cl pd-5 bg-1 bk-gray mt-10" >
				<span class="l">
                    <shiro:hasPermission name="res21">
					<a class="btn btn-secondary radius" onclick="add()">
						<i class="Hui-iconfont">&#xe600;</i>
						新增菜单
					</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="res22">
					<a class="btn btn-primary radius" onclick="edit()">
						<i class="Hui-iconfont">&#xe6df;</i>
						修改菜单
					</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="res23">
					<a class="btn btn-danger radius" onclick="removeMemberAll()">
						<i class="Hui-iconfont">&#xe6e2;</i>
						删除菜单
					</a>
                    </shiro:hasPermission>

				</span>
                <shiro:hasPermission name="res20">
                <span class="r">
					<a class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" href="javascript:location.replace(location.href);" title="刷新">
						<i class="Hui-iconfont">&#xe68f;</i>
					</a>
				</span>
                </shiro:hasPermission>
    </div>

    <div class="mt-10"  >
        <table id="example" class="table table-border table-bordered table-bg table-hover table-sort" >
            <thead>
            <tr class="text-c">
                <th width="25">
                    <input type="checkbox" name="checkAll" class="checkall" onclick="checkedClean();">
                </th>
                <th width="">菜单名称</th>
                <th width="">上级菜单</th>
                <th width="">菜单路径</th>
                <th width="">菜单代码</th>
                <th width="">排序编号</th>
                <th width="">创建人</th>
                <th width="">创建时间</th>
            </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
        <iframe  id="resourceManage"  scrolling="no" width="100%" height="318PX" style="border:0px">


        </iframe>
    </div>
</div>
</div>
</body>
</html>

