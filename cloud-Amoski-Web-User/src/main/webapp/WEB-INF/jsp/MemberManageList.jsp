<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <%@ include file="math.jsp"%>
    <title>会员管理</title>
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
    <script type="text/javascript" src="/AmoskiWebUser/js/wjs/DepartmentZtree.js"></script>
    <link href="/AmoskiWebUser/css/wcss/bootAll.css" rel="stylesheet" type="text/css" />

</head>

<body style="height: 100%;">
<ul id="treeDemoImm" class="ztree" style="margin: 10px 0px 0px 0px;float: left;height: 90%;border: 1px solid #5ca1dc;overflow-y: auto;width: 15%;" ></ul>
<div class="page-container" style="width: 83%;float: left;margin: 0px -100px 0px -10px;padding-top:0px;">

    <div class="cl pd-5 bg-1 bk-gray mt-10" >
        昵称：
        <input type="text" class="input-text" style="width: 180px"  id="name" name="name" maxlength="50">&nbsp;&nbsp;&nbsp;
        手机号：
        <input type="text" class="input-text" style="width: 180px"  id="tel" name="tel" maxlength="50">&nbsp;&nbsp;&nbsp;
        地区搜索会员：
        <input type="text" class="input-text" style="width: 180px"  id="address" name="address" maxlength="50">&nbsp;&nbsp;&nbsp;



        <a class="btn btn-success radius" id="btnSearch">
            <i class="Hui-iconfont">&#xe665;</i>
            查询
        </a>
    </div>

    <div class="cl pd-5 bg-1 bk-gray mt-10" >
                <shiro:hasPermission name="res10">
				<span class="l">
                    <a class="btn btn-danger radius" onclick="ShowMemberImpowerRole()">
						<i class="Hui-iconfont">&#xe6e2;</i>
						会员授权
					</a>
				</span>
                </shiro:hasPermission>
                <shiro:hasPermission name="res11">
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
                <th width="">昵称</th>
                <th width="">手机号</th>
                <th width="">所属部门</th>
                <th width="">会员地址</th>
                <th width="">性别</th>
                <th width="">身份证</th>
                <th width="">是否可用</th>
                <th width="">操作</th>
            </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    </div>
</div>
</div>
</body>
<script>
    var MemberImpowerRole = null;
    function ShowMemberImpowerRole(){
        $("#saveOrUpdate").attr("onclick","save('update');")
        if(table.row('.selected').length!=1){
            return layer.msg('请选择一个会员进行授权',{icon:5,time:2000});
        }
        MemberImpowerRole=layer.open({
            type: 2,
            title: "会员授权用户",
            shadeClose: true,
            fix: false, //不固定
            hade:0.4,
            maxmin: true, //开启最大化最小化按钮
            area: ['580px', '500px'],
            content: "/AmoskiWebUser//AMOSKI/MemberImpowerRole?code="+table.row('.selected').data().ID
        });
    }
    function CloseMemberImpowerRole(){
        table.draw();
        layer.close(MemberImpowerRole);
    }





    var table = null;
    $(document).ready(function() {
        //initTable();
        $("#btnSearch").on('click',function(){
            table.draw();
        });
        selected();
        ztreeObjCheck = $.fn.zTree.init($("#treeDemoImm"), settingDao);
    });


    function closeCouponDatail(){
        layer.close(CouponDatail);
    }
    function initTable() {

        var columns = [
            {
                data : "NAME"
            }, {
                data : "TEL"
            }, {
                data : "ORG_CODE"
            },{
                data : "ADDRESS"
            },{
                data : "MEMBER_SEX",
                render : function(data, type, full, meta) {
                    if(data=="1"){
                        return "男";
                    }else if(data=="2"){
                        return "女";
                    }else{
                        return "未知";
                    }
                },
            },{
                data : "IDENTITY_CARD"
            },{
                data : "LOCKED"
            },{
                data : "ID",
                render : function(data, type, full, meta) {
                    return "<a class=\"btn btn-primary radius\" onclick=\"showAlbum("+data+")\"><i class=\"Hui-iconfont\"></i>微信相册</a>";
                },
            }
        ];
        table = $('#example').DataTable({
            ajax : {
                url : "/AmoskiUser/MemberManage/MemberList",
                type : "POST",
                data : function(d) {
                    d.orgCode = queryDepartment;
                    d.name = $("#name").val();
                    d.tel = $("#tel").val();
                    d.address = $("#address").val();
                }
            },
            columns : columns,
            serverSide : true,// 服务器查询数据分页
            searching : false,
            bAutoWidth : false, //宽度自适应
            dom : 'rt<"bottom"ilp><"clear">',
            filter : false,// 去掉搜索框
            //scrollX : "100%",
            scrollCollapse : "true",
            paginationType : 'full_numbers',
            sorting : [ [ 1, "desc" ] ],
            // 默认第几个排序
        });

    }

    function selected() {
        $('#example tbody').on('click', 'tr', function() {
            $(this).children().first().children().attr("checked", true);
            if ($(this).hasClass('selected')) {
                $(this).removeClass('selected');
            } else {
                table.$('tr.selected').removeClass('selected');
                $(this).addClass('selected');
            }
        });
    }
    var memberAlbum=null;
    function showAlbum(nodes){
        memberAlbum=layer.open({
            type: 2,
            title: "用户授权",
            shadeClose: true,
            fix: false, //不固定
            hade:0.4,
            maxmin: true, //开启最大化最小化按钮
            area: ['100%', '100%'],
            content: "/AmoskiWebActivity/layui/html/photoManage.html?id="+nodes
        });
    }
    function closeAlbum(){
        layer.close(memberAlbum);
    }
</script>
</html>

