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
        url : "/AmoskiUser/DepartmentManage/getZtreeDatail",
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
            if(table==null){
                queryDepartment=nodes[0].id;
                initTable();
            }else{
                table.draw();
            }
            firstAsyncSuccessFlag = 1;
        } catch (err) {
        }
    }
}


function getQueryDepartment(){
    return queryDepartment;
}

function zTreeOnClick(event, treeId, treeNode) {
    queryDepartment=treeNode.id;
    table.draw();
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
    table.draw();
    layer.close(EjectWindow);
}