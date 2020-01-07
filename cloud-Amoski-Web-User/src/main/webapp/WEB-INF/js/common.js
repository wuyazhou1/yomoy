
/*弹出层*/
/*
	参数解释：
	title	标题
	url		请求的url
	id		需要操作的数据id
	w		弹出层宽度（缺省调默认值）
	h		弹出层高度（缺省调默认值）
*/
function layer_show(title,url,w,h){
	if (title == null || title == '') {
		title=false;
	};
	if (url == null || url == '') {
		url="404.html";
	};
	if (w == null || w == '') {
		w=800;
	};
	if (h == null || h == '') {
		h=($(window).height() - 50);
	};
	layer.open({
	      type: 2,
	      title: title,
	      shadeClose: true,
	      fix: false, //不固定
	      hade:0.4,
	      maxmin: true, //开启最大化最小化按钮
	      area: [w+'px', h +'px'],
	      content: url
	});
}

/*弹出层*/
/*
	参数解释：
	title	标题
	url		请求的url
	id		需要操作的数据id
	w		弹出层宽度（缺省调默认值）
	h		弹出层高度（缺省调默认值）
*/
function layer_html(title,id,w,h){
	if (title == null || title == '') {
		title=false;
	};
	if (w == null || w == '') {
		w=800;
	};
	if (h == null || h == '') {
		h=($(window).height() - 50);
	};
	layer_index = layer.open({
	      type: 1,
	      title: title,
	      shadeClose: true,
	      fix: false, //不固定
	      hade:0.4,
	      maxmin: true, //开启最大化最小化按钮
	      area: [w+'px', h +'px'],
	      content: $(id)
	});
}

function layer_maxhtml(title,id){
	if (title == null || title == '') {
		title=false;
	};
	layer.open({
	      type: 1,
	      title: title,
	      shadeClose: true,
	      fix: false, //不固定
	      hade:0.4,
	      maxmin: true, //开启最大化最小化按钮
	      area: ['100%', '100%'],
	      content: $(id)
	});
}
//新增
function layer_maxUrl(title,url,w,h){
	layer.open({
	      type: 2,
	      title: title,
	      shadeClose: true,
	      fix: false, //不固定
	      hade:0.4,
	      maxmin: true, //开启最大化最小化按钮
	      area: ['100%', '100%'],
	      content: url + "?htmlState=1"
	});
}
/*全局变量  弹出加载层的序列*/
var layer_index = null;
/*
 *加载层 type传值0~2图标变化 也可不传 
 * */
function layer_wait(type){
	if(typeof(type)=="undefined"){
		type=1;
	}
	layer_index = layer.load(type, {
		shade: [0.1,'#fff'] 
	});
};


/*关闭弹出框口*/
function layer_close(){
	layer.close(layer_index);
}

function layer_msg(content,type){
	if(type==null||type==""){
		type=1;
	}
	layer.alert(content, {icon: type,skin: 'layer-ext-moon',time:2000})
};

/*上传*/
$(document).on("change",".input-file",function(){
	var uploadVal=$(this).val();
	$(this).parent().find(".upload-url").val(uploadVal).focus().blur();
});

/**
 * hookAjax 可以设置未来(全局)的AJAX请求默认选项
 * 主要设置了AJAX请求遇到Session过期的情况
 */
hookAjax({
    //拦截回调
    onreadystatechange:function(xhr){
		if(xhr.response!=null){
			if(xhr.response.headers!=null){
				if(xhr.response.headers.location!=null){
					if(xhr.response.headers.location.indexOf("/AmoskiWebUser/AMOSKI/loginNameUser")!=-1){
						//location.href="/AmoskiWebUser/AMOSKI/loginNameUser";
						//window.parent
						window.parent.windowLocationUrlLogin();
					}
				}
			}
		}
    	var responseText = "";
    	if(xhr.status==404){
    		layer.msg("请求无效，请重试！", {
	 			icon : 6,
	 			time : 3000
	 		});
	     	return true;
	    }else if(xhr.status==503){
	     	layer.msg("服务不可用，请重试！", {
	 			icon : 6,
	 			time : 3000
	 		});
	     	return true;
	    }else if (xhr.readyState==4 && xhr.status==200){
    		responseText = xhr.responseText;
        }else{
        	return;
        }

        var responseJson = JSON.parse(responseText);
        var code = responseJson.code;
        var message = responseJson.message;
        if(code == 'timeout') {
            var top = getTopWinow();
            layer.msg(message, { icon : 6, time : 2000 });
            window.setTimeout(function(){
            	top.location.href = ctx+'login.jsp';
			},2000);
            return true;
        }
       
    }
})


$.ajaxSetup({
    type : 'POST',
    timeout: 300000,
    beforeSend : function() {
		layer_wait();
	},
	complete: function(XMLHttpRequest ,status){
		layer_close();
		
	},
    error : function(jqXHR, textStatus, errorMsg) {
    	// jqXHR 是经过jQuery封装的XMLHttpRequest对象
    	// textStatus 可能为： null、"timeout"、"error"、"abort"或"parsererror"
    	// errorMsg 可能为： "Not Found"、"Internal Server Error"等
    	alert( '发送AJAX请求到"' + this.url + '"时出错[' + jqXHR.status + ']['+textStatus+']：' + errorMsg );    
		layer.msg("请求中断，请重试！", {
			icon : 6,
			time : 3000
		});
	} 
});


/**
 * 在页面中任何嵌套层次的窗口中获取顶层窗口
 * @return 当前页面的顶层窗口对象
 */
function getTopWinow(){
    var p = window;
    while(p != p.parent){
        p = p.parent;
    }
    return p;
}


/*window.alert = function(str){
	return ;
}*/