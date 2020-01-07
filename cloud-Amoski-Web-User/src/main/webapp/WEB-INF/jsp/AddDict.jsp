<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="math.jsp"%>
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

    <script type="text/javascript" src="/AmoskiWebUser/js/wjs/add.js"></script>
    <script type="text/javascript" src="/AmoskiWebUser/js/wjs/validate-methods.js"></script>
    <link href="/AmoskiWebUser/css/wcss/add.css" rel="stylesheet" type="text/css" />
    <title>新增用户</title>

</head>
<script>
    function validate(){
        return $("#layer_form").validate({
            rules:{
                //字典代码
                code:{
                    required:true,
                    maxlength:20,
                    isAjaxExistense:true,
                    noChineseChar :true
                },
                //字典类型代码
                dictTypeCode:{
                    maxlength:9,
                    noChineseChar :true
                },
                //字典名字
                dictKey:{
                    required:true,
                    maxlength:20,
                },
                //字典值
                dictValue:{
                    maxlength:20,
                    noChineseChar :true
                },
                //备注
                remark:{
                    maxlength:100,
                },
            },
            errorPlacement: function(error, element,node) {},
            errorPlacementIsName:errorPlacementIsName,
        });
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
            url = '/AmoskiUser/Dict/AddDict';
        else
            url = '/AmoskiUser/Dict/UpdateDict';
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
        var getZtreeData=window.parent["getZtreeData"]();
        for(var i=0;i<getZtreeData.length;i++){
            $("#dictTypeCode").append("<option value='"+getZtreeData[i].id+"'>"+getZtreeData[i].name+"</option>")
        }
        validate();
        window[source]();
        jQuery.validator.addMethod("isAjaxExistense", function(value, element) {
            bool=false;
            $.ajax({
                type: 'POST',
                dateType: 'JSON',
                data : {
                    "code":$("#code").val(),
                    "id":$("#id").val()
                },
                url: "/AmoskiUser/Dict/CheckedDict",
                async:false,//true异步调用：false同步调用
                success: function(json){
                    if(json.code=="000"){
                        bool=true;
                    }
                }
            });
            return bool;
        }, "角色代码已经存在");
    })

    /*初始化新增部门方法*/
    function add(){
        $("#dictTypeCode").val(window.parent["getSelectedCode"]());
    }



    /*初始化修改部门方法*/
    function edit(){
        var data=window.parent[getDate]();
        var parentMap = $("#layer_form").serializeArray();
        for(var parent in parentMap){
            var name = parentMap[parent].name.replace(/([A-Z])/g,"_$1").toUpperCase();
            $("#"+parentMap[parent].name).val(data[name]);
        }
        $("#createDate").val(new Date(data.CREATE_DATE).toLocaleString());
    }
    function OrgChanage(node){
        if(node.value=="1")
            $("#orgCodeTr").css("display","none")
        else
            $("#orgCodeTr").css("display","none")
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
                        <label class="form-label col-xs-3 col-sm-3" style="width: auto;"><span class="c-red">*</span>字典代码：</label>
                    </td>
                    <td colspan="2" class="tableValue" style="width: 30%;">
                        <div class="formControls col-xs-8 col-sm-9" style="width: 100%;">
                            <input type="text" class="input-text" id="code" name="code" data-name="字典代码" style="width: 100%;">
                        </div>
                    </td>
                </tr>
                <tr class="trs">
                    <td class="tableTdTitle">
                        <label class="form-label col-xs-3 col-sm-3" style="width: auto;"><span class="c-red">*</span>字典类型代码：</label>
                    </td>
                    <td colspan="2"  class="tableValue"  style="width: 30%;">
                        <div class="formControls col-xs-8 col-sm-9" style="width: 100%;">
                            <select class="input-text" data-name="字典类型代码" id="dictTypeCode" name="dictTypeCode" style="width: 100%;">
                            </select>
                        </div>
                    </td>
                </tr>
                <tr class="trs">
                    <td  class="tableTdTitle">
                        <label class="form-label col-xs-3 col-sm-3" style="width: auto;"><span class="c-red">*</span>字典名字：</label>
                    </td>
                    <td  colspan="2" >
                        <div class="formControls col-xs-8 col-sm-9" style="width: 100%;">
                            <input type="text" class="input-text" data-name="字典名字" id="dictKey" name="dictKey" style="width: 100%;">
                        </div>
                    </td>
                </tr>
                <tr class="trs">
                    <td  class="tableTdTitle">
                        <label class="form-label col-xs-3 col-sm-3" style="width: auto;">字典值：</label>
                    </td>
                    <td  colspan="2" >
                        <div class="formControls col-xs-8 col-sm-9" style="width: 100%;">
                            <input type="text" class="input-text" data-name="字典值" id="dictValue" name="dictValue" style="width: 100%;">
                        </div>
                    </td>
                </tr>
                <tr class="trs">
                    <td  class="tableTdTitle">
                        <label class="form-label col-xs-3 col-sm-3" style="width: auto;">字典备注：</label>
                    </td>
                    <td  colspan="2" >
                        <div class="formControls col-xs-8 col-sm-9" style="width: 100%;">
                            <textarea  class="input-text" rows="3" cols="20"  data-name="字典备注" id="remark" name="remark" style="width: 100%;height: 80px;"></textarea>
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
