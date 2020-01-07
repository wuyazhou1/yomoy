		addEventHandler(parent,"click",hiddenDiv);
		addEventHandler(this,"click",hiddenDiv);
		
		/************************************************ 
		 * addEventListener:监听Dom元素的事件 
		 *  
		 * target：监听对象 
		 * type：监听函数类型，如click,mouseover 
		 * func：监听函数 
		 ************************************************/
		function addEventHandler(target,type,func){ 
			  if(target.addEventListener){ 
			    //监听IE9，谷歌和火狐 
			    target.addEventListener(type, func, false); //false是在事件冒泡时触发事件 
			  }else if(target.attachEvent){ 
			    target.attachEvent("on" + type, func);//IE只支持事件冒泡 
			  }else{ 
			    target["on" + type] = func; 
			  }  
		} 
		
		/*function hiddenDiv(){
			if($("#ztreeContent").length<1)
				return;
			if(event==null){
				$("#ztreeContent").css("display","none");
				return;
			}
			var y=event.clientY;
			var x=event.clientX;
			var divx1 = $("#ztreeContent").offset().left;
			var divy1 = $("#ztreeContent").offset().top;

			var divx2 = $("#ztreeContent").offset().left + $("#ztreeContent").width();
			var divy2 = $("#ztreeContent").offset().top + $("#ztreeContent").height();

			if( x < divx1 || x > divx2 || y < divy1  || y > divy2 ){
				if($("#menuContent").css("display")!="none")
					if(event.srcElement.id=="selectNode")
						return;
					//文本框与a...区域
					//divx1不变
					divx2 = $("#ztreeContent").offset().left+ $("#"+setting.common.id).width()+parseInt($("#"+setting.common.id).css("padding-left"))+parseInt($("#"+setting.common.id).css("padding-right"))+26;
						//modified by cyd for 将padding和border计算在内20160415
					divy1 = $("#ztreeContent").offset().top - $("#"+setting.common.id).height();//高26
					divy2 = $("#ztreeContent").offset().top;
					if(x > divx1 && x < divx2 && y > divy1  && y < divy2)
						return;
					$("#ztreeContent").css("display","none");
			}
		}*/
		
		//ztreeContent 点击之后取消冒泡(click 事件不会触发hiddenDiv2，
		//	因为hiddenDiv2是绑定在body（this）和parent上的)
		$('body').delegate("#ztreeContent","click",function(){
    		event.stopPropagation(); 
		});
		$
		//input a... 点击之后取消冒泡
		$('body').delegate(".ztreebtn","click",function(){
    		event.stopPropagation(); 
		});
		
		
		//parent,this 点击之后，判断是否显示，显示则隐藏或移除
		function hiddenDiv(){
			if($("#ztreeContent").length<1)
				return;
			if(event==null){
				$("#ztreeContent").css("display","none");
				return;
			}
			if($("#menuContent").css("display")!="none")
				if(event.srcElement.id=="selectNode")
					return;
			$("#ztreeContent").css("display","none");
		}
		
		
		
		

		function setCustom(obj){
			if(obj!=null){
				if(obj.chkboxType!=null){
					setting.check.chkboxType=obj.chkboxType;
				}
				if(obj.chkStyle!=null){
					setting.check.chkStyle=obj.chkStyle;
				}
				if(obj.searchField!=null){
					setting.check.searchField=obj.searchField;
				}
				if(obj.lookChildren!=null){
					setting.check.lookChildren=obj.lookChildren;
				}
				if(obj.id!=null){
					setting.check.id=obj.id;
				}
			}
		}
		function setCssCustom(obj){
				if(obj.min_width!=null){
					$("#treeDemo").css("min-width",obj.min_width)
				}
				if(obj.max_width!=null){
					$("#treeDemo").css("max-width",obj.max_width)
				}
				if(obj.max_height!=null){
					$("#treeDemo").css("max-height",obj.max_height)
				}
		}
		var setting = {
			common:{
				"id":"",//当前加载树控件的id
				"currentDataSource":[],//当前数据源
				"currentDataSourceIndex":0,//当前数据源的下标
				"oldValue":""//上一个值，判断值是否发生了变化
			},
			check: {
				enable: true,
				chkboxType: {"Y":"ps", "N":"ps"},
				chkStyle : "checkbox",
				searchField:"name",
				lookChildren:false,
				radioType: "all"
			},
			view: {
				dblClickExpand: false,
				showIcon: false
			},
			data: {
				simpleData: {
					enable: true
				},key: {
					title: "id"
				}
			},
			callback: {
				beforeClick: beforeClick,
				onCheck: onCheck
			}
		};
		
		function beforeClick(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.checkNode(treeNode, !treeNode.checked, null, true);
			return false;
		}
		//点击时调用此方法
		function onCheck() {
			var value = $.trim($("#"+setting.common.id).val());
			var arr=value.split(",");
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			//var nodes = zTree.getCheckedNodes(true);
			var nodes = zTree.transformToArray(zTree.getNodes());
			var v = "";
			var idv=[];
			for(var i=0,l=nodes.length;i<l;i++){
				if(nodes[i].checked){//如果已选中
					v+=nodes[i].name+",";
					idv.push(nodes[i].id);
				}
			}
			if(setting.common.currentDataSource.length>1){//当数据源大于1时，需要将当前数据源选中的值记录到数据源当中，以便下次选择时复原已经选中了的值
				var selectNodeid=$("#selectNode").val();
				for(var i=0;i<setting.common.currentDataSource.length;i++){
					if(selectNodeid==setting.common.currentDataSource[i].id){
						setting.common.currentDataSource[i].hidvalue=v;
						setting.common.currentDataSource[i].hidid=idv;
						setting.common.currentDataSourceIndex=i;
					}
				}
			}else{
				setting.common.currentDataSource[0].hidvalue=v;
				setting.common.currentDataSourceIndex=0;
				setting.common.currentDataSource[0].hidid=idv;
			}
			$("#"+setting.common.id).val(v);
			if(setting.common.currentDataSource.clearFun!=undefined){
				setting.common.currentDataSource.clearFun();//调用清空方法
			}
		}
		//通过数据源加载ztree控件
		function createZtreeDiv(dataSource,obj){
			setting.common.currentDataSource=dataSource;
			setCustom(obj);
			$("#ztreeContent").remove();
			//当为单选按钮时，没有全选和反选按钮
			var ztreehtml='<div id="ztreeContent" style="display:none; position: absolute;"><div>';
			if(setting.check.chkStyle=="checkbox"){
				ztreehtml+='<input id="checkall" value="全选" type="button" onclick="checkAll(true)"/>'+
				'<input id="fanxuan" value="反选" type="button" onclick="fanxuan()"/>';
			}
			
			ztreehtml+='<input id="cancelall" value="取消" type="button"  onclick="checkAll(false)"/><input id="closeall" value="关闭" type="button"  onclick="closeZtree()"/>';
			var selectIndex=0;//选中的下标，默认为第一个
			//是否有多个数据源
			if(dataSource.length>1){
				ztreehtml+='<select style="float: right;" id="selectNode" onChange="selectChange()">';
				//首先判断数据源中的selected属性
				for(var i=0;i<dataSource.length;i++){//默认选择第一个
					var seletedStatus=dataSource[i].selected;
					if(seletedStatus!=undefined&&seletedStatus){
						selectIndex=i;
						setting.common.currentDataSourceIndex=i;
						break;
					}
				}
				for(var i=0;i<dataSource.length;i++){//默认选择第一个
					if(i==selectIndex) ztreehtml+='<option value="'+dataSource[i].id+'" selected>'+dataSource[i].name+'</option>';
					else ztreehtml+='<option value="'+dataSource[i].id+'">'+dataSource[i].name+'</option>';
				}
				ztreehtml+='</select>';
			}else{
				setting.common.currentDataSourceIndex=0;
			}
			ztreehtml+='</div><div id="menuContent" class="menuContent"><ul id="treeDemo" class="ztree" style="display:inline-block;margin-top:0;min-width: 188px; height: 300px;"></ul></div></div>';
			$("body").append(ztreehtml);
			if(dataSource.length>1){//如果多个数据源，则初始化选中的数据
				var selectNodeid=$("#selectNode").val();
				for(var i=0;i<dataSource.length;i++){
					if(selectNodeid==dataSource[i].id){
						$.fn.zTree.init($("#treeDemo"), setting, dataSource[i].data)
						break;
					}
				}
			}else{
				if(dataSource[0])//added by cyd for dataSource[0].data未定义会出错 20160423
					$.fn.zTree.init($("#treeDemo"), setting, dataSource[0].data)
			}
			setCssCustom(obj);//设置css属性
			
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			selectNodes();
			setting.common.id=obj.id;
		}
		
		//清空数据源
		function clearDataSource(dataSource){
			for(var i=0,l=dataSource.length;i<l;i++){
				dataSource[i].hidid=[];
				dataSource[i].hidvalue="";
			}
		}
		//当多个数据源时，下拉框发生变化，调用此方法
		function selectChange(){
				setting.common.currentDataSourceIndex=0;
				var selectNodeid=$("#selectNode").val();
				for(var i=0;i<setting.common.currentDataSource.length;i++){
					if(selectNodeid==setting.common.currentDataSource[i].id){
						setting.common.currentDataSourceIndex=i;
						$.fn.zTree.init($("#treeDemo"), setting, setting.common.currentDataSource[i].data)
						setting.common.currentDataSource[i].selected=true;//当前选项选中
						$("#"+setting.common.id).val(setting.common.currentDataSource[i].hidvalue);//复原上次选中的值，可以查看oncheck中是如何保留的
						selectNodes();//复原
					}else{
						setting.common.currentDataSource[i].selected=false;//取消其他选项的选中属性
					}
				}
		}
		//得到数据源中选中的id
		function getZtreeValue(dataSource){
			var arr=[];
			if(dataSource.length>1){//如果为多个数据源
				var ix=0;
				for(var i=0,l=dataSource.length;i<l;i++){
					if(dataSource[i].selected){
						ix=i;
						break;
					}
				}
				arr=dataSource[ix]==undefined?[]:dataSource[ix].hidid;
			}else{
				arr=dataSource[0]==undefined?[]:dataSource[0].hidid;
			}
			arr=arr==undefined?[]:arr;
			if(arr.length==0){
				return "";
			}else if(arr.length==1){
				return arr[0];
			}else if(arr.length>1){
				var value="";
				for(var i=0,l=arr.length;i<l;i++){
					if(i==l-1){
						value+=arr[i];
					}else{
						value+=arr[i]+",";
					}	
				}
				return value;
			}
		}
		//显示树
		function showMenu() {
			var cityObj = $("#"+setting.common.id);
			var cityOffset = $("#"+setting.common.id).offset();
			$("#ztreeContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
		}
		//为ztree对象绑定事件
		function bindZtree(id){
			$("#"+id).bind("input", searchNode);
			$("#"+id).bind("focus",{'id':id},initZtree);
			$("#"+id).next().bind("click",{'id':id},showZtree);
		}
		//初始化树，并显示
		function showZtree(e){
			initZtree(e);
			showMenu();//显示
			//e.stopPropagation();//added by cyd for用代理方式关闭ztreeContent 20160422
		}
		(function(){//自调用方法，为控件绑定事件
			$(".ztreebtn").each(function(index, element) {
                 var id = $(this).prev()[0].id;
				 bindZtree(id);
            });
		}());
		//文字发生变化时调用此方法
		function searchNode() {
			showMenu();
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			var value = $.trim($("#"+setting.common.id).val());
			var arr=value.split(",");
			var lastvalue=arr[arr.length-1];
			nodeList = zTree.getNodesByParamFuzzy(setting.check.searchField, lastvalue,null);
			packNodes(nodeList);//整理节点，隐藏所有的节点，然后显示搜索结果的节点
			selectNodes();//选中
			if(endWith(value,",")&&value.length>setting.common.oldValue.length){//如果以，结尾，如果只有一个结果,并且非删除操作，则选中
				var temp=zTree.getNodesByParamFuzzy(setting.check.searchField, arr[arr.length-2],null);
				if(temp.length==1){
					zTree.checkNode(temp[0], true, null);
					onCheck();
				}
			}else if(!endWith(setting.common.oldValue,",")&&value.length<setting.common.oldValue.length){//删除时
				var temp=zTree.getNodesByParamFuzzy(setting.check.searchField, arr[arr.length-1],null);
				if(temp.length==1){
					zTree.checkNode(temp[0], false, null);
					//onCheck();
					deleteById(temp[0].id);
				}
			}
			setting.common.oldValue=value;
		}
		//根据id删除hidid
		function deleteById(id){
			var arr=setting.common.currentDataSource[setting.common.currentDataSourceIndex].hidid;
			var newarr=[];
			for(var i=0,l=arr.length;i<l;i++){
				if(arr[i]!=id){
					newarr.push(arr[i]);
				}	
			}
			setting.common.currentDataSource[setting.common.currentDataSourceIndex].hidid=newarr;
		}
		function packNodes(nodeList){
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = zTree.getNodes();
			zTree.hideNodes(nodes);//隐藏所有的节点
			zTree.showNodes(nodeList);//显示搜索结果
			for(var i=0;i<nodeList.length;i++){
				//zTree.removeChildNodes(nodeList[i]);
				if(!setting.check.lookChildren){//是否可以看子节点
					zTree.hideNodes(nodeList[i].children);
				}
				zTree.expandNode(nodeList[i], true, true, true);//展开结果
				packShowNode(nodeList[i]);
			}
		}
		//递归，如果存在父节点，则显示
		function packShowNode(node){
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.showNode(node);
			if(node.getParentNode()!=null){
				packShowNode(node.getParentNode());
			}
		}
		
		function selectNodes(){//选中时，循环树的所有数据
			if(setting.common.currentDataSource[setting.common.currentDataSourceIndex]==undefined){
				return;
			}
			var arr=setting.common.currentDataSource[setting.common.currentDataSourceIndex].hidid;
			arr=arr==undefined?[]:arr;
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.cancelSelectedNode();//取消所有选中的节点
			nodeList = zTree.transformToArray(zTree.getNodes());//zTree.getNodes()只能获取根节点的数据，利用该方法转化为array集合,此时包含子节点
			var v="";
			var idv=[];
			for(var i=0;i<nodeList.length;i++){
				var con=true;//是否存在的标志，如果为true，则取消选中
				for(var j=0;j<arr.length;j++){
					if(nodeList[i].id==arr[j]){
						zTree.checkNode(nodeList[i], true, null);
						v+=nodeList[i].name+",";
						idv.push(nodeList[i].id);
						con=false;//此时存在，则选中
						break;
					}
				}
				if(con){
					zTree.checkNode(nodeList[i], false, null);
				}
			}
			setting.common.currentDataSource[setting.common.currentDataSourceIndex].hidvalue=v;
			setting.common.currentDataSource[setting.common.currentDataSourceIndex].hidid=idv;
		}
		//全选,全不选（取消）
		function checkAll(obj){
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			if(setting.check.chkStyle=="checkbox"){//如果为多选，执行checkAllNodes
				zTree.checkAllNodes(obj);
			}else{//如果是单选，因为单选不支持checkAllNodes方法，则做一下修改
				if(obj==false){//如果为单选按钮，并且点击了取消
					var nodes = zTree.getCheckedNodes(true);//获取已经选中的节点
					zTree.checkNode(nodes[0], false, null);//取消选中
				}
			}
			onCheck();
			if(setting.common.currentDataSource.clearFun!=undefined){
				setting.common.currentDataSource.clearFun();//调用清空方法
			}
		}
		//反选
		function fanxuan(){
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = zTree.transformToArray(zTree.getNodes());
			for(var i=0;i<nodes.length;i++){
				if(nodes[i].checked){
					zTree.checkNode(nodes[i], false, null);
				}else{
					zTree.checkNode(nodes[i], true, null);
				}
			}
			onCheck();
			if(setting.common.currentDataSource.clearFun!=undefined){
				setting.common.currentDataSource.clearFun();//调用清空方法
			}
		}
		//关闭
		function closeZtree(){
			$("#ztreeContent").remove();
		}
		
		//判断以什么结尾
		function endWith(str1, str2) {
			if (str1 == null || str2 == null) {
				return false;
			}
			if (str1.length < str2.length) {
				return false;
			} else if (str1 == str2) {
				return true;
			} else if (str1.substring(str1.length - str2.length) == str2) {
				return true;
			}
			return false;
		}
		
	
		