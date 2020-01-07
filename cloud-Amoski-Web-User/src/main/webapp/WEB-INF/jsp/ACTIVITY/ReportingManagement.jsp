<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
    <%@ include file="../math.jsp"%>
    <title>举报管理</title>
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
    <link href="/AmoskiWebUser/js/ztree/css/zTreeStyle/ztree.css" rel="stylesheet" type="text/css" />
    <link href="/AmoskiWebUser/js/ztree/css/zTreeStyle/hpzTree.css" rel="stylesheet" type="text/css" />
    <link href="/AmoskiWebUser/css/wcss/bootAll.css" rel="stylesheet" type="text/css" />

    <script>
        //ztree树初始化值
        var firstAsyncSuccessFlag = 0;
        //查询值
        var queryDepartment="";
        var settingDao = {
            data : {
                simpleData : {
                    enable : true
                }
            },
            async : {
                enable : true,
                url : "/AmoskiUser/Dict/GetDictZtree",
                autoParam : [ "id", "name" ],
                otherParam : {
                    "parentCode" : "100"
                },
                dataType : "json",//默认text
                dataFilter : ajaxDataFilter
            },
            view : {
                dblClickExpand : false
            },
            callback : {
                onAsyncSuccess : zTreeOnAsyncSuccess,
                onClick: zTreeOnClick
            }
        };

        function getZtreeData(){
            return ZtreeData
        }
        var ZtreeData={};
        function ajaxDataFilter(treeId, parentNode, responseData) {
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
                    ztreeObjCheck.checkNode(childNodes1[0], true, true);
                    queryDepartment=nodes[0].id;
                    selectedCode=nodes[0].id;
                    if(table==null)
                        initTable();
                    else
                        table.draw();
                    firstAsyncSuccessFlag = 1;
                } catch (err) {
                }
            }
        }

        function zTreeOnClick(event, treeId, treeNode) {
            queryDepartment=treeNode.id;
            selectedCode=treeNode.id;
            table.draw();
        }





        //添加修改弹出框设置
        var selectedCode="";
        function selected() {
            $('#example tbody').on('click', 'tr', function() {
                $(this).children().first().children().attr("checked", true);
                if ($(this).hasClass('selected')) {
                    $(this).removeClass('selected');
                } else {
                    table.$('tr.selected').removeClass('selected');
                    $(this).addClass('selected');
                    if(dictTypeBool)
                        selectedCode = table.row('.selected').data().CODE
                }
            });
        }

    </script>

</head>

<body style="height: 100%;">
<ul id="treeDemoImm" class="ztree" style="margin: 10px 0px 0px 0px;float: left;height: 90%;border: 1px solid #5ca1dc;overflow-y: auto;width: 15%;" ></ul>
<div class="page-container" style="width: 83%;float: left;margin: 0px -100px 0px -10px;padding-top:0px;">

    <div class="cl pd-5 bg-1 bk-gray mt-10" >
        举报类容
        <input type="text" class="input-text" style="width: 180px"  id="dictKey" name="dictKey" maxlength="50">&nbsp;&nbsp;&nbsp;

        <shiro:hasPermission name="res41">
        <a class="btn btn-success radius" id="btnSearch">
            <i class="Hui-iconfont">&#xe665;</i>
            查询
        </a>
        </shiro:hasPermission>
    </div>

    <div class="mt-10" >
        <table id="example" class="table table-border table-bordered table-bg table-hover table-sort" >
            <thead>
            <tr class="text-c">
                <th width="">举报id</th>
                <th width="">举报类型</th>
                <th width="">会员名称/会员id</th>
                <th width="">举报类容</th>
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
                data : "ID"
            }, {
                data : "SUGGESTION_ID"
            }, {
                data : "MEMBER_ID",
            },{
                data : "REMAKE",
            },
        ];
        table = $('#example').DataTable({
            ajax : {
                url : "/AmoskiUser/ReportingManage/ReportingList",
                type : "POST",
                data : function(d) {
                    d.suggestionId = queryDepartment;
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

