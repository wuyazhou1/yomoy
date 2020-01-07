<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
        window.parent["ShouAddResource"]({title:"查看窗口",meath:"queryCoupon"},table.row('.selected').data());
    }
    function edit(){
        $("#saveOrUpdate").attr("onclick","save('update');")
        if(table.row('.selected').length!=1){
            return layer.msg('请至少选中一行进行修改!',{icon:5,time:2000});
        }
        window.parent["ShouAddResource"]({title:"修改窗口",meath:"edit"},table.row('.selected').data());
    }
    function add(){
        window.parent["ShouAddResource"]({title:"添加窗口",meath:"add"},table.row('.selected').data());
    }
    function getMemberCoupon(){
        return table.row('.selected').data();
    }
</script>
<body style="height: 100%;">
<div class="page-container" style="padding: 0px;">

    <div class="cl pd-5 bg-1 bk-gray mt-10" >
				<span class="l">
                    <shiro:hasPermission name="res26">
					<a class="btn btn-secondary radius" onclick="add()">
						<i class="Hui-iconfont">&#xe600;</i>
						新增元素
					</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="res27">
					<a class="btn btn-primary radius" onclick="edit()">
						<i class="Hui-iconfont">&#xe6df;</i>
						修改元素
					</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="res28">
					<a class="btn btn-danger radius" onclick="removeMemberAll()">
						<i class="Hui-iconfont">&#xe6e2;</i>
						删除元素
					</a>
                    </shiro:hasPermission>
				</span>
                <shiro:hasPermission name="res25">
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
                <th width="">元素代码</th>
                <th width="">所属菜单</th>
                <th width="">元素名称</th>
                <th width="">元素类型</th>
                <th width="">备注</th>
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
        initTable();
        $("#btnSearch").on('click',function(){
            table.draw();
        });
        selected();
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
                    url:"/AmoskiUser/ResourceManage/deleteResourceManage",
                    type:"post",
                    data:{"id":idAll},
                    dataType:"json",
                    async:true,
                    success:function(data){
                        table.draw(false);
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
    var menuCode = '<%=request.getParameter("menuCode")%>';
    function initTable() {
        var columns = [
            {
                data : "CODE"
            }, {
                data : "MENU_NAME"
            }, {
                data : "RES_NAME"
            }, {
                data : "RES_TYPE",
                render : function(data, type, full, meta) {
                    return data.replace("<","&#60;").replace(">","&#62;").replace("<","&#60;").replace(">","&#62;");
                }
            }, {
                data : "REMARK"
            }
        ];
        table = $('#example').DataTable({
            ajax : {
                url : ctx + "AmoskiUser/ResourceManage/ResourceManageList",
                type : "POST",
                data : function(d) {
                    d.menuCode = menuCode;
                }
            },
            aLengthMenu: [3],//设置每页显示数据条数的下拉选项
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

