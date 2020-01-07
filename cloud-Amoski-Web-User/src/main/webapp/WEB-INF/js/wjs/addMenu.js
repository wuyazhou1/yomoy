
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

function zTreeOnClick(event, treeId, treeNode) {
    queryDepartment=treeNode.id;
    table.draw();
    $("#resourceManage").attr("src","/AmoskiWebUser/AMOSKI/ResourceManageList?menuCode="+treeNode.id);
}




//添加修改弹出框设置
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

function closeEjectWindow(){
    layer.close(EjectWindow);
}
//查看详情
function queryDetails(){
    $("#saveOrUpdate").attr("onclick","save('update');")
    if(table.row('.selected').length!=1){
        return layer.msg('请至少选中一行进行查看!',{icon:5,time:2000});
    }
    ShouAddMemberCoupon({title:"查看详情",meath:"queryCoupon"});
}
function edit(){
    $("#saveOrUpdate").attr("onclick","save('update');")
    if(table.row('.selected').length!=1){
        return layer.msg('请至少选中一行进行修改!',{icon:5,time:2000});
    }
    ShouAddMemberCoupon({title:"修改数据",meath:"edit"});
}
function add(){
    ShouAddMemberCoupon({title:"添加数据",meath:"add"});
}
//获取表单数据
function getEjectWindow(){
    return table.row('.selected').data();
}
//更新表格，关闭弹出框
var EjectWindow="";
function CloseAddEjectWindow(data){
    //table.draw();
    firstAsyncSuccessFlag=0;
    ztreeObjCheck = $.fn.zTree.init($("#treeDemoImm"), settingDao);
    layer.close(EjectWindow);
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
        content: "/AmoskiWebUser/AMOSKI/AddMenuManage?source="+node.meath+"&meath=CloseAddEjectWindow&getDate=getEjectWindow&id="+queryDepartment
        //content: "/AmoskiWebUser/jsp/addDepartment?source="+node.meath+"&meath=CloseAddMemberCoupon&getDate=getMemberCoupon"
    });
}
function CloseAddMemberCoupon(data){
    table.draw();
    layer.close(AddMemberCoupon);
}


var table = null;
$(document).ready(function() {
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
function removeMemberAll(node){
    var data=$("input[name='checkitems']:checked");
    var idAll="";
    if(data.length>0){
        for(var i=0;i<data.length;i++){
            idAll+=","+data[i].dataset.id;
        }
        idAll=idAll.substring(1);
        layer.confirm('确定删除该行数据吗？', {
            btn: ['删除','取消'] //按钮
        }, function(){
            $.ajax({
                url:"/AmoskiUser/MenuManage/RemoveTMenu",
                type:"post",
                data:{"id":idAll},
                dataType:"json",
                async:true,
                success:function(data){
                    //table.draw(false);
                    firstAsyncSuccessFlag=0;
                    ztreeObjCheck = $.fn.zTree.init($("#treeDemoImm"), settingDao);
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
            data : "MENU_NAME"
        }, {
            data : "PARENT_NAME"
        }, {
            data : "MENU_URL"
        }, {
            data : "CODE"
        }, {
            data : "ORDER_CODE"
        }, {
            data : "CREATE_NAME"
        }, {
            data : "CREATE_DATE",
            render : function(data, type, full, meta) {
                return new Date(data).toLocaleString();
            }

        }
    ];
    table = $('#example').DataTable({
        ajax : {
            url : ctx + "AmoskiUser/MenuManage/MenuManageList",
            type : "POST",
            data : function(d) {
                d.menuName = $("#menuName").val();
                d.parentCode = queryDepartment;
            }
        },
        aLengthMenu: [4],//设置每页显示数据条数的下拉选项
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
            $("#resourceManage").attr("src","/AmoskiWebUser/AMOSKI/ResourceManageList?menuCode="+table.row('.selected').data().CODE)
        }
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
};