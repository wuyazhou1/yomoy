<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
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
    <link href="/AmoskiWebUser/css/wcss/bootAll.css" rel="stylesheet" type="text/css" />
</head>
<script>
function RoleMenuResource(node){
    if(node==null || node==""){
        console.log("初始化失败");
        return;
    }
    this.role=node;
    this.menu=null;
    this.menuResource={};

    //获取保存参数
    this.getMenuResourceAjaxParent=function(){
        var strbug="role="+this.role;
        var menuStr="";
        var resourceStr="";
        for(var mStr in this.menuResource){
            if(mStr==null)
                continue;
            menuStr+=","+mStr;
            for(var rStr in this.menuResource[mStr]){
                if(rStr==null)
                    continue;
                resourceStr+=","+rStr;
            }
        }
        if(menuStr==""){
            menuStr='-1'
        }
        if(resourceStr==""){
            resourceStr='-1';
        }
        strbug+="&menu="+menuStr.substring(1);
        strbug+='&resource='+resourceStr.substring(1);
        console.log("提交角色菜单元素"+strbug);
        return strbug;
    }
    //判断元素是否存在
    this.isResourceEntity=function(data){
        if(this.menuResource[this.menu][data]==null){
            return true;
        }else{
            return false;
        }
    }
    //判断菜单是否存在
    this.isMenuEntity=function(nodes){
        if(this.menuResource[nodes]!=null )
            return false;
        else
            return true;
    }
    //菜单元素节点
    this.toResourceChecked=function(nodes){
        if(this.menuResource[queryDepartment]==null){
            nodes.checked=false;
            layer.msg("请选中菜单后操作元素", {icon : 6,time : 2000},function(){});
            return;
        }
        if(nodes==null){
            return;
        }
        var dataset = nodes.dataset;
        if(nodes.checked==true && this.menuResource[this.menu][dataset.code]==null){
            this.menuResource[this.menu][dataset.code]=dataset.code;
        }else if(nodes.checked==false){
            delete this.menuResource[this.menu][dataset.code];
        }
    }
    //设置对象的菜单代码和元素代码
    this.setMenuResource=function(data){
        for(var i=0;i<data.length;i++){
            if(this.menuResource[data[i].MENU_CODE]==null){
                this.menuResource[data[i].MENU_CODE]={};
            }
            if(this.menuResource[data[i].MENU_CODE][data[i].RES_CODE]==null){
                this.menuResource[data[i].MENU_CODE][data[i].RES_CODE]=data[i].RES_CODE;
            }
        }
    }
    this.setMenu=function(node){
        this.menu=node;
    }
    //上级菜单节点
    this.toParentZtreeCheck=function(treeNode){
        if(treeNode==null){
            return;
        }
        if(treeNode.checked==true && this.menuResource[treeNode.id]==null){
            this.menuResource[treeNode.id]={};
        }else if(treeNode.checked==false ){
            delete this.menuResource[treeNode.id];
        }
        if(treeNode.getParentNode()!=null){
            this.toParentZtreeCheck(treeNode.getParentNode());
        }
    }
    //下级菜单节点
    this.toNextZtreeCheck=function (treeNode){
        if(treeNode==null){
            return;
        }
        if(treeNode.checked==true && this.menuResource[treeNode.id]==null){
            this.menuResource[treeNode.id]={};
        }else if(treeNode.checked==false ){
            delete this.menuResource[treeNode.id];
        }
        if(treeNode.children!=null){
            for(var i=0;i<treeNode.children.length;i++){
                this.toNextZtreeCheck(treeNode.children[i]);
            }
        }
    }
}
</script>
<body style="height: 100%;">
<ul id="treeDemoImm" class="ztree" style="margin: 10px 0px 0px 0px;float: left;height: 75%;border: 1px solid #5ca1dc;overflow-y: auto;width: 15%;" ></ul>
<div class="page-container" style="width: 83%;float: left;margin: 0px -100px 0px -10px;padding-top:0px;">
    <div class="mt-10"  >
        <table id="example" class="table table-border table-bordered table-bg table-hover table-sort" >
            <thead>
            <tr class="text-c">
                <th width="25">
                    <input type="checkbox" name="checkAll" class="checkall" onclick="checkedClean();">
                </th>
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

    <div style="width: 100%;position: absolute;bottom: 21%;left: 20%;" >
        <a class="btn btn-secondary radius" onclick="save()">
            <i class="Hui-iconfont">&#xe600;</i>
            保存
        </a>
        <a class="btn btn-secondary radius" onclick="cancel()">
            <i class="Hui-iconfont">&#xe600;</i>
            取消
        </a>
    </div>


</div>

<script>

    //ztree树初始化值
    var firstAsyncSuccessFlag = 0;
    //查询值
    var queryDepartment="";
    var settingDao = {
        check: {
            enable: true ,
            nocheckInherit: true
        },
        data : {
            simpleData : {
                enable : true
            }
        },
        async : {
            enable : true,
            url : "/AmoskiUser/MenuManage/ztreeTMenu",
            autoParam : [ "id", "name" ],
            otherParam : {
                "otherParam" : "zTreeAsyncTest"
            },
            dataType : "json",//默认text
            dataFilter : ajaxDataFilter
        },
        view : {
            dblClickExpand : false
        },
        callback : {
            onAsyncSuccess : zTreeOnAsyncSuccess,
            onClick: zTreeOnClick,
            onCheck: onCheck
        }
    };
    function cancel(){
        window.parent["CloseRoleAuthorization"]();
    }

    function zTreeOnClick(event, treeId, treeNode) {
        queryDepartment=treeNode.id;
        MenuResource.setMenu(treeNode.id);
        table.draw();
        //$("#resourceManage").attr("src","/AmoskiWebUser/AMOSKI/ResourceManageList?menuCode="+treeNode.id);
    }


    function onCheck(e, treeId, treeNode){
        MenuResource.toParentZtreeCheck(treeNode.getParentNode());
        MenuResource.toNextZtreeCheck(treeNode);
        MenuResource.setMenu(treeNode.id);
        queryDepartment=treeNode.id;
        table.draw();
    }

    function getZtreeData(){
        return ZtreeData
    }
    var ZtreeData={};
    function ajaxDataFilter(treeId, parentNode, responseData) {
        for(var str in responseData){
            if(!MenuResource.isMenuEntity(responseData[str].id)){
                $(responseData[str]).attr("checked",'true');
            }else{
                $(responseData[str]).attr("checked",'false');
            }
            delete responseData[str].isParent;
        }
        ZtreeData=responseData;
        return responseData;
    };

    function zTreeOnAsyncSuccess(event, treeId, msg) {
        if (firstAsyncSuccessFlag == 0) {
            try {
                //调用默认展开第一个结点
                var selectedNode = ztreeObjCheck.getSelectedNodes();
                var nodes = ztreeObjCheck.getNodes();
                ztreeObjCheck.expandNode(nodes[0], true);
                var childNodes = ztreeObjCheck.transformToArray(nodes[0]);
                ztreeObjCheck.expandNode(childNodes[0], true);
                ztreeObjCheck.selectNode(childNodes[0]);
                var childNodes1 = ztreeObjCheck.transformToArray(childNodes[0]);
                //ztreeObjCheck.checkNode(childNodes1[0], true, true);
                queryDepartment=nodes[0].id;
                if(table==null){
                    initTable();
                }else{
                    table.draw()
                }
                firstAsyncSuccessFlag = 1;
            } catch (err) {
            }
        }
    }





    function selected() {
        $('#example tbody').on('click', 'tr', function() {
            if ($(this).hasClass('selected')){
                $(this).removeClass('selected');
            } else {
                table.$('tr.selected').removeClass('selected');
                $(this).addClass('selected');
            }
            /*var checkedBoox = $(this).children().first().children()[0];
            if(checkedBoox.disabled){
                layer.msg("请选中菜单后操作元素", {icon : 6,time : 2000},function(){});
            }else{
                checkedBoox.checked=!checkedBoox.checked;
            }*/
        });
    }




    var table = null;
    var MenuResource=null;
    $(document).ready(function() {
        var role='<%=request.getParameter("code")%>';
        MenuResource=new RoleMenuResource(role);
        //查看角色权限
        $.ajax({
            url:"/AmoskiUser/RoleManage/getRoleMenuResource",
            type:"post",
            data:"role="+role,
            dataType:"json",
            async:false,
            success:function(data){
                if(data.code!=null){
                    layer.msg(data.message, {
                        icon : 6,
                        time : 2000
                    },function(){
                        cancel();
                    });
                }
                MenuResource.setMenuResource(data);
            }
        });

        ztreeObjCheck = $.fn.zTree.init($("#treeDemoImm"), settingDao);
        $("#btnSearch").on('click',function(){
            table.draw();
        });
        selected();
    });
    /* 全选/反选 */
    function checkedClean() {
        if ($("[name='checkAll']").is(":checked")) {
            $("[name='checkitems']").prop("checked", true);
        } else {
            $("[name='checkitems']").prop("checked", false);
        }
    }
    function save(node){
            layer.confirm('确定保存角色权限吗？', {
                btn: ['确认','取消'] //按钮
            }, function(){
                $.ajax({
                    url:"/AmoskiUser/RoleManage/setRoleMenuResource",
                    type:"post",
                    data:MenuResource.getMenuResourceAjaxParent(),
                    dataType:"json",
                    async:true,
                    success:function(data){
                        //table.draw(false);
                        firstAsyncSuccessFlag=0;
                        if(data.code=="000"){
                            layer.msg("操作成功", {
                                icon : 6,
                                time : 2000
                            },function(){
                                cancel();
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
    }
    function closeCouponDatail(){
        layer.close(CouponDatail);
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
                    if(MenuResource.isMenuEntity(queryDepartment)){
                        return '<input type="checkbox" '+datas+' name="checkitems" onchange="MenuResource.toResourceChecked(this)"  value="' + data + '" class="checkchild" />';
                    }else if(MenuResource.isResourceEntity(data)){
                        return '<input type="checkbox" '+datas+' name="checkitems" onchange="MenuResource.toResourceChecked(this)"    value="' + data + '" class="checkchild" />';
                    }else{
                        return '<input type="checkbox" '+datas+' name="checkitems"  onchange="MenuResource.toResourceChecked(this)"  checked="checked" value="' + data + '" class="checkchild" />';
                    }
                },
                orderable : false
            },
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
                    d.menuCode = queryDepartment;
                }
            },
            aLengthMenu: [5],//设置每页显示数据条数的下拉选项
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





    Date.prototype.toLocaleString = function() {
        // 补0   例如 2018/7/10 14:7:2  补完后为 2018/07/10 14:07:02
        function addZero(num){
            if(num<10)
                return "0" + num;
            return num;
        }
        // 按自定义拼接格式返回
        return this.getFullYear() + "-" + addZero(this.getMonth() + 1) + "-" + addZero(this.getDate())+" "+
            + addZero(this.getHours()) + ":" + addZero(this.getMinutes()) + ":" + addZero(this.getSeconds());
    }


</script>

</body>
</html>

