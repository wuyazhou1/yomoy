var setting = {
	check : {
		enable : true,
        chkStyle:"radio",
        radioType :"all",
},
	view : {
		dblClickExpand : false
	},
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		beforeClick : beforeClick,
		onCheck : onCheck
	}
};
$(document).ready(function() {
	$("#btn_CheckAllNodes").click(CheckAllNodes);
	$("#btn_CancelAllNodes").click(CancelAllNodes);
	$("#returnCheckAllNodes").click(returnCheckAllNodes);
	$("#menuContent").hide();
});
function beforeClick(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}
function onCheck(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo"), nodes = zTree
			.getCheckedNodes(true), v = "",code="";
	for (var i = 0, l = nodes.length; i < l; i++) {
		v += nodes[i].name + ",";
		code +=nodes[i].id + ",";
	}
	if(code.length>0)
		code = code.substring(0, code.length - 1);
	if (v.length > 0)
		v = v.substring(0, v.length - 1);
	$("#key"+index).val(v);
	$("#code"+index).val(code);
	return false;

}
var lastValue = "", nodeList = [], fontCss = {};
function searchNode(str) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	 if(/.*[\u4e00-\u9fa5]+.*$/.test(str)){
  	  nodeList = zTree.getNodesByParamFuzzy("name", str);
    }else{
  	  nodeList = zTree.getNodesByParamFuzzy("id", str);
    };
	nodeList = zTree.transformToArray(nodeList);
	updateNodes(true);

}
function updateNodes(highlight) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	var allNode = zTree.transformToArray(zTree.getNodes());
	zTree.hideNodes(allNode);
	for ( var n in nodeList) {
		findParent(zTree, nodeList[n]);
	}
	zTree.showNodes(nodeList);
}
function findParent(zTree, node) {
	zTree.expandNode(node, true, false, false);
	var pNode = node.getParentNode();
	if (pNode != null) {
		nodeList.push(pNode);
		findParent(zTree, pNode);
	}
}
// 全选
function CheckAllNodes() {
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	treeObj.checkAllNodes(true);
	var nodes = treeObj.getCheckedNodes(true);
	var msg = "",code="";
	for (var i = 0; i < nodes.length; i++) {
		msg += nodes[i].name + ",";
		code +=nodes[i].id + ",";
	}
	if(code.length>0)
		code = code.substring(0, code.length - 1);
	if (msg.length > 0) {
		msg = msg.substring(0, msg.length - 1);
	}
	var cityObj = $("#key" + index);
	cityObj.attr("value", msg);
	$("#code"+index).val(code);
}

// 全取消
function CancelAllNodes() {
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	treeObj.checkAllNodes(false);
	var cityObj = $("#key" + index);
	cityObj.attr("value", "");
	$("#code"+index).val("");
}

// 反选
function returnCheckAllNodes() {
	var value = "";
	var v = "";
	var code="";
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	nodes = treeObj.getCheckedNodes(true);
	var nodesno = treeObj.getCheckedNodes(false);
	if (nodes.length > 0) {
		for (var i = 0, l = nodes.length; i < l; i++) {
			treeObj.checkNode(nodes[i]);
			v += nodes[i].name + ",";
			code +=nodes[i].id + ",";
		};
		if(code.length>0)
			code = code.substring(0, code.length - 1);
		if (v.length > 0)
			v = v.substring(0, v.length - 1);
		var cityObj = $("#key" + index);
		cityObj.attr("value","");
		$("#code"+index).val("");
	}
	if (nodesno.length > 0) {
		for (var i = 0, l = nodesno.length; i < l; i++) {
			treeObj.checkNode(nodesno[i]);
			value += nodesno[i].name + ",";
			code +="'"+nodesno[i].id + "',";
		}
		if(code.length>0)
			code = code.substring(0, code.length - 1);
		if (value.length > 0)
			value = value.substring(0, value.length - 1);
		var cityObj = $("#key" + index);
		cityObj.attr("value", value);
		$("#code"+index).val(code);
	}
}

function closediv() {
	$("#menuContent").fadeOut("fast");
}
var index = "";
function showdiv(i) {
	$("#menuContent").fadeIn("fast");
	$.fn.zTree.destroy("treeDemo");
    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
	index = i;
	// CurInput = i; //标记第一个文本框
	var cityObj = $("#key" + i);
	var cityOffset = $("#key" + i).offset();
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	treeObj.checkAllNodes(false);
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	treeObj.showNodes(treeObj.transformToArray(treeObj.getNodes()));
	$("#menuContent").css({
		left : cityOffset.left + "px",
		top : cityOffset.top + cityObj.outerHeight() + "px"
	}).slideDown("fast");
}

// 获取所有选中节点的值
function GetCheckedAll() {
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var nodes = treeObj.getCheckedNodes(true);
	/* var msg = "name--id--pid\n"; */
	var msg = "";
	for (var i = 0; i < nodes.length; i++) {
		/* msg += nodes[i].name+"--"+nodes[i].id+"--"+nodes[i].pId+"\n"; */
		msg += nodes[i].name + ",";
	}
	// alert(msg);
	if (msg.length > 0)
		msg = msg.substring(0, msg.length - 1);
	var cityObj = $("#key" + index);
	cityObj.attr("value", msg);
}

function setValue(){
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    var value = $.trim($("#code" + index).val());
    var  msg = value.split(",");
    var nodeList1 = "";
    l=msg.length;
    if(l>0){
	    for (var i=0; i<l; i++) {
		    nodeList1 = treeObj.getNodeByParam("id", msg[i]);
		    if(nodeList1!=null){
		    	treeObj.checkNode(nodeList1,true);
		    }
	    }
    }
}
var cleararr=[];
function clearValue(){
	if(cleararr.length>0){
		var clearstr=cleararr[index];
		var j=clearstr.split(",");
		for(var i=0;i<j.length;i++){
			$("#key"+j[i]).val("");
			$("#code"+j[i]).val("");
		}
	}
}