var setting = {
	check: {
		enable: true,
		chkDisabledInherit: true,
		chkboxType : {"Y" : "s", "N" : "s"},
		 radioType :"all"
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
		onCheck : onCheck
	}
};
	$(document).ready(function(){
       $("#btn_CheckAllNodes").click(CheckAllNodes);
       $("#btn_CancelAllNodes").click(CancelAllNodes);
       $("#returnCheckAllNodes").click(returnCheckAllNodes);
	   
    });
	
	function beforeClick(treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj(ulid);
		zTree.checkNode(treeNode, !treeNode.checked, null, true);
		return false;
	}
    
	
    //默认全选中
	function ztreeChecked(id,node,commonFlag,selectNode,levelFlag1){
		hideid=id+"id";
	    nameid=id;
	    ulid=id+"tree";
	    var selectid = id+"select";
	    if(levelFlag1 == undefined || levelFlag1 == ""){
			levelFlag = false;
		}else{
			levelFlag = levelFlag1;
		}
	    if('common' == commonFlag){
	    	createTree(ulid,"");
	    }else if('level' == commonFlag){
	    	$("body").append("<input id='"+selectid+"' type='hidden' />");
	    	createTree(ulid,selectNode);
	    	var selectedId = $('#selectNode').val();
			node = filter(node,selectedId);
	    }
	    $.fn.zTree.init($("#"+ulid), setting, node);
		 
	    CheckAllNodes();
    }
    
	//全选
    function CheckAllNodes() {
    	 var treeObj = $.fn.zTree.getZTreeObj(ulid);
         treeObj.checkAllNodes(true);
         if(levelFlag){
 			getCheckedTree(treeObj);
 		}else{
 			var nodes = treeObj.getCheckedNodes(true);
 			var msg = "";
 			var tid = "";
 			for (var i = 0; i < nodes.length; i++) {
 				msg += nodes[i].name+",";
 				tid += nodes[i].id+",";
 			}
 			if (msg.length > 0 ){
 				msg = msg.substring(0, msg.length-1);
 			}
 			if (tid.length > 0 ){
 				tid = tid.substring(0, tid.length-1);
 			}
 			 
 			$("#"+nameid).val(msg);
 			$("#"+hideid).val(tid);
 		}
    }

	//全取消
	function CancelAllNodes() {
		var treeObj = $.fn.zTree.getZTreeObj(ulid);
		treeObj.checkAllNodes(false);
		$("#"+nameid).val('');
		$("#"+hideid).val('');
	}
	//单选取消
	function radioCancelAllNodes() {
	    var treeObj = $.fn.zTree.getZTreeObj(ulid);
	    //treeObj.checkAllNodes(false);
	    var nodes = treeObj.getNodes();
	    for(var i=0;i<nodes.length;i++){
	    	treeObj.checkNode(nodes[i], false);
	    }
		$("#"+nameid).val('');
		$("#"+hideid).val('');
	}
  
	//反选
	function returnCheckAllNodes () {
		var treeObj = $.fn.zTree.getZTreeObj(ulid);
		var nodes = treeObj.getCheckedNodes(true);
		var nodesno = treeObj.getCheckedNodes(false);
		if(nodes.length>0){
			var v="";
			var vid="";
			for (var i=0, l=nodes.length; i<l; i++) {
				treeObj.checkNode(nodes[i]);
				v += nodes[i].name + ",";
				vid += nodes[i].id + ",";
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			if (vid.length > 0 ) vid = vid.substring(0, vid.length-1);
				$("#"+nameid).val(v);
				$("#"+hideid).val(vid);
		}else{
			$("#"+nameid).val('');
			$("#"+hideid).val('');
		}
		if(nodesno.length>0){
			var value="";
			var valueid="";
		for(var i=0, l=nodesno.length; i<l; i++){
			treeObj.checkNode(nodesno[i]);
			value += nodesno[i].name + ",";
			valueid += nodesno[i].id + ",";
		}
		if(value.length > 0) value = value.substring(0, value.length-1);
		if(valueid.length > 0) valueid = valueid.substring(0, valueid.length-1); 
		   $("#"+nameid).val(value);
		   $("#"+hideid).val(valueid);
		}else{
		   $("#"+nameid).val('');
		   $("#"+hideid).val('');
		}
		if(levelFlag){
			getCheckedTree(treeObj);
		}
	}
	
	//关闭
	function closediv(){
		$("#"+ulid).hide();
		$("#menuContent").fadeOut("fast"); 
	}
	
	//创建树
	function createTree(id,selectNode){
	    $("#menuContent").remove();
	    
	    
    	var html_str = '<div id="menuContent" class="menuContent" style="min-width:200px;display:none;position: absolute;letter-spacing:1px;z-index:2">';
    	
    	if(setting.check.chkStyle=="radio"){
    		 html_str += '<div><input type="button" id="btn_CancelAllNodes" onclick="radioCancelAllNodes()" value="取消" class="nj_button"/>';

      	 	 html_str += '<input type="button" onclick="closediv()" value="关闭" class="nj_button"/>';
    	}else{
	        html_str += '<div><input id="btn_CheckAllNodes" type="button" onclick="CheckAllNodes()"  value="全选" class="nj_button"/>';
	
	        html_str += '<input type="button" id="btn_CancelAllNodes" onclick="CancelAllNodes()" value="取消" class="nj_button"/>';
	
	        html_str += '<input type="button" id="returnCheckAllNodes" onclick="returnCheckAllNodes()" value="反选" class="nj_button"/>';
	
	        html_str += '<input type="button" onclick="closediv()" value="关闭" class="nj_button"/>';
        }
		
		if(selectNode != undefined && selectNode.length > 0){
			var treeIdx = id.indexOf("tree");
			var selectid = id.substring(0,treeIdx)+"select";
			html_str += '<select id="selectNode" style="border: transparent;margin:1px 1px -1px 1px;height:24px;" onchange="selectChange()">';
			for(var i=0;i<selectNode.length;i++){
				if($('#'+selectid).val() == selectNode[i].id){
					html_str += '<option value="'+selectNode[i].id+'" selected>'+selectNode[i].name+'</option>';
				}else{
					html_str += '<option value="'+selectNode[i].id+'" >'+selectNode[i].name+'</option>';
				}
			}
			html_str += '</select>';
		}
       
		html_str += '</div><ul id="'+id+'" class="ztree" style="margin-top:1px;margin-left:0px;height:300px;overflow:auto;z-index:3;width: 100%;"></ul>';
        
		html_str += '</div>';
        
        $("body").append(html_str);
    }
	
	function selectChange(){
		var selectedId = $('#selectNode').val();
		
		var treeIdx = ulid.indexOf("tree");
		var selectid = ulid.substring(0,treeIdx)+"select";
		$('#'+selectid).val(selectedId);
		
		var node = filter(srcNode,selectedId);
	    $.fn.zTree.init($("#"+ulid), setting, node);
	    $("#"+nameid).val('');
	    $("#"+hideid).val('');
	}
	
	//显示树
	function showdiv(id,node,commonFlag,selectNode,levelFlag1){
		ids = id+"id";
		if(selectNode == undefined || selectNode == ""){
	        if("common" == commonFlag){
	        	commonTree(id,ids,node);
	        }
		}else{
			if("level" == commonFlag){
	        	levelTree(id,ids,node,selectNode);
	        }
		}
		if(levelFlag1 == undefined || levelFlag1 == ""){
			levelFlag = false;
		}else{
			levelFlag = levelFlag1;
		}
	}
	
	function commonRadioTree(id,node,commonFlag,radioFlag){
	 setting = {
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
				beforeClick : beforeClick,
				onCheck : radioCheck
			}
		};
	
		ids = id+"id";
		hideid=ids;
	    nameid=id;
		 
	    ulid=id+"tree";
	    
		createTree(ulid,"");
	    $.fn.zTree.init($("#"+ulid), setting, node);
	    
	    /*用jquery绑定一个按钮click事件后，
	      jquery click 不是替换原有的function, 
	            而是接着添加，所以才会执行次数越来越多
	            解决方法：$("").unbind('click');*/
	    $("#"+id).unbind("keyup").bind("keyup", function(){radioSearchNode($("#"+id).val(),event);});
		
	
		
	    var cityObj = $("#"+nameid);
	    var cityOffset = $("#"+nameid).offset();
	    var treeObj = $.fn.zTree.getZTreeObj(ulid);
	    // treeObj.checkAllNodes(false);
	    treeObj.showNodes(treeObj.transformToArray(treeObj.getNodes())) ;
	    $("#menuContent").prop("hiddenId",nameid)
	    $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
	    setValue();
	    
	
	}
	
	function radioCheck(){
			var zTree = $.fn.zTree.getZTreeObj(ulid), nodes = zTree.getCheckedNodes(true), v = "",code="";
			for (var i = 0, l = nodes.length; i < l; i++) {
				v += nodes[i].name + ",";
				code +=nodes[i].id + ",";
			}
			if(code.length>0)
				code = code.substring(0, code.length - 1);
			if (v.length > 0)
				v = v.substring(0, v.length - 1);
			$("#"+nameid).val(v);
			$("#"+hideid).val(code);
	}
	//文本框输入事件处理
	function radioSearchNode(str,e){
		e = e||window.event;
		var kcode = e.which || e.keyCode;
		var charcode = e.charCode;
		if(typeof charcode == "number" && charcode != 0){
            kcode = charcode;
		}else{
            //在中文输入法下 keyCode == 229 || keyCode == 197(Opera)
            kcode = e.keyCode;
		}
		
		var position = getPosition(nameid);
	
		var value = $.trim(str).replace(/，/gi, ",");//中文逗号替换为英文逗号
		
		var zTree = $.fn.zTree.getZTreeObj(ulid);
			  
		var prestr = value.substring(0,position);
		var substr=value.substring(position);
		var prevalIndex = prestr.lastIndexOf(",");
		var newvalue = value.substring(prevalIndex+1,position);
		
		
		if(/.*[\u4e00-\u9fa5]+.*$/.test(newvalue)){
			nodeList = zTree.getNodesByParamFuzzy("name", newvalue);
		}else{
			nodeList = zTree.getNodesByParamFuzzy("id", newvalue);
		}
		nodeList = zTree.transformToArray(nodeList);
		updateNodes(nodeList);
			  
		
		if(kcode == 13){ //增加新内容  回车键
		    getRadioAdd(zTree,value,position);
		}
		
		if(kcode==8) { //删除内容 	退格键
			upPosition(position,value);
			getRadioDel(zTree,value,position);
		}
		
		$("#menuContent").slideDown("fast");//输入时显示ztree框
		return false;
	}
	//输入信息并回车
	function getRadioAdd(zTree,value,position){
		var tempvalue = value.substring(0,position);
		var tempIndex = 0;
		if(tempvalue.indexOf(",") > -1){
			tempIndex = tempvalue.lastIndexOf(",")+1;
		}else{
			tempIndex = 0;
		}
		
		var searchvalue = tempvalue.substring(tempIndex);
		
		var tempList ;
		if(/.*[\u4e00-\u9fa5]+.*$/.test(searchvalue)){
			tempList = zTree.getNodesByParamFuzzy("name", searchvalue);
		}else{
			tempList = zTree.getNodesByParamFuzzy("id", searchvalue);
		}
		
		if(tempList.length == 1){ //已匹配上
			$("#"+nameid).val('');
			$("#"+hideid).val('');
			tempList[0].checked = true;
			zTree.updateNode(tempList[0]);	
			
			$("#"+hideid).val(tempList[0].id);
			$("#"+nameid).val(tempList[0].name);
		}
		if(tempList == undefined){//没匹配上就删掉
			value = value.replace(searchvalue,"");
			$("#"+nameid).val(value);
		}
	}
	//单选删除输入的信息
	function getRadioDel(zTree,value,position){
		
		if(position == undefined){ //表示第一个元素
			var nodeId = getIdByIndex(0);
			deleteIdByIndex(0);
			
			value = value.replace(new RegExp(',+',"gm"),',');
			if(value.indexOf(",") == 0){ //首位逗号,则删除逗号
				value = value.substring(1);
			}
			$("#"+nameid).val(value);
			lastposition = position;
			//setPosition(nameid,lastposition);
			
			var node = zTree.getNodesByParam("id", nodeId);
			if(node.length == 1){
				node[0].checked = false;
				zTree.updateNode(node[0]);
			}
		}
	}
	
	//获取焦点隐藏ztree框
    function radioShowTreeNode(id,node,commonFlag){
		commonRadioTree(id,node,commonFlag);
		$("#menuContent").hide();
	}
    
     function showTreeNode(id,node,commonFlag,selectNode,levelFlag1){
		showdiv(id,node,commonFlag,selectNode,levelFlag1);
		$("#menuContent").hide();
	}
	
	function specshowdiv(id,ids,node,selectNode,lookChildren1,levelFlag1){
		lookChildren = lookChildren1;
		levelFlag = levelFlag1;
		if(selectNode == undefined || selectNode == ""){
			commonTree(id,ids,node);
		}else{
			levelTree(id,ids,node,selectNode);
		}
	}
   
    var lookChildren = true,levelFlag = false;
    var nameid="",hideid="",ulid="";
    //普通的树
    function commonTree(id,ids,node){
setting = {
	check: {
		enable: true,
		chkDisabledInherit: true,
		chkboxType : {"Y" : "s", "N" : "s"},
		 radioType :"all"
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
		onCheck : onCheck
	}
};
		hideid=ids;
	    nameid=id;
		 
	    ulid=id+"tree";
	    
		createTree(ulid,"");
	    $.fn.zTree.init($("#"+ulid), setting, node);
	    
	    /*用jquery绑定一个按钮click事件后，
	      jquery click 不是替换原有的function, 
	            而是接着添加，所以才会执行次数越来越多
	            解决方法：$("").unbind('click');*/
	    $("#"+id).unbind("keydown").bind("keydown", function(){getLastValue($("#"+id).val(),event);});
		$("#"+id).unbind("keyup").bind("keyup", function(){searchNode($("#"+id).val(),event);});
		$("#"+id).unbind("blur").bind("blur", function(){matchNode($("#"+id).val());});
		
	    var cityObj = $("#"+nameid);
	    var cityOffset = $("#"+nameid).offset();
	    var treeObj = $.fn.zTree.getZTreeObj(ulid);
	    // treeObj.checkAllNodes(false);
	    treeObj.showNodes(treeObj.transformToArray(treeObj.getNodes())) ;
	    $("#menuContent").prop("hiddenId",nameid)
	    $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
	    setValue();
		
  	}
	
	
	var srcNode;
	//有下拉框的树
	function levelTree(id,ids,node,selectNode){
	setting = {
	check: {
		enable: true,
		chkDisabledInherit: true,
		chkboxType : {"Y" : "s", "N" : "s"},
		 radioType :"all"
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
		onCheck : onCheck
	}
};
		srcNode = node;
		
		nameid=id;
		hideid=ids;
	   
	    ulid=id+"tree";

		createTree(ulid,selectNode);
		 
		var selectedId = $('#selectNode').val();
		node = filter(node,selectedId);
	    $.fn.zTree.init($("#"+ulid), setting, node);
	    
	    $("#"+id).unbind("keydown").bind("keydown", function(){getLastValue($("#"+id).val(),event);});
		$("#"+id).unbind("keyup").bind("keyup", function(){searchNode($("#"+id).val(),event);});
		$("#"+id).unbind("blur").bind("blur", function(){matchNode($("#"+id).val());});
		
	    var cityObj = $("#"+nameid);
	    var cityOffset = $("#"+nameid).offset();
	    var treeObj = $.fn.zTree.getZTreeObj(ulid);
	    // treeObj.checkAllNodes(false);
	    treeObj.showNodes(treeObj.transformToArray(treeObj.getNodes())) ;
	    $("#menuContent").prop("hiddenId",nameid)
	    $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
	    setValue();
		
  	}
  	
	//zTree过滤
	function filter(node,selectedId){
		var filterNode = [];
		if("all" == selectedId){
			return node;
		}
		for(var i=0;i<node.length;i++){
			if(selectedId == node[i].dataType){
				filterNode.push(node[i]);
			}
		}
		return filterNode;
	}
	

	//获取光标位置
	function getPosition(id){
		var obj = document.getElementById(id);
		var cursurPosition = -1;
		var value = $('#'+id).val();
		
		if(obj.selectionStart){//非IE浏览器
			cursurPosition = obj.selectionStart;
		}else{//IE
			if(document.selection == undefined){
				return;
			}
			var range = document.selection.createRange();
			range.moveStart("character",-obj.value.length);
			cursurPosition = range.text.length;
		}
		return cursurPosition;
	}
	
	
	//数组去重
	function distinctarr(str){
		var strArr = str.split(",");
		var result = new Array();//创建出一个结果数组  
		for(var key in strArr){
			if(result.indexOf(strArr[key]) > -1){
	
			}else{
				if("" != strArr[key]){
					result.push(strArr[key]);  
				}
			
			}
		}
		return result.toString();
	}
	
	//获取所有子节点并选中
	function getAllChildrenNodes(treeNode,result,flag){
		var zTree = $.fn.zTree.getZTreeObj(ulid);
		if(treeNode.isParent){
			var childrenNodes = treeNode.children;
			for(var i=0;i<childrenNodes.length;i++){
				if(flag == "id"){
					result += ','+childrenNodes[i].id;
				}else{
					result += ','+childrenNodes[i].name;
				}
				childrenNodes[i].checked = true;
				zTree.updateNode(childrenNodes[i]);	
				result = getAllChildrenNodes(childrenNodes[i],result,flag);
			}
		}
		return result;
	}
	
	//手动输入内容,id值回填隐藏域中,带顺序的
	function inCheck(treeNode,sortArr){

	    var zTree = $.fn.zTree.getZTreeObj(ulid);
		var sortIndex = sortArr[0];
		var sortId = sortArr[1];
		var sortName = sortArr[2];
		if(lookChildren){
			sortId = getAllChildrenNodes(treeNode,sortId,"id");
			sortName = getAllChildrenNodes(treeNode,sortName,"name");
		}
		
		var idsarray = $("#"+hideid).val().split(","); 
		var valuesarray = $("#"+nameid).val().split(",");
		
		concatArr(idsarray,parseInt(sortIndex),sortId);	
		concatArr(valuesarray,parseInt(sortIndex),sortName);
		
		var ids = distinctarr(idsarray.toString());
		var values = distinctarr(valuesarray.toString());
		
        $("#"+hideid).val(ids);
		$("#"+nameid).val(values);

	}
	
	//从特定位置插入数组
	function concatArr(srcArr,index,str){
		str = ""+str;
		if(str.indexOf(",") > -1){
			var insertArr = str.split(",");
			for(var i=0;i<insertArr.length;i++){
				srcArr.splice(index+i,0,insertArr[i]);
			}
		}else{
			srcArr.splice(index,0,str);
		}
		
	}
	
	//取消所有子节点
	function cancelAllChildrenNodes(treeNode,result,flag){
		var zTree = $.fn.zTree.getZTreeObj(ulid);
		if(treeNode.isParent){
			var childrenNodes = treeNode.children;
			for(var i=0;i<childrenNodes.length;i++){
				if(flag == "id"){
					result += ','+childrenNodes[i].id;
				}else{
					result += ','+childrenNodes[i].name;
				}
				childrenNodes[i].checked = false;
				zTree.updateNode(childrenNodes[i]);	
				
				result = cancelAllChildrenNodes(childrenNodes[i],result,flag);
			}
		}
		return result;
	}
	
	//字符串中是否包含特定字符串
	function isIndexOf(str,reg){
		var flag = false;
		var arr = str.split(",");
		for(var i=0;i<arr.length;i++){
			if(reg == arr[i]){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	//获取选中节点最大的层级(底层)
	function getLevel(zTree){
		var max = 0;
		var nodes = zTree.transformToArray(zTree.getNodes());
		for(var i=0;i<nodes.length;i++){
			if(nodes[i].checked){
				if(max < nodes[i].level){
					max = nodes[i].level;
				}
			}
		}
		return max;
	}
	
	//获取所有选中节点
	function getCheckedTree(zTree){
		var nodes = zTree.transformToArray(zTree.getNodes());
		
		var ids = $.trim($("#"+hideid).val());
		var values = $.trim($("#"+nameid).val());
		
		var position = getPosition(nameid);
		var prevalue = values.substring(0,position);
		var temp = prevalue.substring(prevalue.lastIndexOf(",")+1);
		
		var idsarr = ids.split(",");
		var valuearr = $.trim($("#"+nameid).val()).split(",");
		
		var level = getLevel(zTree);

		for(var i=0,l=nodes.length;i<l;i++){
			if(nodes[i].checked){//选中(打勾)
				
				if(isIndexOf($("#"+hideid).val(),nodes[i].id)){
					if(levelFlag){//表示有层级,父层级不选中,已底层为准
						if(nodes[i].level != level){ //删除层级不等的元素
							idsarr = ids.split(",");
							valuearr = values.split(",");
							
							for(var p=0;p<idsarr.length;p++){
								if(idsarr[p] == nodes[i].id){
									idsarr.splice(p,1);
								}
							}
							for(var k=0;k<valuearr.length;k++){
								if(valuearr[k] == nodes[i].name){
									valuearr.splice(k,1);
								}
							}
							
							ids = idsarr.toString();
							values = valuearr.toString();
							
						}
					}
				}else{
					if(levelFlag){//表示有层级,父层级不选中,已底层为准
						if(nodes[i].level == level){
							ids += ","+nodes[i].id;
							if(nodes[i].name.indexOf(temp) > -1){//勾上的时候追加到最后,并且删除已输入的内容
								values = values.replace(temp,"");
							}
							values += ","+nodes[i].name;
						}
					}else{
						ids += ","+nodes[i].id;
						if(nodes[i].name.indexOf(temp) > -1){//勾上的时候追加到最后,并且删除已输入的内容
							values = values.replace(temp,"");
						}
						values += ","+nodes[i].name;
					}
				}
				
			}
		}
		
		values = distinctarr(values);
		ids = distinctarr(ids);
		$("#"+nameid).val(values);
		$("#"+hideid).val(ids);
	}
	
	function onCheck(e, treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj(ulid);    
		
		var ids = $.trim($("#"+hideid).val());
		var values = $.trim($("#"+nameid).val());
		//$("#"+nameid).val(delOnCheck(zTree,values));
		//values = $.trim($("#"+nameid).val());
		
		var idsarr = ids.split(",");
		var valuesarr = $.trim($("#"+nameid).val()).split(",");
		if(!treeNode.checked){ //没选中(去勾)
			if(treeNode.isParent){//是父节点就取消它的所有子节点
				var nocheckids = cancelAllChildrenNodes(treeNode,treeNode.id,"id").split(",");
				var nochecknames = cancelAllChildrenNodes(treeNode,treeNode.name,"name").split(",");
				
				for(var i=0;i<nocheckids.length;i++){
					for(var j=0;j<idsarr.length;j++){
						if(idsarr[j] == nocheckids[i]){
							idsarr.splice(j,1);
						}
					}
				}
				for(var i=0;i<nochecknames.length;i++){
					for(var j=0;j<valuesarr.length;j++){
						if(nochecknames[i] == valuesarr[j]){
							valuesarr.splice(j,1);
						}
					}
				}
			}else{
				for(var i=0;i<idsarr.length;i++){
					if(idsarr[i] == treeNode.id){
						idsarr.splice(i,1);
					}
				}
				
				for(var j=0;j<valuesarr.length;j++){
					if(valuesarr[j] == treeNode.name){
						valuesarr.splice(j,1);
					}
				}
			}
			ids = idsarr.toString();
			values = valuesarr.toString();
			$("#"+hideid).val(ids);
			$("#"+nameid).val(values);
		}
		
		getCheckedTree(zTree);
		
		//setValue();
        //GetCheckedAll();
        return false;

    }
	
	function getIdByIndex(idx){
		var array = $("#"+hideid).val().split(",");
		return array[idx];		
	}
	
	function deleteIdByIndex(delIdx){
		var array = $("#"+hideid).val().split(","); 
		array.splice(delIdx,1); //哪个位置开始删除，删除几个元素
		var ids = distinctarr(array.toString());
		if("," == ids){//所有元素删除之后,剩余","替换为""
			ids = "";
		}
		$("#"+hideid).val(ids);
	}
	
	//失去焦点时重新匹配节点
	function matchNode(value){
		var zTree = $.fn.zTree.getZTreeObj(ulid);
		var checkedId = $('#'+hideid).val();
		if(checkedId == undefined || checkedId == ""){
			return false;
		}
		zTree.checkAllNodes(false);
		var names="",ids="";
		var matchList = value.split(",");
		var tempList ;
		for(var i=0;i<matchList.length;i++){
			var matchvalue = matchList[i];
			if(/.*[\u4e00-\u9fa5]+.*$/.test(matchvalue)){
				tempList = zTree.getNodesByParamFuzzy("name", matchvalue);
			}else{
				tempList = zTree.getNodesByParamFuzzy("id", matchvalue);
			}
			if(tempList.length == 1){ //已匹配上
				if(isIndexOf(checkedId,tempList[0].id)){//已选中的删掉
					tempList[0].checked = true;
					zTree.updateNode(tempList[0]);
	
					ids += tempList[0].id+",";
					names += tempList[0].name+",";
				}
			}
		}
		if(ids.length > 0) ids = ids.substring(0, ids.length-1);
	    if(names.length > 0) names = names.substring(0, names.length-1);
		
		$("#"+hideid).val(distinctarr(ids));
		$("#"+nameid).val(distinctarr(names));
		return false;
	}
	
	var lastvalue,lastposition;
	function searchNode(str,e) {
		
		e = e||window.event;
		var kcode = e.which || e.keyCode;
		var charcode = e.charCode;
		if(typeof charcode == "number" && charcode != 0){
            kcode = charcode;
		}else{
            //在中文输入法下 keyCode == 229 || keyCode == 197(Opera)
            kcode = e.keyCode;
		}
		
		var position = getPosition(nameid);
	
		var value = $.trim(str).replace(/，/gi, ",");//中文逗号替换为英文逗号
		
		var zTree = $.fn.zTree.getZTreeObj(ulid);
			  
		var prestr = value.substring(0,position);
		var substr=value.substring(position);
		var prevalIndex = prestr.lastIndexOf(",");
		var newvalue = value.substring(prevalIndex+1,position);
		
		
		if(/.*[\u4e00-\u9fa5]+.*$/.test(newvalue)){
			nodeList = zTree.getNodesByParamFuzzy("name", newvalue);
		}else{
			nodeList = zTree.getNodesByParamFuzzy("id", newvalue);
		}
		nodeList = zTree.transformToArray(nodeList);
		updateNodes(nodeList);
			  
		
		if(kcode==188) {   
			//alert("输入逗号,在最后时不前移，两个逗号中间不前移，只有前面是逗号后面是非逗号光标前移1位");
			if("," == value.charAt(position-1) && "," != value.charAt(position) && "" != value.charAt(position)){
				setPosition(nameid,position-1);
			}
		}
		
		if(kcode == 13){ //增加新内容  回车键
		    getAdd(zTree,value,position);
			//setPosition(nameid,lastposition);//回车键之后,光标定位到当前位置,默认移到末位
		}
		
		if(kcode==8) { //删除内容 	退格键
			upPosition(position,value);
			getDel(zTree,value,position);
		}
		
		$("#menuContent").slideDown("fast");//输入时显示ztree框
		//lastvalue = value;
		//lastposition = position;
		return false;
	}
	
	//获取按键盘之前的上一次的值和光标位置
	function getLastValue(str,e){		
		var position = getPosition(nameid);
		lastvalue = $.trim(str).charAt(position-1);
		lastposition = position;
		return false;
	}
	
	//逗号往前移一位
	function upPosition(position,value){
		var prestr = value.substring(0,position);
		var substr = value.substring(position);
		//alert("按退格键时，在最前或最后时不前移。前面是逗号后面有值且不是逗号光标前移1位");
		if("," == lastvalue && "," != value.charAt(position)) {
			$("#"+nameid).val(prestr+","+substr);
			setPosition(nameid,position);//光标前移1位
		}
	}
	
	//获取删除	
	function getDel(zTree,value,position){
		var delIdx = 0;
		if(position > 0){
			var substr = value.substring(0,position);
			if("," == substr.charAt(position-1)){	//删除内容,前面是逗号
				if("," == value.charAt(position) || "" == value.charAt(position)){	//后面有逗号或者后面空(最后末位)
					delIdx = substr.split(",").length-1;
					var nodeId = getIdByIndex(delIdx); //获取要删除的id
					deleteIdByIndex(delIdx);
					
					value = value.replace(new RegExp(',+',"gm"),',');//多个","替换为一个","
					$("#"+nameid).val(value);
					lastposition = position;
					setPosition(nameid,lastposition);//删除完之后,光标定位到当前位置,默认移到末位
					
					var node = zTree.getNodesByParam("id", nodeId);
					if(node.length == 1){
						node[0].checked = false;
						zTree.updateNode(node[0]);
					}
				}
			}
		}
		
		if(position == undefined){ //表示第一个元素
			var nodeId = getIdByIndex(0);
			deleteIdByIndex(0);
			
			value = value.replace(new RegExp(',+',"gm"),',');
			if(value.indexOf(",") == 0){ //首位逗号,则删除逗号
				value = value.substring(1);
			}
			$("#"+nameid).val(value);
			lastposition = position;
			//setPosition(nameid,lastposition);
			
			var node = zTree.getNodesByParam("id", nodeId);
			if(node.length == 1){
				node[0].checked = false;
				zTree.updateNode(node[0]);
			}
		}
		
	}
	
	//获取增加的内容
	function getAdd(zTree,value,position){
		var tempvalue = value.substring(0,position);
		var tempIndex = 0;
		if(tempvalue.indexOf(",") > -1){
			tempIndex = tempvalue.lastIndexOf(",")+1;
		}else{
			tempIndex = 0;
		}
		
		var searchvalue = tempvalue.substring(tempIndex);
		
		var tempList ;
		if(/.*[\u4e00-\u9fa5]+.*$/.test(searchvalue)){
			tempList = zTree.getNodesByParamFuzzy("name", searchvalue);
		}else{
			tempList = zTree.getNodesByParamFuzzy("id", searchvalue);
		}
		
		if(tempList.length == 1){ //已匹配上
			tempList[0].checked = true;
			zTree.updateNode(tempList[0]);	
			var sortIndex = 0;
			var substr = value.substring(0,position);
			
			if(substr.indexOf(",") > -1){
				var arr1 = substr.split(",");
				sortIndex = arr1.length-1;
			}else{
				sortIndex = 0;
			}
			
			var sortArr = [sortIndex,tempList[0].id,tempList[0].name];
			
			var array = value.split(","); 
			for(var i=array.length-1;i>=0;i--){//删除输入的内容
				if(searchvalue == array[i]){
					array.splice(i,1);
					break;
				}
			}
			
			$("#"+nameid).val(array.toString());
			
			inCheck(tempList[0],sortArr);	

			if(levelFlag){
				getCheckedTree(zTree);
			}	
			
		}
		if(tempList == undefined){//没匹配上就删掉
			value = value.replace(searchvalue,"");
			$("#"+nameid).val(value);
		}
	}
	
	//设置光标位置函数 
	function setPosition(id, pos){
		var obj = document.getElementById(id);
		if(obj.setSelectionRange) {
			obj.focus(); 
			obj.setSelectionRange(pos,pos); 
		} else if (obj.createTextRange){
			var range = obj.createTextRange(); 
			range.collapse(true); 
			range.moveEnd('character', pos); 
			range.moveStart('character', pos); 
			range.select(); 
		} 
	}
	
	function updateNodes(nodeList) {
		var zTree = $.fn.zTree.getZTreeObj(ulid);
		var allNode = zTree.transformToArray(zTree.getNodes());
		zTree.hideNodes(allNode);
		for(var n in nodeList){
			findParent(zTree,nodeList[n]);
		}
		
		if(nodeList.length < allNode.length && !lookChildren){//是否可以看子节点
			if(nodeList.length > 0){
				if(nodeList[0].isParent){ //是否父节点
					zTree.hideNodes(nodeList[0].children);
				}
			}	
		}
		
	}

    function findParent(zTree,node){
		zTree.expandNode(node,true,false,false);
		zTree.showNode(node);
		var pNode = node.getParentNode();
		if(pNode != null){
			findParent(zTree,pNode);
		}
	}
	
	//选中状态
	function setValue(){
		var treeObj = $.fn.zTree.getZTreeObj(ulid);
		var value = $.trim($("#" + hideid).val());
		var ids = value.split(",");
		var nodeList = "";
		if(ids.length>0){
			for (var i=0; i<ids.length; i++) {
				nodeList = treeObj.getNodeByParam("id", ids[i]);
				if(nodeList!=null){
					treeObj.checkNode(nodeList,true);
				}	
			}
		}
	}
	
	/*韩鹏*/
	//为控件绑定事件,引用时，当在页面load结束之后调用，
	//传入对应的id和data
	//chkStyle如果为true，则为单选，否则为多选,可以为空
	//initFun，传入自定义的一个方法，级联时使用，此方法传入当前控件的id,返回data,可以为空
	//typedata 如果多组数据时使用，加载带有下拉框的树
	function bindZtree(id,data,chkStyle,initFun,typedata){
		$("#"+id).focus(function(){
			if(initFun!=undefined){
				data=initFun(id);
			}
			if(typedata!=undefined){
				showTreeNode(id,data,'level',typedata,true);
			}else if(chkStyle){
				radioShowTreeNode(id,data,'radio');
			}else{
				showTreeNode(id,data,'common');
			}
		});
		$("#"+id).next().mousedown(function(){
			if(initFun!=undefined){
				data=initFun(id);
			}
			if(typedata!=undefined){
				showdiv(id,data,'level',typedata,true)
			}else if(chkStyle){
				commonRadioTree(id,data,'common','radio');
			}else{
				showdiv(id,data,'common');
			}
		});
	}