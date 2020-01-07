<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<html>
<head>
    <%@ include file="math.jsp"%>
    <title>部门管理</title>
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
    <style type="text/css">
        .buttonStyle{
            background: white;
        }
        .select{
            width: 180px;
            height: 32px;
        }
        .tableTdTitle{
            width:65px
        }
        .radius{
            margin-left:5px;
        }
        .input-text, .textarea{
            width: 93%;
        }
        .layui-layer-page .layui-layer-content{
            overflow-x: hidden;
        }
        .form .row{
            width:49%;
            float:left
        }
        .col-sm-3{
            width: 30%;
        }
        .col-sm-9{
            width: 69%;
        }
        .tableTitle{
            width: 160px;
        }
        .tableValue{
            width:35%;
        }
        label{
            margin-top: 0px;
            float: right!important;
        }

        .input-width{
            width:100%
        }
    </style>
</head>
<script>
    function queryCoupon(){
        $("#saveOrUpdate").attr("onclick","save('update');")
        if(table.row('.selected').length!=1){
            return layer.msg('请至少选中一行进行查看!',{icon:5,time:2000});
        }
        ShouAddMemberCoupon({title:"查看窗口",meath:"queryCoupon"});
    }
    function edit(){
        $("#saveOrUpdate").attr("onclick","save('update');")
        if(table.row('.selected').length!=1){
            return layer.msg('请至少选中一行进行修改!',{icon:5,time:2000});
        }
        ShouAddMemberCoupon({title:"修改窗口",meath:"edit"});
    }
    function add(){
        ShouAddMemberCoupon({title:"添加窗口",meath:"add"});
    }
    function getMemberCoupon(){
        return table.row('.selected').data();
    }
    var AddMemberCoupon="";
    function ShouAddMemberCoupon(node){
        AddMemberCoupon=layer.open({
            type: 2,
            title: node.title,
            shadeClose: true,
            fix: false, //不固定
            hade:0.4,
            maxmin: true, //开启最大化最小化按钮
            area: ['70%', '90%'],
            content: "/AmoskiWebUser/Department/addDepartment?source="+node.meath+"&meath=CloseAddMemberCoupon&getDate=getMemberCoupon"
            //content: "/AmoskiWebUser/jsp/addDepartment?source="+node.meath+"&meath=CloseAddMemberCoupon&getDate=getMemberCoupon"
        });
    }
    function CloseAddMemberCoupon(data){
        ztreeObjCheck = $.fn.zTree.init($("#treeDemoImm"), settingDao);
        table.draw();
        layer.close(AddMemberCoupon);
    }
</script>
<body style="height: 100%;">
<ul id="treeDemoImm" class="ztree" style="margin: 10px 0px 0px 0px;float: left;height: 90%;border: 1px solid #5ca1dc;overflow-y: auto;width: 15%;" ></ul>
<div class="page-container"  style="width: 83%;float: left;margin: 0px -100px 0px -10px;padding-top:0px;">


    <div class="cl pd-5 bg-1 bk-gray mt-10" >
        部门名称：
        <input type="text" class="input-text" style="width: 180px"  id="name" name="name" maxlength="50">&nbsp;&nbsp;&nbsp;
<%--
        优惠券名称：
        <input type="text" class="input-text" style="width: 180px"  id="queryName" name="queryName" maxlength="50">&nbsp;&nbsp;&nbsp;
        创建日期：
        <input type="text" class="input-text" style="width: 180px"  id="queryCreateTime" name="queryCreateTime" onclick="laydate({istime: true, format: 'YYYY-MM-DD',min: '2000-01-01',max: $('#datemax').val()})" maxlength="50" readonly="readonly">&nbsp;&nbsp;&nbsp;
--%>

        <shiro:hasPermission name="res2">
        <a class="btn btn-success radius" id="btnSearch">
            <i class="Hui-iconfont">&#xe665;</i>
            查询
        </a>
        </shiro:hasPermission>
    </div>

    <div class="cl pd-5 bg-1 bk-gray mt-10" >
				<span class="l">
                    <shiro:hasPermission name="res4">
					<a class="btn btn-secondary radius" onclick="add()">
						<i class="Hui-iconfont">&#xe600;</i>
						新增
					</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="res5">
					<a class="btn btn-primary radius" onclick="edit()">
						<i class="Hui-iconfont">&#xe6df;</i>
						修改
					</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="res6">
					<a class="btn btn-danger radius" onclick="removeMemberAll()">
						<i class="Hui-iconfont">&#xe6e2;</i>
						删除
					</a>
                    </shiro:hasPermission>
				</span>

                <shiro:hasPermission name="res3">
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
                <th width="">部门名称</th>
                <th width="">创建人</th>
                <th width="">上级部门</th>
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
    var table = null;
    $(document).ready(function() {
        //initTable();
        $("#btnSearch").on('click',function(){
            table.draw();
        });
        selected();
        ztreeObjCheck = $.fn.zTree.init($("#treeDemoImm"), settingDao);
    });


    function removeMemberAll(node){
        var data=table.row('.selected');
        var idAll="";
        if(data.length>0){
            for(var i=0;i<data.length;i++){
                idAll+=","+data.data().ID;
            }
            idAll=idAll.substring(1);
            layer.confirm('确定删除该部门吗？', {
                btn: ['删除','取消'] //按钮
            }, function(){
                $.ajax({
                    url:"/AmoskiUser/DepartmentManage/removeDepartment",
                    type:"post",
                    data:{"id":idAll},
                    dataType:"json",
                    async:true,
                    success:function(data){
                        ztreeObjCheck = $.fn.zTree.init($("#treeDemoImm"), settingDao);
                        table.draw();
                        if(data.code=="000"){
                            layer.msg("操作成功", {
                                icon : 6,
                                time : 2000
                            },function(){
                            });
                        }else{
                            layer.msg("操作失败", {
                                icon : 6,
                                time : 2000
                            });
                        }
                    }
                });
            }, function(){
                layer_close();
            });
        }else{
            layer.msg("请勾部门进行删除", {
                icon : 6,
                time : 2000
            });
        }
    }
    function closeCouponDatail(){
        layer.close(CouponDatail);
    }
    function initTable() {
        var columns = [
            {
                data : "ORG_NAME"
            }, {
                data : "CREATE_NAME"
            }, {
                data : "PARENT_ID"
            }
        ];
        table = $('#example').DataTable({
            ajax : {
                url : ctx + "AmoskiUser/DepartmentManage/DepartmentList",
                type : "POST",
                data : function(d) {
                    d.name = $("#name").val();
                    d.parentId = queryDepartment;
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
</script>
</html>

