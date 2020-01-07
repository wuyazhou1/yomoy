
var setting = {
	check: {
		enable: true,
		chkDisabledInherit: true,
		chkboxType : {"Y" : "ps", "N" : "ps"}
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	view : {
		dblClickExpand : false
	},
	callback : {
		beforeClick : beforeClick,
		onCheck : onCheck,
		onExpand:onExapand
	}
};

var TreeRecord = 1;
var zNodesCheckID={};//校验id集合对象
var zNodesCheckName={};//校验name集合对象
var treeDemoUL;//判断用那种树
var initBool;//判断是否初始化层级树
var selectTake=0;//记录层级位置

 var borderFlag = true; 
function onExapand(event,treeId,treeNode){
	var max_aWidth = 129;
	$("#treeDemo>li>ul>li>a").each(function(i){
		if($(this).width()>max_aWidth)
			max_aWidth = $(this).width();
	});
	if(max_aWidth>129 &&borderFlag){
		$("#treeDemo").width($("#treeDemo").width()+max_aWidth-85);
		borderFlag = false;
	}
	$("#menuContent>:first-child").width($("#tree_container").width());
}

function cancelLinkageCheckbox(){
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	if($("#cancelLinkage").val()=="0"){
		zTree.setting.check.chkboxType =  { "Y" : "ps", "N" : "ps" };
		setting.check.chkboxType={ "Y" : "ps", "N" : "ps" };
	}else if($("#cancelLinkage").val()=="1"){
		zTree.setting.check.chkboxType =   { "Y" : "s", "N" : "s" };
		setting.check.chkboxType={ "Y" : "s", "N" : "s" };
	}
}
function showTreeNode(i,ztreeNodes,UL){
	if(ztreeNodes!=null)
		zNodes=ztreeNodes;
	if($("#screenZtreeNameAnNiu").length>0){
		selectTake=$("#screenZtreeNameAnNiu").val();	
	}
	zNodesCheckID={};
	zNodesCheckName={};
	
	if($("#menuContent")[0].style.display=="none" || $("#menuContent")[0].name!=i){
		if(UL!=null)
			if(UL=="common"){
				createDatasearchZtreeGeneral(i);
			}else if(UL=="alliance"){
				createDatasearchZtreeCancelLinkage(i);
			}else if(UL=="level"){
				createDatasearchZtreeLevel(i);
			}
		TreeRecord = 0;
	}else{
		TreeRecord=1;
	}
	
	index = i;
	TreeRecord = 0;
	$.fn.zTree.destroy("treeDemo");
	$("#screenZtreeNameAnNiu").val(selectTake);	
	$.fn.zTree.init($("#treeDemo"), setting,filter());
	var cityOffset = $("#" + index);
	var cityObj = $("#" + index);
	$("#menuContent").css({
		left : cityOffset.offset().left + "px",
		top : cityOffset.offset().top + cityOffset.outerHeight() + "px"
	})
	//$("#menuContent").css("display","none");
}
var jiru;
var nodeCheckBool=0;
function changeZtreeV2(V2Nodes){
	V2Nodes.split(",")
	var nodessBool=true;
	var zNodesStr=[];
	if(TreeRecord==0){
		var cityOffset = $("#" + index);
		var cityObj = $("#" + index);
		$("#menuContent").css({
			left : cityOffset.offset().left + "px",
			top : cityOffset.offset().top + cityObj.outerHeight() + "px"
		}).slideDown("fast");
		TreeRecord++;
	}
	if(V2Nodes.indexOf(",")<0){
		zNodesStr[0] = V2Nodes
	}else{
		if(V2Nodes.substring(V2Nodes.length-1,V2Nodes.length)==",")
			zNodesStr = V2Nodes.substring(0,V2Nodes.length-1).split(",");
		else
			zNodesStr = V2Nodes.substring(0,V2Nodes.length).split(",");
	}
	var count = V2Nodes.split(",").length;
	for(var i=0;i<zNodesStr.length;i++){
		if(zNodesCheckName[zNodesStr[i]]==null){
			searchNode(zNodesStr[i].toUpperCase());//Added by cyd for 小写搜索功能20160323
			if(nodeList.length==1 && jiru!=count){
				nodessBool=false;
				V2Nodes=$("#" + index).val().replace(zNodesStr[i],nodeList[0].name);
				inputChange(V2Nodes.toUpperCase(),'false');//Added by cyd for 小写搜索功能20160323
			}else if(jiru!=count){
				inputChange(V2Nodes.toUpperCase(),'false');//Added by cyd for 小写搜索功能20160323
			}
		}
	}
	/*Deleted by cyd for 首次无法输入20160319*/
	/*if(V2Nodes.split(",").length!=nodeCheckBool){
		nodeCheckBool=V2Nodes.split(",").length;
		inputChange(V2Nodes,'false');
	}*/
}


function closeZtreeDao(Nodess){
	TreeRecord++;
	changeZtreeV2(Nodess.value);
	inputQuery(Nodess.value,"false");
	}

function inputQuery(node,bool){
	if(bool=='true'){
		var zNodesStr = node.value.split(",");
	}else{
		var zNodesStr = node.split(",");
	}
	if(jiru==node.split(",").length){
		$("#"+index+"id").val("");
		$("#"+index).val("");
		var count=0;
		for(var i in zNodesCheckID){
			$("#"+index).val($("#"+index).val()+zNodesCheckID[i]+",");
			if($("#" + index +"id").val()=="")
				$("#" + index +"id").val($("#" + index +"id").val() + i );
			else
				$("#" + index +"id").val($("#" + index +"id").val() + "," + i );
		}
		return;
	}
	var zNodesName={};
	//赋查询值
	for(var i=0;i<zNodesStr.length;i++){
		zNodesName[zNodesStr[i]]={};
		zNodesName[zNodesStr[i]]["bool"]=false;
		zNodesName[zNodesStr[i]]["name"]=zNodesStr[i];
	}
	//查找新值
	for(var i=0;i<zNodes.length;i++){
		if(zNodesName[zNodes[i].name]!=null && zNodesCheckName[zNodes[i].name]!=null){
			zNodesName[zNodes[i].name].bool=true;
		}else if(zNodesName[zNodes[i].name]!=null){
			zNodesCheckID[zNodes[i].id]=zNodes[i].name;
			zNodesCheckName[zNodes[i].name]=zNodes[i].id;
		}else if(zNodesName[zNodes[i].id]!=null){
			zNodesCheckID[zNodes[i].id]=zNodes[i].name;
			zNodesCheckName[zNodes[i].name]=zNodes[i].id;
			//$("#" + index).val($("#" + index).val().replace(zNodesName[zNodes[i].id].name,zNodes[i].name));
			//$("#" + index +"id").val($("#" + index +"id").val() + zNodes[i].id + ",");
			zNodesName[zNodes[i].id].bool=true;
		}else{
			delete zNodesCheckID[zNodes[i].id];
			delete zNodesCheckName[zNodes[i].name];
		}
	}
	var zNodesResetID=new Array();
	var zNodesResetName=new Array();
	for(var i=0;i<zNodesStr.length;i++){//
		if(zNodesCheckName[zNodesStr[i]]!=null && zNodesCheckID[zNodesCheckName[zNodesStr[i]]]!=null){
			zNodesResetID[zNodesCheckName[zNodesStr[i]]] = zNodesStr[i];
			zNodesResetName[zNodesResetName] = zNodesCheckName[zNodesStr[i]];
		}else if(zNodesCheckName[zNodesCheckID[zNodesStr[i]]]!=null && zNodesCheckID[zNodesStr[i]]!=null){
			zNodesResetID[zNodesStr[i]] = zNodesCheckID[zNodesStr[i]];
			zNodesResetName[zNodesCheckID[zNodesStr[i]]] = zNodesStr[i];
			
		}
	}
	$("#" + index).val("");
	$("#" + index +"id").val("");
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var nodess = treeObj.getNodes();
	for(var i in zNodesResetID){
		for(var j=0;j<nodess.length;j++){
			if(zNodesResetID[nodess[j].id]!=null)
				treeObj.checkNode(nodess[j], true, null);
			else
				treeObj.checkNode(nodess[j], false, null);
		}
		var node = treeObj.getNodeByParam(i, zNodesResetID[i], null);
		if(i!=""){
			$("#" + index).val($("#" + index).val() + zNodesResetID[i] + ",");
			if($("#" + index +"id").val()=="")
				$("#" + index +"id").val($("#" + index +"id").val() + i );
			else
				$("#" + index +"id").val($("#" + index +"id").val() + "," + i );
		}
	}
	nodeCheckBool = $("#" + index).val().split(",").length;
}


function inputChange(node,bool){
	if(bool=='true'){
		var zNodesStr = node.value.split(",");
	}else{
		var zNodesStr = node.split(",");
	}
	var zNodesName={};
	//赋查询值
	for(var i=0;i<zNodesStr.length;i++){
		zNodesName[zNodesStr[i]]={};
		zNodesName[zNodesStr[i]]["bool"]=false;
		zNodesName[zNodesStr[i]]["name"]=zNodesStr[i];
	}
	//查找新值
	for(var i=0;i<zNodes.length;i++){
		if(zNodesName[zNodes[i].name]!=null && zNodesCheckName[zNodes[i].name]!=null){
			zNodesName[zNodes[i].name].bool=true;
		}else if(zNodesName[zNodes[i].name]!=null){
			zNodesCheckID[zNodes[i].id]=zNodes[i].name;
			zNodesCheckName[zNodes[i].name]=zNodes[i].id;
		}else if(zNodesName[zNodes[i].id]!=null){
			zNodesCheckID[zNodes[i].id]=zNodes[i].name;
			zNodesCheckName[zNodes[i].name]=zNodes[i].id;
			//$("#" + index).val($("#" + index).val().replace(zNodesName[zNodes[i].id].name,zNodes[i].name));
			//$("#" + index +"id").val($("#" + index +"id").val() + zNodes[i].id + ",");
			zNodesName[zNodes[i].id].bool=true;
		}else{
			delete zNodesCheckID[zNodes[i].id];
			delete zNodesCheckName[zNodes[i].name];
		}
	}
	var zNodesResetID=new Array();
	var zNodesResetName=new Array();
	var inputValArr = new Array();//放进inpu的value
	for(var i=0;i<zNodesStr.length;i++){//
		//检查zNodesStr[i]在inputValArr中是否存在
		var repeatFlag = false;//是否有重复元素标志
		for(var j=0;j<zNodesResetID.length;j++){
			if(zNodesResetID[j]==zNodesCheckName[zNodesStr[i]]){//已有该节点，
				repeatFlag = true;
				break;//无需添加
			}
			else if(zNodesResetID[j]==zNodesStr[i]){//已有该节点，
				repeatFlag = true;
				break;//无需添加
			}
			if(repeatFlag)	break;//有重复元素，跳出循环
		}
		if(repeatFlag) continue;//有重复元素，无需执行下面代码添加，直接跳入i+1
		if(zNodesCheckName[zNodesStr[i]]!=null && zNodesCheckID[zNodesCheckName[zNodesStr[i]]]!=null){
			zNodesResetID.push(zNodesCheckName[zNodesStr[i]]) ;
			zNodesResetName[zNodesResetName] = zNodesCheckName[zNodesStr[i]];
			inputValArr.push(zNodesStr[i]);//按原顺序塞进数组里
		}else if(zNodesCheckName[zNodesCheckID[zNodesStr[i]]]!=null && zNodesCheckID[zNodesStr[i]]!=null){
			zNodesResetID.push(zNodesStr[i]);
			zNodesResetName[zNodesCheckID[zNodesStr[i]]] = zNodesStr[i];
			inputValArr.push(zNodesCheckID[zNodesStr[i]]);//按原顺序塞进数组里
		}
	}
	$("#" + index).val("");
	$("#" + index +"id").val("");
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var nodess = treeObj.getNodes();
	for(var i =0;i< zNodesResetID.length;i++){
		
		for(var j=0;j<nodess.length;j++){
			if(zNodesResetID[i]==nodess[j].id)
				treeObj.checkNode(nodess[j], true, null);
			else
				treeObj.checkNode(nodess[j], false, null);
		}
		//var node = treeObj.getNodeByParam(i, zNodesResetID[i], null);
		if($("#" + index +"id").val()=="")
			$("#" + index +"id").val($("#" + index +"id").val() + zNodesResetID[i] );
		else
			$("#" + index +"id").val($("#" + index +"id").val() + "," + zNodesResetID[i] );
	}
	/*Begin:Added by cyd for 组装input的value值20160323*/
	var value="";
	for(var i=0;i<inputValArr.length;i++){
		value = value+inputValArr[i]+",";//按序取出放入input
	}
	$("#" + index).val(value);
	/*End:Added by cyd for 组装input的value值20160323*/
	
	nodeCheckBool = $("#" + index).val().split(",").length;
	$.fn.zTree.init($("#treeDemo"), setting,filter());
}
function onCheck(e, treeId, treeNode) {
	
	if(treeNode.checked){
		if($("#" + index).val().indexOf(treeNode.name+",")!=-1){
			return;
		}
		$("#" + index).val($("#" + index).val() + treeNode.name + ",");
		if($("#" + index +"id").val()=="")
			$("#" + index +"id").val($("#" + index +"id").val() + treeNode.id );
		else
			$("#" + index +"id").val($("#" + index +"id").val() + "," + treeNode.id );
		//$("#" + index +"id").val($("#" + index +"id").val() + treeNode.id + ",");
		zNodesCheckName[treeNode.name]=treeNode.id;
		zNodesCheckID[treeNode.id]=treeNode.name;
		zNodesId[treeNode.id]=true;
	}else{
		$("#" + index).val($("#" + index).val().replace(treeNode.name + "," , ""));
		if($("#" + index +"id").val().indexOf(",")==-1)
			$("#" + index +"id").val($("#" + index +"id").val().replace(treeNode.id , ""));
		else if($("#" + index +"id").val().indexOf(treeNode.id+",")!=-1 && $("#" + index +"id").val().indexOf(","+treeNode.id)==-1)
			$("#" + index +"id").val($("#" + index +"id").val().replace(treeNode.id + "," , ""));
		else
			$("#" + index +"id").val($("#" + index +"id").val().replace("," + treeNode.id , ""));
		//$("#" + index +"id").val($("#" + index +"id").val().replace(treeNode.id + "," , ""));
		delete zNodesCheckName[treeNode.name];
		delete zNodesCheckID[treeNode.id];
		zNodesId[treeNode.id]=false;
	}
	if($.fn.zTree.getZTreeObj("treeDemo").setting.check.chkboxType.N=="ps"){
		parentCheck(treeNode);
		childrenCheck(treeNode);
	}else if($.fn.zTree.getZTreeObj("treeDemo").setting.check.chkboxType.N=="s"){
		childrenCheck(treeNode);
	}
	jiru = $("#" + index).val().split(",").length;
	nodeCheckBool = $("#" + index).val().split(",").length;
}
function childrenCheck(Nodes){
	if(Nodes.children!=null){
		var childrenNames = Nodes.children;
		for(var i=0;i<childrenNames.length;i++){
			if(childrenNames[i].checked){
				if(zNodesCheckID[childrenNames[i].id]==null){
					$("#" + index).val($("#" + index).val() + childrenNames[i].name + ",");
					if($("#" + index +"id").val()=="")
						$("#" + index +"id").val($("#" + index +"id").val() + childrenNames[i].id );
					else
						$("#" + index +"id").val($("#" + index +"id").val() + "," + childrenNames[i].id );
					//$("#" + index +"id").val($("#" + index +"id").val() + childrenNames[i].id + ",");
					zNodesCheckName[childrenNames[i].name]=childrenNames[i].id;
					zNodesCheckID[childrenNames[i].id]=childrenNames[i].name;
				}
			}else{
				if(zNodesCheckID[childrenNames[i].id]!=null){
					$("#" + index).val($("#" + index).val().replace(childrenNames[i].name + "," , ""));
					if($("#" + index +"id").val().indexOf(",")==-1)
						$("#" + index +"id").val($("#" + index +"id").val().replace(childrenNames[i].id , ""));
					else
						$("#" + index +"id").val($("#" + index +"id").val().replace("," + childrenNames[i].id , ""));
					//$("#" + index +"id").val($("#" + index +"id").val().replace(childrenNames[i].id + "," , ""));
					delete zNodesCheckName[childrenNames[i].name];
					delete zNodesCheckID[childrenNames[i].id];
				}
			}
			if(childrenNames[i].children!=null){
				childrenCheck(childrenNames[i]);
			}
		}
	}
}
function parentCheck(Nodes){
	if(Nodes.getParentNode()!=null){
		var parentNodes = Nodes.getParentNode();
		if(parentNodes.checked){
			if(zNodesCheckID[parentNodes.id]==null){
				$("#" + index).val($("#" + index).val() + parentNodes.name + ",");
				if($("#" + index +"id").val()=="")
						$("#" + index +"id").val($("#" + index +"id").val() + parentNodes.id );
				else
						$("#" + index +"id").val($("#" + index +"id").val() + "," + parentNodes.id );
				//$("#" + index +"id").val($("#" + index +"id").val() + parentNodes.id + ",");
				zNodesCheckName[parentNodes.name]=parentNodes.id;
				zNodesCheckID[parentNodes.id]=parentNodes.name;
			}
		}else{
			if(zNodesCheckID[parentNodes.id]!=null){
				$("#" + index).val($("#" + index).val().replace(parentNodes.name + "," , ""));
				if($("#" + index +"id").val().indexOf(",")==-1)
						$("#" + index +"id").val($("#" + index +"id").val().replace(parentNodes.id , ""));
				else
						$("#" + index +"id").val($("#" + index +"id").val().replace("," + parentNodes.id , ""));
				//$("#" + index +"id").val($("#" + index +"id").val().replace(parentNodes.id + "," , ""));
				delete zNodesCheckName[parentNodes.name];
				delete zNodesCheckID[parentNodes.id];
			}
		}
		parentCheck(parentNodes);
	}
}

var selectnodesArr = [];
function vaildataztree(){
	
	
	
	$("#" + index).val("");
	$("#" + index +"id").val("");
	zNodesCheckID={};
	zNodesCheckName={};
	initBool=true;
	$.fn.zTree.init($("#treeDemo"), setting,filter());
	
	
}
var zNodesId={};
function filter(treeId, parentNode, childNodes) {
	zNodesCheckID={};
	zNodesCheckName={};
	var strName=[];
	var strId=[];
	if($("#" + index).val()!="")
		strName = $("#" + index).val().substring(0,$("#" + index).val().length-1).split(",");
	if($("#" + index +"id").val()!="")
		strId = $("#" + index +"id").val().split(",");
	//var strId = $("#" + index +"id").val().substring(0,$("#" + index +"id").val().length-1).split(",");
	jiru = $("#" + index).val().split(",").length;
	if(strId[0]!=null){
		for(var i=0;i<strId.length;i++){
			zNodesCheckID[strId[i]]=strName[i];
			zNodesCheckName[strName[i]]=strId[i];
		}
	}
	if (!zNodes) return null;
	for (var i=0, l=zNodes.length; i<l; i++) {
		zNodes[i].name = zNodes[i].name.replace(/\.n/g, '.');
		if(zNodesCheckID[zNodes[i].id]==null){
			//zNodesId[zNodes[i].id]=zNodes[i].nocheck;
			zNodes[i]["checked"]=false;
		}else{
			//zNodes[i]["checked"] = zNodesId[zNodes[i].id];
			zNodes[i]["checked"] = true;
		}
	}
	
	
	if($("#screenZtreeNameAnNiu").length>0){
		var vailDatazNodes=[];
		var vailCount=0;
		for(var i=0;i<zNodes.length;i++){
			if($("#screenZtreeNameAnNiu").val()=="0"){
				vailDatazNodes[vailCount]=zNodes[i];
				if(zNodes[i].portType=='z' || zNodes[i].nocheck==true){
					vailDatazNodes[vailCount].nocheck="true";
				}else if(initBool){
					vailDatazNodes[vailCount].checked="true";
					$("#" + index).val($("#" + index).val() + zNodes[i].name + ",");
					$("#" + index +"id").val($("#" + index +"id").val() + zNodes[i].id + ",");
					zNodesCheckName[zNodes[i].name]=zNodes[i].id;
					zNodesCheckID[zNodes[i].id]=zNodes[i].name;
				}
				vailCount++;
			}else{ 
				if(zNodes[i].portType==$("#screenZtreeNameAnNiu").val() && initBool){
					vailDatazNodes[vailCount]=zNodes[i];
					vailDatazNodes[vailCount].isParent="false";
					vailDatazNodes[vailCount].nocheck="false";
					//true 表示此节点不显示 checkbox / radio，不影响勾选的关联关系，不影响父节点的半选状态。
					//false 表示节点具有正常的勾选功能
				  //vailDatazNodes[vailCount].checked="true";
					//true 表示节点的输入框被勾选,false 表示节点的输入框未勾选
					//$("#" + index).val($("#" + index).val() + zNodes[i].name + ",");
					//Modified by cyd for 切换select时不选中20160408
					if(zNodes[i].checked){//Added by cyd for梅沙行政切换时id域未清空20160408
						if($("#" + index +"id").val()=="")
							$("#" + index +"id").val($("#" + index +"id").val() + zNodes[i].id );
						else
							$("#" + index +"id").val($("#" + index +"id").val() + "," + zNodes[i].id );
						//$("#" + index +"id").val($("#" + index +"id").val() + zNodes[i].id + ",");
					}
					zNodesCheckName[zNodes[i].name]=zNodes[i].id;
					zNodesCheckID[zNodes[i].id]=zNodes[i].name;
					vailCount++;
				}else if(zNodes[i].portType==$("#screenZtreeNameAnNiu").val() ){
					vailDatazNodes[vailCount]=zNodes[i];
					vailDatazNodes[vailCount].isParent="false";
					vailDatazNodes[vailCount].nocheck="false";
					zNodes[i].name = zNodes[i].name.replace(/\.n/g, '.');
					if(zNodesCheckID[zNodes[i].id]==null){
						//zNodesId[zNodes[vailCount].id]=zNodes[i].nocheck;
						vailDatazNodes[vailCount]["checked"]=false;
					}else{
						//zNodes[i]["checked"] = zNodesId[zNodes[i].id];
						vailDatazNodes[vailCount]["checked"] = true;
					}
					vailCount++;
				}
			}
		}
		return vailDatazNodes;
	}
	
	
	if($("#screenZtreeNameAnNiusss").length>0){
		var vailDatazNodes=[];
		var vailCount=0;
		for(var i=0;i<zNodes.length;i++){
			if($("#screenZtreeNameAnNiusss").val()=="0"){
				vailDatazNodes[vailCount]=zNodes[i];
				if(zNodes[i].portType=='z' || zNodes[i].nocheck==true){
					
				}else if(initBool){
					zNodesCheckName[zNodes[i].name]=zNodes[i].id;
					zNodesCheckID[zNodes[i].id]=zNodes[i].name;
				}
				vailCount++;
			}else{ 
				if(zNodes[i].portType==$("#screenZtreeNameAnNiusss").val() && initBool){
					vailDatazNodes[vailCount]=zNodes[i];
					vailDatazNodes[vailCount].isParent="false";
					vailDatazNodes[vailCount].nocheck="false";
					
					//$("#" + index).val($("#" + index).val() + zNodes[i].name + ",");
					//if($("#" + index +"id").val()=="")
						//$("#" + index +"id").val($("#" + index +"id").val() + zNodes[i].id );
					//else
						//$("#" + index +"id").val($("#" + index +"id").val() + "," + zNodes[i].id );
					//$("#" + index +"id").val($("#" + index +"id").val() + zNodes[i].id + ",");//deleted by 瑶哥 20160328梅沙
					zNodesCheckName[zNodes[i].name]=zNodes[i].id;
					zNodesCheckID[zNodes[i].id]=zNodes[i].name;
					vailCount++;
				}else if(zNodes[i].portType==$("#screenZtreeNameAnNiusss").val()){
					vailDatazNodes[vailCount]=zNodes[i];
					vailDatazNodes[vailCount].isParent="false";
					vailDatazNodes[vailCount].nocheck="false";
					zNodes[i].name = zNodes[i].name.replace(/\.n/g, '.');
					if(zNodesCheckID[zNodes[i].id]==null){
						//zNodesId[zNodes[vailCount].id]=zNodes[i].nocheck;//deleted by 瑶哥 20160328梅沙
						vailDatazNodes[vailCount]["checked"]=false;
					}
					vailCount++;
				}
			}
		}
		return vailDatazNodes;
	}
	return zNodes;
}





$(document).ready(function() {
	$("#btn_CheckAllNodes").click(CheckAllNodes);//全选
	$("#btn_CancelAllNodes").click(CancelAllNodes);//取消
	$("#returnCheckAllNodes").click(returnCheckAllNodes);//反选
	
	$("#menuContent").hide();
	//监听eventOne事件 
	addEventHandler(parent,"click",hiddenDiv);
	addEventHandler(this,"click",hiddenDiv);
 	if($("iframe").length>0){
 		for(var i=0;i<$("iframe").length;i++){
 			addEventHandler($("iframe")[0].contentWindow,"click",hiddenDiv);
 		}
 	}
});
function hiddenDiv(){
		if($("#menuContent").length<1)
			return;
		if(event==null){
			$("#menuContent").css("display","none");
			return;
		}
		if(initBool){
			initBool=false;
			return;
		}
		var y=event.clientY;
		var x=event.clientX;
		var divx1 = $("#menuContent").offset().left;
		var divy1 = $("#menuContent").offset().top-30;

		var divx2 = $("#menuContent").offset().left + ($("#menuContent").width()>($("#"+index).width()+29)?$("#menuContent").width():($("#"+index).width()+29));
		var divy2 = $("#menuContent").offset().top + $("#menuContent>div").eq(0).height()+$("#tree_container").height()-1;
		
		if( x < divx1 || x > divx2 || y < divy1  || y > divy2 ){
			if($("#menuContent").css("display")!="none"){
				$(".nj_button[value^='关闭']").click();
				if($("#menuContent").css("display")=="none"){
      		if($(".nj_button[value='方法']").length>0)
      			$(".nj_button[value='方法']").click();
      	}
      	}
            //$("#menuContent").css("display","none");
      	}
		
}




function beforeClick(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

var lastValue = "", nodeList = [], fontCss = {};
var inputStrGlobalR="";//文本框输入值，保存下来，以前删除了
function searchNode(str) {
	inputStrGlobalR = str;
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    if(/.*[\u4e00-\u9fa5]+.*$/.test(str)){
  	  nodeList = zTree.getNodesByParamFuzzy("name", str);
    }else{
  	  nodeList = zTree.getNodesByParamFuzzy("id", str);
    }
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
	var Nodess = treeObj.getNodes();
	vailCheckAllNodes(Nodess);
}
//校验是否全选
function vailCheckAllNodes(Nodes){
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	for(var i=0;i<Nodes.length;i++){
		if(!Nodes[i].checked && (Nodes[i].nocheck==null || !Nodes[i].nocheck) && (Nodes[i].chkDisabled==null || !Nodes[i].chkDisabled)){
			$("#" + index).val($("#" + index).val() + Nodes[i].name + ",");
			if($("#" + index +"id").val()=="")
				$("#" + index +"id").val($("#" + index +"id").val() + Nodes[i].id );
			else
				$("#" + index +"id").val($("#" + index +"id").val() + "," + Nodes[i].id );
			//$("#" + index +"id").val($("#" + index +"id").val() + Nodes[i].id + ",");
			zNodesCheckName[Nodes[i].name]=Nodes[i].id;
			zNodesCheckID[Nodes[i].id]=Nodes[i].name;
			treeObj.checkNode(Nodes[i], true, false);
		}
		if(Nodes[i].children!=null){
			vailCheckAllNodes(Nodes[i].children);
		}
	}
}
// 全取消
function CancelAllNodes() {
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	treeObj.checkAllNodes(false);
	zNodesCheckID={};//校验id集合对象
	zNodesCheckName={};//校验name集合对象
	$("#"+index).val("");
	$("#"+index+"id").val("");
}

// 反选
function returnCheckAllNodes() {
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var Nodess = treeObj.getNodes();
	vailReturnCheckAllNodes(Nodess);
}
//校验反选
function vailReturnCheckAllNodes(Nodes){
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	for(var i=0;i<Nodes.length;i++){
			
		if(Nodes[i].checked && (Nodes[i].nocheck==null || !Nodes[i].nocheck)){
			$("#" + index).val($("#" + index).val().replace(Nodes[i].name + "," , ""));
			if($("#" + index +"id").val().indexOf(",")==-1)
				$("#" + index +"id").val($("#" + index +"id").val().replace(Nodes[i].id , ""));
			else if($("#" + index +"id").val()!=""){
				$("#" + index +"id").val($("#" + index +"id").val().replace(Nodes[i].id+"," , ""));
			}else
				$("#" + index +"id").val($("#" + index +"id").val().replace("," + Nodes[i].id , ""));
			//$("#" + index +"id").val($("#" + index +"id").val().replace(Nodes[i].id + "," , ""));
			delete zNodesCheckName[Nodes[i].name];
			delete zNodesCheckID[Nodes[i].id];
			treeObj.checkNode(Nodes[i], false, false);
		}else if(Nodes[i].nocheck==null || !Nodes[i].nocheck){
			$("#" + index).val($("#" + index).val() + Nodes[i].name + ",");
			if($("#" + index +"id").val()=="")
				$("#" + index +"id").val($("#" + index +"id").val() + Nodes[i].id );
			else
				$("#" + index +"id").val($("#" + index +"id").val() + "," + Nodes[i].id );
			//$("#" + index +"id").val($("#" + index +"id").val() + Nodes[i].id + ",");
			zNodesCheckName[Nodes[i].name]=Nodes[i].id;
			zNodesCheckID[Nodes[i].id]=Nodes[i].name;
			treeObj.checkNode(Nodes[i], true, false);
		}
		if(Nodes[i].children!=null){
			vailReturnCheckAllNodes(Nodes[i].children);
		}
	}
}
//默认获取焦点事件
function onfocusdiv(e){
	if($("#menuContent")[0].name!=e.id){//判断是否是同一个文本框
		$(e.parentNode).find("a").mousedown();
	}
}
//隐藏
function closediv() {
	if($("#screenZtreeNameAnNiu").length>0){
		selectTake=$("#screenZtreeNameAnNiu").val();	
	}
	$("#menuContent").css("display","none");
	max_aWidth = 129; 
	//$("#menuContent").fadeOut("fast");
}
var index = "";
//初始化
function showdiv(i,ztreeNodes,UL) {
	if($("#"+index).length>0 && i!=index)//Added by cyd for 文本框不能获得异常20160326
		$("#"+index).blur();
	if(ztreeNodes!=null)
		zNodes=ztreeNodes;
	if($("#screenZtreeNameAnNiu").length>0){
		selectTake=$("#screenZtreeNameAnNiu").val();	
	}
	TreeRecord++;
	zNodesCheckID={};
	zNodesCheckName={};
	$.fn.zTree.destroy("treeDemo");
	if(UL!=null)
		if(UL=="common"){
			createDatasearchZtreeGeneral(i);
		}else if(UL=="alliance"){
			createDatasearchZtreeCancelLinkage(i);
		}else if(UL=="level"){
			createDatasearchZtreeLevel(i);
		}
	//$("#menuContent").blur(colseDivHidden);
	//addEventHandler($("#menuContent")[0],"onblur",hiddenDiv);
	index = i;
	$("#screenZtreeNameAnNiu").val(selectTake);	
	$.fn.zTree.init($("#treeDemo"), setting , filter());
	var cityOffset = $("#" + index);
	var cityObj = $("#" + index +"id");
	$("#menuContent").css({
		left : cityOffset.offset().left + "px",
		top : cityOffset.offset().top + cityOffset.outerHeight() + "px"
	});//.slideDown("fast")
	$("#menuContent")[0].style.display="block";
	$("#menuContent>:first-child").width($("#tree_container").width());
	setValue();
}

function ztreeChecked(ids,str,node){
        	InitZtree(ids,str,node);
        	CheckAllNodes();
        	closediv();
}
function clickselect(){
	initBool=true;
}
function InitZtree(i,ztreeNodes,UL){
	if(ztreeNodes!=null)
		zNodes=ztreeNodes;
	TreeRecord++;
	zNodesCheckID={};
	zNodesCheckName={};
	$.fn.zTree.destroy("treeDemo");
	if(UL!=null)
		if(UL=="common"){
			createDatasearchZtreeGeneral();
		}else if(UL=="alliance"){
			createDatasearchZtreeCancelLinkage();
		}else if(UL=="level"){
			createDatasearchZtreeLevel();
		}
	
	index = i;
	$.fn.zTree.init($("#treeDemo"), setting , filter());
	var cityOffset = $("#" + index);
	var cityObj = $("#" + index +"id");
	$("#menuContent").css({
		left : cityOffset.offset().left + "px",
		top : cityOffset.offset().top + cityOffset.outerHeight()+2000 + "px"
	});//.slideDown("fast")
	$("#menuContent")[0].style.display="block";
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
	var cityObj = $("#" + index);
	cityObj.attr("value", msg);
}

function setValue(){
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    var value = $.trim($("#" + index +"id").val());
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
/*var cleararr=[];
function clearValue(){
	if(cleararr.length>0){
		var clearstr=cleararr[index];
		var j=clearstr.split(",");
		for(var i=0;i<j.length;i++){
			$("#key"+j[i]).val("");
			$("#code"+j[i]).val("");
		}
	}
}*/
//判断i在arr数组中是否存在
function arrexist(arr,i){
	for(var k=0;k<arr.length;k++){
		if(arr[k]==i){
			return true;
		}
	}
	return false;
}

/**********************************************************
 * addResize给弹出树框添加拖拽功能
 * Added by  cyd for jqueryui 拖拽公共方法20160330
 * 依赖jquery ui.js和css
 *
 **********************************************************/
function addResize(){
	if($("#menuContent").resizable){
		$("#menuContent").resizable({
			minWidth:200,
			minHeight:250,
			maxWidth:800,
			maxHeight:500,
			handles:"e",
			aspectRatio:false, //是否按照比例缩放
			resize:function(event,ui){
				//$("#treeDemo").height($("#menuContent").height()-24);
				$("#menuContent").height($("#menuContent"));
			}
		});
	}
}

//普通ztree树
function createDatasearchZtreeGeneral(){
	$("#menuContent").remove();
	if($("#menuContent").length!=0)
		return;
	var html_str = '<div id="menuContent" name="'+name+'"  class="menuContent ui-widget-content" style="display:none;position: absolute;letter-spacing:5px;z-index:2">';
        html_str += '<div ><input id="btn_CheckAllNodes" type="button" onclick="CheckAllNodes()"  value="全选" class="nj_button"/>';

        html_str += '<input type="button" id="btn_CancelAllNodes" onclick="CancelAllNodes()" value="取消" class="nj_button"/>';

        html_str += '<input type="button" id="returnCheckAllNodes" onclick="returnCheckAllNodes()" value="反选" class="nj_button"/>';

        html_str += '<input type="button" onclick="closediv()" value="关闭" class="nj_button"/></div>';
       
        html_str += '<div id="tree_container" style=""><ul id="treeDemo" class="ztree" style="display:block;margin-top:1px;margin-left:0px;z-index:3;  height: 300px;overflow-y: auto;"></ul></div>';
    
        html_str += '</div>';
    $("body").append(html_str);
    $("#menuContent")[0]["name"]=name;
    
    addResize();//added by cyd for 添加拖拽20160331
    borderFlag = true;
}

//级联树
function createDatasearchZtreeCancelLinkage(name){
	$("#menuContent").remove();
	if($("#menuContent").length!=0)
		return;
	var html_str = '<div id="menuContent" name="'+name+'" class="menuContent menuContent1 ui-widget-content" style="display:none;position: absolute;letter-spacing:5px;z-index:2">';
        html_str += '<div><input id="btn_CheckAllNodes" type="button"  onclick="CheckAllNodes()" value=" 全选" class="nj_button" style="margin-left: 2px;"/>';

        html_str += '<input type="button" id="btn_CancelAllNodes" onclick="CancelAllNodes()" value="取消" class="nj_button" style="margin-left: 2px;"/>';

        html_str += '<input type="button" id="returnCheckAllNodes" onclick="returnCheckAllNodes()" value="反选 " class="nj_button" style="margin-left: 2px;"/>';

        html_str += '<input type="button" onclick="closediv()" value="关闭 " class="nj_button" style="margin-left: 2px;"/>';
       	
       	html_str += '<select id="cancelLinkage" style="padding: 0px!important;float: right;margin-top: 2px;margin-left:6px" onchange="cancelLinkageCheckbox()">';
          	html_str += '<option value="0">级联</option>';
          	html_str += '<option value="1">多选</option>';
        html_str += '</select></div>';
       	
        html_str += '<div id="tree_container" style=""><ul id="treeDemo" class="ztree" style="display:block;margin-top:5px;margin-left:0px; z-index:3;  height: 255px;overflow-y: auto;"></ul></div>';
    html_str += '</div>';
    $("body").append(html_str);
    $("#menuContent")[0]["name"]=name;
    
     addResize();//added by cyd for 添加拖拽20160331
     borderFlag = true;
}

//层级树
function createDatasearchZtreeLevel(name){
	$("#menuContent").remove();
	if($("#menuContent").length!=0)
		return;
	var html_str = '<div id="menuContent" name="'+name+'"  class="menuContent menuContent1 ui-widget-content" style="display:none;position: absolute;letter-spacing:5px;z-index:2">';
        html_str += '<div><input id="btn_CheckAllNodes" type="button"  onclick="CheckAllNodes()" value=" 全选" class="nj_button"/>';

        html_str += '<input type="button" id="btn_CancelAllNodes" onclick="CancelAllNodes()" value="取消" class="nj_button"/>';

        html_str += '<input type="button" id="returnCheckAllNodes" onclick="returnCheckAllNodes()" value="反选 " class="nj_button"/>';

        html_str += '<input type="button" onclick="closediv()" value="关闭" class="nj_button"/>';
       	
        html_str += '<select id="screenZtreeNameAnNiu"  onclick="clickselect()" style="float: right;margin-top: 0px;" onchange="vaildataztree()">';
          	html_str += '<option value="0" >全部口岸</option>';
          	html_str += '<option value="z" >总站</option>';
          	html_str += '<option value="a" >空港口岸</option>';
          	html_str += '<option value="b" >海港口岸</option>';
          	html_str += '<option value="c" >陆港口岸</option>';
        html_str += '</select></div>';
        
        html_str += '<div id="tree_container" style=""><ul id="treeDemo" class="ztree" style="display:block;margin-left:0px;z-index:3;  height: 255px;overflow-y: auto;"></ul></div>';
    html_str += '</div>';
    $("body").append(html_str);
    $("#menuContent")[0]["name"]=name;
    
    addResize();//added by cyd for 添加拖拽20160331
     borderFlag = true;
}

//初始化树参数
function createNameInputZtree(node1,node2,node3,node4,node5,node6,node7,node8,node9,node10,node11,node12,node13,node14,node15,node16,node17,node18,node19,node20,node21,node22,node23,node24,node25,node26,node27,node28,node29,node30){
	var node=[node1,node2,node3,node4,node5,node6,node7,node8,node9,node10,node11,node12,node13,node14,node15,node16,node17,node18,node19,node20,node21,node22,node23,node24,node25,node26,node27,node28,node29,node30];
	for(var i=0;i<node.length;i++){
		if($("#"+node[i]).length==0)
			return;
		var html_str = $("#"+node[i])[0].outerHTML.substring(0,6);
		html_str += " oninput='changeZtreeV2(this.value)'";
		if($("#"+node[i])[0].outerHTML.indexOf("onfocus")==-1){
			html_str += " onfocus='onfocusdiv("+node[i]+")'"
		}
		//html_str += " onfocus='colseDivHidden(this)'";
		html_str += " onblur='closeZtreeDao(this)'";
		html_str += $("#"+node[i])[0].outerHTML.substring(6);
		$("#"+node[i])[0].outerHTML=html_str;
	}
}

//初始化参数
function createNameInputZtreev2(node){
	for(var i=0;i<node.length;i++){
		if($("#"+node[i]).length==0)
			return;
		var html_str = $("#"+node[i])[0].outerHTML.substring(0,6);
		html_str += " oninput='changeZtreeV2(this.value)'";
		if($("#"+node[i])[0].outerHTML.indexOf("onfocus")!=-1){
			html_str += " onfocus='onfocusdiv("+node[i]+")'"
		}
		//html_str += " onfocus='showTree(\""+node[i]+"\")'";
		html_str += " onblur='closeZtreeDao(this.value)'";
		html_str += $("#"+node[i])[0].outerHTML.substring(6);
		$("#"+node[i])[0].outerHTML=html_str;
	}
}
/************************************************ 
 * addEventListener:监听Dom元素的事件 
 *  
 * target：监听对象 
 * type：监听函数类型，如click,mouseover 
 * func：监听函数 
 **************************************/
function addEventHandler(target,type,func){ 
  if(target.addEventListener){ 
    //监听IE9，谷歌和火狐 
    target.addEventListener(type, func, false); 
  }else if(target.attachEvent){ 
    target.attachEvent("on" + type, func); 
  }else{ 
    target["on" + type] = func; 
  }  
} 
/* 
 * removeEventHandler:移除Dom元素的事件 
 *  
 * target：监听对象 
 * type：监听函数类型，如click,mouseover 
 * func：监听函数 
 */
function removeEventHandler(target, type, func) { 
  if (target.removeEventListener){ 
    //监听IE9，谷歌和火狐 
    target.removeEventListener(type, func, false); 
  } else if (target.detachEvent){ 
    target.detachEvent("on" + type, func); 
  }else { 
    delete target["on" + type]; 
  } 
} 


var setting1 = {
	check : {
		enable : true,
        chkStyle:"radio",
        radioType :"all"
		
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
		beforeClick : radioBeforeClick,
		onCheck : radioOnCheck
	}
};

var zNodesRadioID={};//校验id集合对象
var zNodesRadioName={};//校验name集合对象
var jiru;//记入逗号位置
var index = "";//记入id值
//文本框改变事件
function radiochangeZtreeV2(InputNodes){//输入框的值
	var nodes=[]; 
	if(InputNodes.indexOf(",")==-1){
		nodes[0]= InputNodes;
	}else{
		if(InputNodes.substring(InputNodes.length-1,InputNodes.length)==",")
			nodes = InputNodes.substring(0,InputNodes.length-1).split(",");
		else
			nodes = InputNodes.substring(0,InputNodes.length).split(",");
	}
	for(var i=0;i<nodes.length;i++){
		//判断是否已经选中的值
		if(zNodesRadioName[nodes[i]]==null){
			searchNodeR(nodes[i]);
			//判断赛选只有一个
			if(nodeList.length==1){
				zNodesRadioID={};//校验id集合对象
				zNodesRadioName={};//校验name集合对象
				$("#"+index).val(nodeList[0].name+",");
				$("#"+index + "id").val(nodeList[0].id);
				zNodesRadioID[nodeList[0].id]=nodeList[0].name;
				zNodesRadioName[nodeList[0].name]=nodeList[0].id;
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				treeObj.checkNode(nodeList[0], true, null)
				searchNodeR("");
			}
			//判断是否有新逗号
			if(InputNodes.split(",").length!=jiru && InputNodes!=""){
				searchNodeR(nodes[i]);
				//判断赛选只有一个
				if(nodeList.length==1){
					zNodesRadioID={};
					zNodesRadioName={};
					zNodesRadioID[nodeList[0].id]=nodeList[0].name;
					zNodesRadioName[nodeList[0].name]=nodeList[0].id;
					var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
					treeObj.checkNode(nodeList[0], true, null)
					searchNodeR("");
					for(var b in zNodesRadioName){
						$("#"+index).val(b+",");
						$("#"+index +"id").val(zNodesRadioName[b]);
					}
					break;
				}else{//判断赛选不只一个
					for(var b in zNodesRadioName){
						$("#"+index).val(b+",");
						$("#"+index + "id").val(zNodesRadioName[b]);
					}
				}
			}
		}
	}
	//判断是否有新逗号，并且刷新选中项
	if(InputNodes.split(",").length!=jiru && InputNodes!=""){
		for(var b in zNodesRadioName){
			$("#"+index).val(b+",");
			$("#"+index+"id").val(zNodesRadioName[b]);
		}
	}
	jiru = $("#"+index).val().split(",").length;
	if($("#"+index+"id").val().trim()==""){
		$("#"+index+"id").val("");
		return;
	}
}
//失去焦点事件
function radioBlurZtree(BlurNodes){
	if(BlurNodes.value.trim()==""){
		$("#"+index+"id").val("");
		jiru = $("#"+index).val().split(",").length;
		return;
	}
	//判断是否是选中
	var nodes=[]; 
	if(BlurNodes.value.indexOf(",")==-1 && BlurNodes.value.trim()!=""){
		nodes[0]= BlurNodes.value;
	}else if(BlurNodes.value.indexOf(",")!=-1){
		nodes = BlurNodes.value.split(",");
	}
	//校验是否选中的值
	for(var i=0;i<nodes.length;i++){
		if(zNodesRadioName[nodes[i]]!=null)
			continue;
		else{
			if(BlurNodes.value.indexOf(",")!=-1){
				$("#"+index).val($("#"+index).val().replace(","+nodes[i] , "") + ",");
			}else{
				$("#"+index).val($("#"+index).val().replace(nodes[i] , "") + ",");
			}
		}
	}
}


$(document).ready(function() {
	$("#btn_CancelAllNodes").click(CancelAllNodes);
	$("#returnCheckAllNodes").click(returnCheckAllNodes);
	$("#menuContent").hide();
});
/**********************************************************
 * 用于捕获单击节点之前的事件回调函数，并且根据返回值确定是否允许单击操作
 * @param treeId {对应 zTree 的 treeId，便于用户操控} 
 * @param treeNode {被单击的节点 JSON 数据对象} 
 * @return {Boolean}
 * 如果返回 false，zTree 将不会选中节点，也无法触发 onClick 事件回调函数
 **********************************************************/

function radioBeforeClick(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.checkNode(treeNode, !treeNode.checked, null,true);
	return true;
}
/****************************************************
 * 用于捕获 checkbox / radio 被勾选 或 取消勾选的事件回调函数
 * @param {} e	标准的 js event 对象
 * @param {} treeId 对应 zTree 的 treeId，便于用户操控
 * @param {} treeNode 被勾选 或 取消勾选的节点 JSON 数据对象
 *****************************************************/
function radioOnCheck(e, treeId, treeNode) {
	zNodesRadioID={};//校验id集合对象
	zNodesRadioName={};//校验name集合对象
	
	/*Begin:Deleted by cyd for no use 20160405 */
	/*var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	var nodes = zTree.getCheckedNodes(true);//true 表示获取 被勾选 的节点集合
	var v = "",code="";
	l = nodes.length
	for (var i = 0 ; i < l; i++) {
		v += nodes[i].name + ",";
		code +=nodes[i].id + ",";
	}
	if(code.length>0)
		code = code.substring(0, code.length - 1);*/
	
	/*End:Deleted by cyd for no use 20160405 */
	if (treeNode.name.length > 0){
		zNodesRadioID={};
		zNodesRadioName={};
		$("#"+index).val(treeNode.name+",");
		$("#"+index+"id").val(treeNode.id);
		zNodesRadioID[treeNode.id]= treeNode.name;
		zNodesRadioName[treeNode.name] = treeNode.id;
	}
	jiru = 1;
}


var lastValue = "", nodeList = [], fontCss = {};
var inputStrGlobal="";//文本框输入值，保存下来，以前删除了
function searchNodeR(str) { //另一棵树调用此方法，不调用searchNode Added by cyd for 瑶哥20160326
    inputStrGlobal=str;
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

// 获取所有选中节点的值
function GetCheckedAll() {
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var nodes = treeObj.getCheckedNodes(true);
	/* var msg = "name--id--pid\n"; */
	var msg = "";
	for (var i = 0; i < nodes.length; i++) {
		msg += nodes[i].name + ",";
	}
	if (msg.length > 0)
		msg = msg.substring(0, msg.length - 1);
	var cityObj = $("#" + index);
	cityObj.attr("value", msg);
}


function filter1(treeId, parentNode, childNodes) {
	if($("#"+index+"id").val()!=null){
		var data = zNodes;
		for(var i=0;i<data.length;i++){
			if(data[i].id==$("#"+index+"id").val()){
				data[i]['checked']=true;
			}
		}
	}
	return zNodes;
}
//初始化树数据
function showRadioDiv(i,ztreeData,UL) {
	$("#menuContent").fadeIn("fast");
	$.fn.zTree.destroy("treeDemo");
	zNodes = ztreeData;
    if(UL!=null)
		if(UL=="commonRadio"){
			createRadioZtreeGeneral(i);
		}
    $.fn.zTree.init($("#treeDemo"), setting1, zNodes);
	index = i;
	jiru = $("#" + i).val().split(",").length;
	// CurInput = i; //标记第一个文本框
	var cityObj = $("#" + i);
	var cityOffset = $("#" + i).offset();
	$.fn.zTree.init($("#treeDemo"), setting1 , filter1());
	
	$("#menuContent").css({
		left : cityOffset.left + "px",
		top : cityOffset.top + cityObj.outerHeight() + "px"
	}).slideDown("fast");
}
//单选ztree树
function createRadioZtreeGeneral(){
	$("#menuContent").remove();
	if($("#menuContent").length!=0)
		return;
	var html_str = '<div id="menuContent" name="'+name+'"  class="menuContent" style="width: 180px; display: none; position: absolute; letter-spacing: 5px; z-index: 2">';
        html_str += '<div><input type="button" id="btn_CancelAllNodes" onclick="CancelAllNodes()" value="取消" class="nj_button" style="width:80px"/>';

        html_str += '<input type="button" onclick="closediv()" value="关闭" class="nj_button" style="width:80px"/></div>';
       
        html_str += '<div id="tree_container" style=""><ul id="treeDemo" class="ztree" style="padding: 0px!important;padding: 0px!important;display:block;margin-top:1px;margin-left:0px; width:99%;z-index:3;  height: 300px;overflow-y: auto;"></ul></div>';
    html_str += '</div>';
    $("body").append(html_str);
    $("#menuContent")[0]["name"]=name;
}
//初始化树参数
function createNameRadioZtree(node1,node2,node3,node4,node5,node6,node7,node8,node9,node10,node11,node12){
	var node=[node1,node2,node3,node4,node5,node6,node7,node8,node9,node10,node11,node12];
	for(var i=0;i<node.length;i++){
		if($("#"+node[i]).length==0)
			return;
		var html_str = $("#"+node[i])[0].outerHTML.substring(0,6);
		html_str += " oninput='radiochangeZtreeV2(this.value)'";
		if($("#"+node[i])[0].outerHTML.indexOf("onfocus")==-1){
			html_str += " onfocus='onfocusdiv("+node[i]+")'"
		}
		html_str += " onblur='radioBlurZtree(this)'";
		html_str += $("#"+node[i])[0].outerHTML.substring(6);
		$("#"+node[i])[0].outerHTML=html_str;
	}
}

//初始化参数
function createNameRadioZtreev2(node){
	for(var i=0;i<node.length;i++){
		if($("#"+node[i]).length==0)
			return;
		var html_str = $("#"+node[i])[0].outerHTML.substring(0,6);
		html_str += " oninput='radiochangeZtreeV2(this.value)'";
		if($("#"+node[i])[0].outerHTML.indexOf("onfocus")!=-1){
			html_str += " onfocus='onfocusdiv("+node[i]+")'"
		}
		html_str += " onblur='radioBlurZtree(this.value)'";
		html_str += $("#"+node[i])[0].outerHTML.substring(6);
		$("#"+node[i])[0].outerHTML=html_str;
	}
}