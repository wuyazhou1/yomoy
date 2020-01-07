var setting = {
            check: {
                enable: true,
                chkStyle: "checkbox",
                chkboxType: {"Y":"s", "N":"s"}
            },
            view: {
               dblClickExpand: false

            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                beforeClick: beforeClick,
                onCheck: onCheck
            }
        };

        $(document).ready(function(){
        	
           // $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            $("#btn_CheckAllNodes").click(CheckAllNodes);
            $("#btn_CancelAllNodes").click(CancelAllNodes);
            $("#returnCheckAllNodes").click(returnCheckAllNodes);
          
           /* $("#menuContent").hide();*/
        });
        function ztreeChecked(i,ids,str,node){
        	 inId=ids;
		     index=i;
		     ulid=str;
		    $.fn.zTree.init($("#"+ulid), setting, node);
		    CheckAllNodes();
        }
        function beforeClick(treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj(ulid);
            zTree.checkNode(treeNode, !treeNode.checked, null, true);
            return false;
        }

        function onCheck(e, treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj(ulid),
                    nodes = zTree.getCheckedNodes(true),
                    v = "";
            for (var i=0, l=nodes.length; i<l; i++) {
                v += nodes[i].name + ",";
            }
            if (v.length > 0 ) v = v.substring(0, v.length-1);
            var   cityObj = $("#"+index);
            cityObj.val(v);
            GetCheckedAll();
            return false;

        }
        
        var lastValue = "", nodeList = [], fontCss = {};
		 function searchNode(str) {
		      var zTree = $.fn.zTree.getZTreeObj(ulid);
		      if(/.*[\u4e00-\u9fa5]+.*$/.test(str)){
		    	  nodeList = zTree.getNodesByParamFuzzy("name", str);
		      }else{
		    	  nodeList = zTree.getNodesByParamFuzzy("id", str);
		      }
		      nodeList = zTree.transformToArray(nodeList);
		     updateNodes(true);
		
		 }
		 function updateNodes(highlight) {
		
		            var zTree = $.fn.zTree.getZTreeObj(ulid);
		            var allNode = zTree.transformToArray(zTree.getNodes());
		            zTree.hideNodes(allNode);
		            for(var n in nodeList){
		                findParent(zTree,nodeList[n]);
		            }
		
		            zTree.showNodes(nodeList);
		        }

        function findParent(zTree,node){
            zTree.expandNode(node,true,false,false);
            var pNode = node.getParentNode();
            if(pNode != null){
                nodeList.push(pNode);
                findParent(zTree,pNode);
            }
        }
		
		 //全选
        function CheckAllNodes() {
            var treeObj = $.fn.zTree.getZTreeObj(ulid);
             treeObj.checkAllNodes(true);
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
             var   cityObj = $("#"+index);
                cityObj.val(msg);
//             cityObj.attr("value", msg);
                $("#"+inId).val(tid);
        }

        //全取消
        function CancelAllNodes() {
            var treeObj = $.fn.zTree.getZTreeObj(ulid);
            treeObj.checkAllNodes(false);
            var cityObj = $("#"+index);
            cityObj.val('');
            $("#"+inId).val('');
//         cityObj.attr("value", "");
        }
        
        //反选
        function returnCheckAllNodes () {
	       var treeObj = $.fn.zTree.getZTreeObj(ulid);
	       var  nodes = treeObj.getCheckedNodes(true);
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
	          var   cityObj = $("#"+index);
	              cityObj.val(v);
	              $("#"+inId).val(vid);
//	              cityObj.attr("value", v);
	      }else{
	    	   $("#"+index).val('');
	           $("#"+inId).val('');
	       }
	       if(nodesno.length>0){
	           var value="";
	           var valueid="";
	       for (var i=0, l=nodesno.length; i<l; i++) {
	          treeObj.checkNode(nodesno[i]);
	           value += nodesno[i].name + ",";
	           valueid += nodesno[i].id + ",";
	           }
	     if (value.length > 0 )value = value.substring(0, value.length-1);
	     if (valueid.length > 0 )valueid = valueid.substring(0, valueid.length-1);
	           var   cityObj = $("#"+index);
	           cityObj.val(value);
	           $("#"+inId).val(valueid);
//	           cityObj.attr("value", value);
	
	       }else{
	    	   $("#"+index).val('');
	           $("#"+inId).val('');
	       }
        }
        //关闭
		function closediv(){
		    $("#"+ulid).hide();
		    $("#menuContent").fadeOut("fast");
		    
		}

		var index="";
		var ulid="";
		var inId="";
		
		function showdiv(i,ids,str,node){
			 $('.nj_button00').val('');//清空搜索内容
			 inId=ids;
		     index=i;
		     ulid=str;
		    $.fn.zTree.init($("#"+ulid), setting, node);
		     $("#menuContent ul").each(function(i,dom){
		     if(dom.id==ulid){
		             $("#"+dom.id).show();
		         }else{
		          $("#"+dom.id).hide()
		         }
		        // console.log(dom.id);
		
		     });
	     var cityObj = $("#"+index);
	     var cityOffset = $("#"+index).offset();
	     var treeObj = $.fn.zTree.getZTreeObj(ulid);
	    // treeObj.checkAllNodes(false);
	     treeObj.showNodes(treeObj.transformToArray(treeObj.getNodes())) ;
	     $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
	     setValue();
	   }
		 //获取所有选中节点的id值
	        function GetCheckedAll() {
	        	var treeObj = $.fn.zTree.getZTreeObj(ulid);
	            var msg = "";
	            if(treeObj!=null){
	                var nodes = treeObj.getCheckedNodes(true);
	                if(nodes.length>0){
	                    for (var i = 0; i < nodes.length; i++) {
	                        if(i==nodes.length-1){
	                        	msg += nodes[i].id;
	                        	continue;
	                        }
	                        msg += nodes[i].id+",";
	                    }
	                }
	                $("#"+inId).val(msg);
	            }
	        }
		 //选中状态
		 function setValue(){
			 	var treeObj = $.fn.zTree.getZTreeObj(ulid);
			    var value = $.trim($("#" + inId).val());
			    var  msg = value.split(",");
			    var nodeList = "";
			    l=msg.length;
			    if(l>0){
				    for (var i=0; i<l; i++) {
					    nodeList = treeObj.getNodeByParam("id", msg[i]);
					    if(nodeList!=null)
					    	treeObj.checkNode(nodeList,true);
				    }
			    }
			}
		//资源管理--资源搜索
		 function searchTree(str) {
			 var treeId = "treeview";
		      var zTree = $.fn.zTree.getZTreeObj(treeId);
		      if(/.*[\u4e00-\u9fa5]+.*$/.test(str)){
		    	  nodeList = zTree.getNodesByParamFuzzy("name", str);
		      }else{
		    	  nodeList = zTree.getNodesByParamFuzzy("id", str);
		      }
		      nodeList = zTree.transformToArray(nodeList);
		      treeUpdateNodes(true,treeId);
		 }
		 function treeUpdateNodes(highlight,treeId) {
            var zTree = $.fn.zTree.getZTreeObj(treeId);
            var allNode = zTree.transformToArray(zTree.getNodes());
            zTree.hideNodes(allNode);
            for(var n in nodeList){
                findParent(zTree,nodeList[n]);
            }
            zTree.showNodes(nodeList);
        }

		 //资源管理--上层目录
		 function showTree(i,ids,str,node,set){
			 $('.nj_button00').val('');//清空搜索内容
			 inId=ids;
		     index=i;
		     ulid=str;
		     setting=set;
		     $.fn.zTree.init($("#"+ulid), setting, node);
		     $("#rptContent ul").each(function(i,dom){
		     if(dom.id==ulid){
		             $("#"+dom.id).show();
		         }else{
		        	 $("#"+dom.id).hide()
		         }
		     });
	     var cityObj = $("#"+index);
	     var cityOffset = $("#"+index).offset();
	     var treeObj = $.fn.zTree.getZTreeObj(ulid);
	    // treeObj.checkAllNodes(false);
	     treeObj.showNodes(treeObj.transformToArray(treeObj.getNodes())) ;
	     $("#rptContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
	     
	   }
		//资源管理--关闭
		function closeDiv(){
		    $("#"+ulid).hide();
		    $("#rptContent").fadeOut("fast");
		}
		 //资源管理--取消
	    function cancel() {
	        $("#"+index).val('');
	        $("#"+inId).val('');
	    }
	    //资源管理--cognos
	    function showCognos(i,ids,str,node,setting){
	    	$('.nj_button00').val('');//清空搜索内容
	    	 inId=ids;
		     index=i;
		     ulid=str;
		    $.fn.zTree.init($("#"+ulid), setting, node);
		     $("#menuContent ul").each(function(i,dom){
		     if(dom.id==ulid){
		             $("#"+dom.id).show();
		         }else{
		          $("#"+dom.id).hide()
		         }
		        // console.log(dom.id);
		
		     });
	     var cityObj = $("#"+index);
	     var cityOffset = $("#"+index).offset();
	     var treeObj = $.fn.zTree.getZTreeObj(ulid);
	    // treeObj.checkAllNodes(false);
	     treeObj.showNodes(treeObj.transformToArray(treeObj.getNodes())) ;
	     $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
	     setValue();
	    }

