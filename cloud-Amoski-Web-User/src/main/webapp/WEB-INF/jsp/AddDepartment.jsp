<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="math.jsp"%>
    <title>新增部门</title>
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
            width:10% !important
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
        .trs td{
            height: 32px;
            padding-top: 10px;
        }
        label{
            float: right!important;
            padding: 0px!important;
            margin-top: -2px;
        }
        .formControls {
            padding-left: 9px;
        }
        .input-width{
            width:100%
        }
        .widthTd{
            width:20px
        }
    </style>
</head>
<script>




    function validate(){
        return $("#layer_form").validate({
            rules:{
                orgName:{
                    required:true,
                    maxlength:20
                },
            },
            errorPlacement: function(error, element,node) {},
            errorPlacementIsName:errorPlacementIsName,
        });
    }

    var dataMessage;
    function errorPlacementIsName(vaild, node){
        dataMessage=node.element.dataset.name+node.message;
    }






    var imgsURL;
    function save(type){
        if(!validate().form()){
            layer.msg(dataMessage, {
                icon : 5,
                time : 3000
            });
            return;
        }
        var url=null;
        if(source=='add')
            url = '/AmoskiUser/DepartmentManage/AddDepartment';
        else
            url = '/AmoskiUser/DepartmentManage/UpdateDepartment';
        $.ajax({
            type: 'POST',
            dateType: 'JSON',
            data : $("#layer_form").serialize()+"&imgsURL="+imgsURL,
            url: url,
            beforeSend:function(){
                layer_wait();
            },
            success: function(json){
                layer_close();
                layer.msg(json.message,{icon:6,time:2000},function(){
                    if(json.code=="000")
                        window.parent[meath]()
                });
                //layer.close(addUpdateIndex);
                //table.draw();
            },
            error: function(){
                layer_close();
            }
        });
    }
    var bool=false;
    var getDate='<%=request.getParameter("getDate")%>';
    var source='<%=request.getParameter("source")%>';
    var meath='<%=request.getParameter("meath")%>';
    $(function(){
        validate();
        var getZtreeData=window.parent["getZtreeData"]();
        for(var i=0;i<getZtreeData.length;i++){
            $("#parentId").append("<option value='"+getZtreeData[i].id+"'>"+getZtreeData[i].name+"</option>")
        }
        window[source]();
    })

    /*初始化新增部门方法*/
    function add(){
        $("#parentId").val(window.parent.getQueryDepartment());
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
    };


    /*初始化修改部门方法*/
    function edit(){
        var data=window.parent[getDate]();
        $("#id").val(data.ID);
        $("#createName").val(data.CREATE_NAME);
        $("#createDate").val(new Date(data.CREATE_DATE).toLocaleString());
        $("#orgName").val(data.ORG_NAME);
        $("#parentId").val(data.PARENT_ID);
        $("#remark").val(data.REMARK);
    }
</script>
<body >

<div id="layer_add">
    <div class="page-add" style="padding-left: 3%;">
        <form action="" method="post" class="form form-horizontal" id="layer_form">

            <input type="hidden" id="id" name="id" value="" >
            <input type="hidden" id="createName" name="createName" value="" >
            <input type="hidden" id="createDate" name="createDate" value="" >

            <table style="width:80%">
                <tr class="trs">
                    <td class="tableTdTitle">
                        <label class="form-label col-xs-3 col-sm-3" style="width: auto;"><span class="c-red">*</span>部门名称：</label>
                    </td>
                    <td colspan="2" class="tableValue" style="width: 30%;">
                        <div class="formControls col-xs-8 col-sm-9" style="width: 100%;">
                            <input type="text" class="input-text" id="orgName" name="orgName" data-name="部门名称" style="width: 100%;">
                        </div>
                    </td>
                </tr>
                <tr class="trs">
                    <td class="tableTdTitle">
                        <label class="form-label col-xs-3 col-sm-3" style="width: auto;"><span class="c-red">*</span>上级部门：</label>
                    </td>
                    <td colspan="2"  class="tableValue"  style="width: 30%;">
                        <div class="formControls col-xs-8 col-sm-9" style="width: 100%;">
                            <select class="input-text" data-name="上级部门" id="parentId" name="parentId" style="width: 100%;">

                            </select>
                        </div>
                    </td>
                </tr>
                <tr class="trs">
                    <td  class="tableTdTitle">
                        <label class="form-label col-xs-3 col-sm-3" style="width: auto;"><span class="c-red"></span>备注：</label>
                    </td>
                    <td  colspan="2" >
                        <div class="formControls col-xs-8 col-sm-9" style="width: 100%;">
                            <input type="text" class="input-text" data-name="备注" id="remark" name="remark" style="width: 100%;">
                        </div>
                    </td>
                </tr>
            </table>

            <div class="row cl page-bottom">
                <div class="col-xs-offset-4">
                    <input class="btn btn-primary radius" type="button" onclick="save();" id="saveOrUpdate" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
                    <input class="btn btn-primary radius" type="button" onclick="window.parent[meath]();" value="&nbsp;&nbsp;关闭&nbsp;&nbsp;">
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
