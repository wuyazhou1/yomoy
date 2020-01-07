var myScroll;//滚动对象
/**
 * 对Date的扩展，将 Date 转化为指定格式的String 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 例子： (new
 * Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 (new
 * Date()).Format("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
 */
Date.prototype.format = function (fmt) { // author: meizz
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

//两个时间相差天数 兼容firefox chrome
function datedifference(sDate1, sDate2) {    //sDate1和sDate2是2006-12-18格式  
    var dateSpan,tempDate,iDays;
    sDate1 = Date.parse(sDate1);
    sDate2 = Date.parse(sDate2);
   
    dateSpan = sDate2 - sDate1;
    dateSpan = Math.abs(dateSpan);
    iDays = Math.floor(dateSpan / (24 * 3600 * 1000));
    return iDays
};

/**
 * 对String的扩展，去除前后空格
 */
String.prototype.trim=function() {
    return this.replace(/(^\s*)|(\s*$)/g,'');
};

/**
 * 将数值四舍五入后格式化.
 * @param num 数值(Number或者String)
 * @param cent 要保留的小数位(Number)
 * @param isThousand 是否需要千分位 0:不需要,1:需要(数值类型);
 * @return 格式的字符串,如'1,234,567.45'
 * @type String
 */
function formatNumber(num,cent,isThousand) {
	num = num.toString().replace(/\$|\,/g,'');

	// 检查传入数值为数值类型
	if(isNaN(num)) num = "0";

	// 获取符号(正/负数)
	sign = (num == (num = Math.abs(num)));

	 num = Math.floor(num*Math.pow(10,cent)+0.50000000001); // 把指定的小数位先转换成整数.多余的小数位四舍五入
	 cents = num%Math.pow(10,cent);       // 求出小数位数值
	 num = Math.floor(num/Math.pow(10,cent)).toString();  // 求出整数位数值
	 cents = cents.toString();        // 把小数位转换成字符串,以便求小数位长度

	 // 补足小数位到指定的位数
	 while(cents.length<cent) cents = "0" + cents;

	 if(isThousand) {
		 // 对整数部分进行千分位格式化.
	     for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
	     num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));
	 }

	 if (cent > 0) return (((sign)?'':'-') + num + '.' + cents);
	 
	 else return (((sign)?'':'-') + num);
};

/**
 * 根据键值对字符串的key获取value
 * @param key
 * @param str
 * @eg: 例如传入'name','name=Abel;age=23' return 'Abel'
 * @returns
 */
function getValueByStrKV(key,str){
	var arr,reg=new RegExp("(^|\\S)"+key+"=([^;]*)(;|$)");
	if(arr=str.match(reg))
	return unescape(arr[2]);
	else
	return null;
};

/**
 * 禁用按钮
 * @param id 元素ID
 * @returns
 */
function disabledBtn(id){
	var ele = document.getElementById(id);
	ele.disabled = true;
}

/**
 * 启用按钮
 * @param id 元素ID
 * @returns
 */
function startBtn(id){
	var ele = document.getElementById(id);
	ele.disabled = false;
}

//写cookies
function setCookie(name, value) {
	var Days = 15;
	var exp = new Date();
	exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
	document.cookie = name + "=" + escape(value) + ";expires="
			+ exp.toGMTString();
}

/**
 * get username from cookie
 */
function getCookie(name) {
	var cookieValue = '';
	var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
	if (arr = document.cookie.match(reg)) {
		cookieValue = unescape(arr[2]);
	}
	return cookieValue;

}

/**
 * clear all cookie
 */
function clearCookie(){ 
	var keys=document.cookie.match(/[^ =;]+(?=\=)/g); 
	if (keys) { 
		for (var i = keys.length; i--;) 
			document.cookie=keys[i]+'=0;expires=' + new Date(0).toUTCString() 
	} 
} 

/**
 * 图片处理 图片不存在显示图片不存在样图
 */
function imageDeal(imgUrl){
	/*if(imgUrl){
		return imageUrl+imgUrl;
	}else{//地址为空
		return "";//样图地址
	}
	*/
	if(imgUrl&&imgUrl.indexOf('http')==0){
		return imgUrl;
	}
	return imgUrl?(imageUrl+imgUrl):"/images/wt.png";
}
/**
 * 合并数组
 */
function meshArr(arr,arr1){
	for (var i = 0; i < arr1.length; i++) {
		arr.push(arr1[i]);
	}
	return arr;
}

/**
 * 从url获取参数
 */
function getURLParam(queryName) {
	var queryValue = '';
	var name, value, i;
	var str = location.href;
	var num = str.indexOf("?")
	str = str.substr(num + 1);
	var arrtmp = str.split("&");
	for (i = 0; i < arrtmp.length; i++) {
		num = arrtmp[i].indexOf("=");
		if (num > 0) {
			name = arrtmp[i].substring(0, num);
			value = arrtmp[i].substr(num + 1);
			if (queryName === name) {
				queryValue = value;
				break;
			}
		}
	}
    if(queryValue.indexOf("#")>=0){
      queryValue=queryValue.split("#")[0];
    }
	return queryValue;
}

/**
 * 开启等待动画
 */
function openWaitAnimation(obj){
	appObj.loading2=true;
	return;
	if(obj==null||obj==undefined||obj==""){
		obj="正在加载中...";
	}
	var loadEle = document.getElementById('loadDiv');
	if(!loadEle){
		var loadDivEle = document.createElement('div');
		loadDivEle.style.display = 'block';
		loadDivEle.setAttribute('id','loadDiv');
		loadDivEle.setAttribute('class','loading01 text-c red font12');
		var loadDivHTML =  ''
			   +  '	     <div class="loader">'
			   +  '	        <div class="loader-inner ball-clip-rotate-pulse">'
			   +  '	          <div></div>'
			   +  '	          <div></div>'
			   +  '	        </div>'
			   +  '	     </div>';
			  // +  '		 <div class="jizai" id="loadingDiv">'+obj+'</div>';
		loadDivEle.innerHTML = loadDivHTML;
		document.getElementsByTagName('body')[0].appendChild(loadDivEle);
	}else{
		//document.getElementById("loadingDiv").innerText=obj;
		loadEle.style.display = 'block';
		$("#loadDiv").attr("style","display:block;");
	}
	$("body").addClass("disabled");
}

/**
 * 关闭等待动画
 */
function closeWaitAnimation(){
	appObj.loading2=false;
	return;
	$("body").removeClass("disabled");
	var loadEle = document.getElementById('loadDiv');
	if(loadEle){
		loadEle.style.display = 'none';
		$("#loadDiv").attr("style","display:none;");
	}
}
/**
 * 
 * @param {}
 *            url 必须有
 * @param {}
 *            obj 请求参数 按照对象的形式 可为空
 * @param {}
 *            type 默认是POST 可为空
 * @param {}
 *            dataType 默认是json 可为空
 * @param {}
 *            successFun 请求成功回调的函数必须有
 * @param {}
 *            errorFun 请求失败回调的函数
 *            
 * @param loadAnimation 
 * 				val:true or false 是否开启动画
 * @author yw
 */
var appObj;
function jsonAjax(url, obj, successFun, errorFun, type, dataType,loadAnimation,isAsync) {
	if (url == undefined || url == null) {
		console.log("url不可以不填写");
		return;
	}
	if (!typeof successFun == 'function' || !typeof errorFun == 'function') {
		console.log("successFun 或者 errorFun  不是一个函数");
		return;
	}
	if (errorFun != undefined) {
		if (!typeof errorFun == 'function') {
			console.log("errorFun 不是一个函数");
			return;
		}
	}
	var sessionId=getURLParam("sessionId"),headObj={};//sesionid
	if(sessionId){
		//headObj["Cookie"]='SESSION='+sessionId;
		obj.sessionId=sessionId;
	}
	if (type == null || type == undefined || type == "") {
		type = 'POST';
	}
	if (dataType == null || dataType == undefined || dataType == "") {
		dataType = 'json';
	}
	
	if (loadAnimation === null || loadAnimation === undefined || loadAnimation === "") {
		loadAnimation = true;
	}
	
	if (isAsync === null || isAsync === undefined || isAsync === "") {
		isAsync = true;
	}

	if(loadAnimation){ openWaitAnimation()};

	$.ajax({
		url : url,
		data : obj,
		type : type,
		cache:false,
		async: isAsync,
		timeout : 60000,
		headers:headObj,
		dataType : dataType,
		success : function(res) {
			isLogin(res,successFun);
		},
		error : function(res) {
			errorFun(res);
		},
		complete : function(XHR, TR) {
			// 关闭等待动画
			closeWaitAnimation();
			if (TR == 'timeout') {// 超时
				//showJudegTip('fail','Time out,please refresh.');
			} else if (XHR.status == '500' || TR == 'error') {// 异常
				//showJudegTip('fail','Server error.');
				console.log("Server error.");
			}
		}
	});

}

function isLogin(res, successFun) {
	if (res.code == "10009") {
		clearLocalStorage();//清空缓存
		clearCookie();//清空cookie
		$("body").addClass("disabled");
		/*showSuccessWinTip("会话过期,重新登录","ever","确定",function(){
			wx.miniProgram.redirectTo({url:'/pages/index/index'});//回到首页
			//window.location.href="/index.html";
		});*/
		console.log("login out!!!");
		return;
	}else if(res.code == "10001"){//数据校验不通过
		//showJudegTip("fail",res.msg.replace(",","").replace(":",""));
		return;
	}else if(res.code == "10002"){//如果不是成功码
		//showJudegTip("fail",res.msg?res.msg:'系统繁忙');
		return;
	}else if(res.code=="10007"){
		//isShowTip();
	}
	successFun(res);
}

//组装img数据
function packageImgData(data){
	for(var i=0;i<data.length;i++){
		var obj=data[i];



		/*basicsId: "1"
		code: null
		createDate: 1563853530736
		fileNameUrl: "fileNameUrl1563853530675.jpg"
		filePath: "D:/home/uploadFile/images/2019/07/23/11"
		filePathUrl: "/ActivityCreate/getFile"
		id: 59
		imgCompress: "imgCompress1563853530675.jpg"
		orgCode: "0"
		projectUrl: "AmoskiUser"
		scheduleId: "1"
		state: "1"
		uploadTime: 1563853403052*/
		obj.originalBig='/'+obj.projectUrl+obj.filePathUrl+"?filePath="+obj.filePath+"/"+obj.fileNameUrl;
		obj.srcpic='/'+obj.projectUrl+obj.filePathUrl+"?filePath="+obj.filePath+"/"+obj.imgCompress;
		obj.text=obj.fileNameUrl.replace("fileNameUrl","");
	}
}

/**
 * 获取localStorage
 * @param key
 */
function getLocalStorage(key){
	return localStorage.getItem(key);
}

/**
 * 设置localStorage
 * @param key
 * @param value
 */
function setLocalStorage(key,value){
	localStorage.setItem(key,value);
}

/**
 * 根据key删除localStorage
 * @param key
 */
function removeLocalStorage(key){
	localStorage.removeItem(key);
}

/**
 * 清空localStorage
 */
function clearLocalStorage(){
	localStorage.clear();
}

/**
 * 获取localStorage
 * @param key
 */
function getSessionStorage(key){
	return sessionStorage.getItem(key);
}

/**
 * 设置localStorage
 * @param key
 * @param value
 */
function setSessionStorage(key,value){
	sessionStorage.setItem(key,value);
}

/**
 * 根据key删除localStorage
 * @param key
 */
function removeSessionStorage(key){
	sessionStorage.removeItem(key);
}

/**
 * 清空localStorage
 */
function clearSessionStorage(){
	sessionStorage.clear();
}
