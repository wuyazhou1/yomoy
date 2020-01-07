<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <%@ include file="math.jsp"%>
    <title>用户管理</title>
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
    <script type="text/javascript" src="/AmoskiWebUser/js/wjs/addDictType.js"></script>
    <link href="/AmoskiWebUser/css/wcss/bootAll.css" rel="stylesheet" type="text/css" />

</head>

<body style="height: 100%;">
<ul id="treeDemoImm" class="ztree" style="margin: 10px 0px 0px 0px;float: left;height: 90%;border: 1px solid #5ca1dc;overflow-y: auto;width: 15%;" ></ul>
<div class="page-container" style="width: 83%;float: left;margin: 0px -100px 0px -10px;padding-top:0px;">

    <div class="cl pd-5 bg-1 bk-gray mt-10" >
        字典主键
        <input type="text" class="input-text" style="width: 180px"  id="dictKey" name="dictKey" maxlength="50">&nbsp;&nbsp;&nbsp;

        <shiro:hasPermission name="res34">
        <a class="btn btn-success radius" id="btnSearch">
            <i class="Hui-iconfont">&#xe665;</i>
            查询
        </a>
        </shiro:hasPermission>
    </div>

    <div class="cl pd-5 bg-1 bk-gray mt-10" >
				<span class="l">
                    <shiro:hasPermission name="res36">
					<a class="btn btn-secondary radius" onclick="add()">
						<i class="Hui-iconfont">&#xe600;</i>
						新增
					</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="res37">
					<a class="btn btn-primary radius" onclick="edit()">
						<i class="Hui-iconfont">&#xe6df;</i>
						修改
					</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="res38">
					<a class="btn btn-danger radius" onclick="removeMemberAll()">
						<i class="Hui-iconfont">&#xe6e2;</i>
						删除
					</a>
                    </shiro:hasPermission>
				</span>
                <shiro:hasPermission name="res35">
                <span class="r">
					<a class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" href="javascript:location.replace(location.href);" title="刷新">
						<i class="Hui-iconfont">&#xe68f;</i>
					</a>
				</span>
                </shiro:hasPermission>
    </div>

    <div class="mt-10" >
        <table id="example" class="table table-border table-bordered table-bg table-hover table-sort" >
            <thead>
            <tr class="text-c">
                <th width="25">
                    <input type="checkbox" name="checkAll" class="checkall" onclick="checkedClean();">
                </th>
                <th width="">字典代码</th>
                <th width="">字典类型代码</th>
                <th width="">字典主键</th>
                <th width="">字典值</th>
                <th width="">字典备注</th>
                <th width="">创建人</th>
                <th width="">创建时间</th>
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
    var dictTypeBool=false;
    function removeMemberAll(node){
        var data=$("input[name='checkitems']:checked");
        var idAll="";
        if(data.length>0){
            for(var i=0;i<data.length;i++){
                idAll+=","+data[i].dataset.code;
            }
            idAll=idAll.substring(1);
            layer.confirm('确定删除该用户吗？', {
                btn: ['删除','取消'] //按钮
            }, function(){
                $.ajax({
                    url:"/AmoskiUser/Dict/DeleteDict",
                    type:"post",
                    data:{"code":idAll},
                    dataType:"json",
                    async:true,
                    success:function(data){
                        table.draw(false);
                        if(data.code=="000"){
                            layer.msg("操作成功", {
                                icon : 6,
                                time : 2000
                            },function(){
                                table.draw();
                            });
                        }else{
                            layer.msg(data.message, {
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
            layer.msg("请勾选用户进行删除", {
                icon : 6,
                time : 2000
            });
        }
    }


    function ShouAddMemberCoupon(node){
        EjectWindow=layer.open({
            type: 2,
            title: node.title,
            shadeClose: true,
            fix: false, //不固定
            hade:0.4,
            maxmin: true, //开启最大化最小化按钮
            area: ['70%', '90%'],
            content: "/AmoskiWebUser/AMOSKI/AddDict?source="+node.meath+"&meath=CloseAddEjectWindow&getDate=getEjectWindow"
        });
    }
    var table = null;
    $(document).ready(function() {

        $("#btnSearch").on('click',function(){
            table.draw();
        });
        selected();
        ztreeObjCheck = $.fn.zTree.init($("#treeDemoImm"), settingDao);
    });

    /* 全选/反选 */
    function checkedClean() {
        if ($("[name='checkAll']").is(":checked")) {
            $("[name='checkitems']").prop("checked", true);
        } else {
            $("[name='checkitems']").prop("checked", false);
        }
    }

    function initTable() {
        var columns = [
            {
                sClass : "text-c",
                data : "CODE",
                render : function(data, type, full, meta) {
                    var datas="  ";
                    for(var str in full){
                        datas+="data-"+str+"='"+(full[str]==null?"":full[str])+"'";
                    }
                    return '<input type="checkbox" '+datas+' name="checkitems" value="' + data + '" class="checkchild" />';
                },
                orderable : false
            },
            {
                data : "CODE"
            }, {
                data : "DICT_TYPE_CODE"
            }, {
                data : "DICT_KEY",
            },{
                data : "DICT_VALUE",
            },{
                data : "REMARK"
            },{
                data : "CREATE_NAME"
            },{
                data : "CREATE_DATE",
                render : function(data, type, full, meta) {
                    return new Date(data).toLocaleString();
                },
            },
        ];
        table = $('#example').DataTable({
            ajax : {
                url : "/AmoskiUser/Dict/DictList",
                type : "POST",
                data : function(d) {
                    d.dictTypeCode = queryDepartment;
                    d.dictKey = $("#dictKey").val();
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


</script>
</html>

