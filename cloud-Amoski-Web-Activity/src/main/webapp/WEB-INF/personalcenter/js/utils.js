axios.defaults.withCredentials=true;
//var API_ROOT = 'http://192.168.5.155/';//测试环境
//var  APT_Root2 = '';//本地使用
// var API_ROOT = 'http://122.114.91.150/';//线上环境

//var API_ROOT = 'http://192.168.5.155/';//测试环境
var baseUrl='http://yomoy.com.cn/';
var baseFileUrl='/home/uploadFile/images';
//var API_ROOT = 'http://192.168.5.177/';//测试环境
// var API_ROOT = 'http://17n97122k7.imwork.net/';//测试环境
// var API_ROOT = 'http://122.114.163.29:8761/';//测试环境
var API_ROOT='/';//微服务使用
var  APT_Root2 = '/AmoskiWebActivity/personalcenter';//微服务使用

function axiosSendPost(url,param,successFun,error){
	var divImg = document.getElementsByClassName('lodingDiv');
	if(divImg.length == 0){
		var warpDiv = document.createElement('div');
		warpDiv.innerHTML = '<div style="background:#000;opacity:0.5;filter:alpha(opacity=50);width:64px;height:64px;border-radius:5px;position:fixed;left:50%;top:50%;z-index:9999;transform:translate(-32px,-32px);"></div><img src="../img/loading.gif" class="lodingDiv" style="width:48px;height:48px;position:fixed;left:50%;top:50%;z-index:9999;transform:translate(-24px,-24px);">';
		document.body.appendChild(warpDiv);
	}
	if(param){
		param=JSON.stringify(param)
	}
	if (typeof successFun != 'function'){
		return ;
	}
	if (typeof error != 'function'){
		return ;
	}
	axios.post(API_ROOT+url,param,{
		headers: {'Content-Type': 'application/json;charset=utf-8'}
	}).then(function(res){
		document.body.removeChild(warpDiv);
		if(res.data.code=='0'){
			successFun(res);
		}else if (res.data.code == 10009) {
			if(isWeiXin())
			{//判断为微信浏览器时
				window.location.href = '/AmoskiActivity/memberUser/loginOAuth?gotoUrl='+window.location.href.substr(window.location.href.indexOf("AmoskiWebActivity")-1);
			}else{//判断为其他浏览器时
				window.location.href = API_ROOT+'AmoskiWebActivity/personalcenter/logintel.html';
			}
		} else {
			return res.data;
		}
	}).catch(error)
}
function postData(params,url) {
	// var headers={
	//     'Content-Type': 'application/json;charset=utf-8',
	// }
	// var appToken=sessionStorage.getItem('appToken');//获取appToken
	// if(appToken){
	//     headers['appToken']=appToken;
	// };
	//  let Request = new Object();
	//  Request = GetRequest();

	//loading
	var divImg = document.getElementsByClassName('lodingDiv');
	if(divImg.length == 0){
		var warpDiv = document.createElement('div');
		warpDiv.innerHTML = '<div style="background:#000;opacity:0.5;filter:alpha(opacity=50);width:64px;height:64px;border-radius:5px;position:fixed;left:50%;top:50%;z-index:9999;transform:translate(-32px,-32px);"></div><img src="../img/loading.gif" class="lodingDiv" style="width:48px;height:48px;position:fixed;left:50%;top:50%;z-index:9999;transform:translate(-24px,-24px);">';
		document.body.appendChild(warpDiv);
	}

	var headers={
		'Content-Type': 'application/json;charset=utf-8',
	};

	let Request = new Object();
	Request = GetRequest();
	var appToken=Request.appToken;//获取appToken
	if(appToken){
		headers['appToken']=appToken;
	}
	return axios.post(API_ROOT+url, params,
		{
			withCredentials: true,
			headers: headers
		})
		.then(function (res) {

			document.body.removeChild(warpDiv);
			if (res.data.code == 10009){//登陆超时
				if(isWeiXin())
				{//判断为微信浏览器时
					window.location.href = '/AmoskiActivity/memberUser/loginOAuth?gotoUrl='+window.location.href.substr(window.location.href.indexOf("AmoskiWebActivity")-1);
				}else{//判断为其他浏览器时
					window.location.href = API_ROOT+'AmoskiWebActivity/personalcenter/logintel.html';
				}
			}
			else if(res.data.code == 10005){//用户不存在
				errorTip(res.data.msg,true);
			}
			else if(res.data.code == 10007){//账号和密码错误
				errorTip(res.data.msg,true);
			}else if(res.data.code == 20002){
				errorTip(res.data.msg,true);
			}else if(res.data.code == 20004){
				errorTip(res.data.msg,true);
				window.location.href = APT_Root2+'/logintel2.html';
			}
			else if(res.data.code == 20005){//验证码2分钟有效
				// errorTip(res.data.msg,true);
				if(params.type==1){
					window.location.href =APT_Root2+ '/registerverifcode.html';
				}else if(params.type==2){
					window.location.href =APT_Root2+ '/verificodefreepass.html';
				}else if(params.type==3){
					window.location.href = APT_Root2+'/changepassword.html';
				}
			} else {
				return res.data;
			}
		})
		.catch(function (res) {
			return res;
		});
};

function isWeiXin(){
	var ua = window.navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i) == 'micromessenger'){
		return true;
	}else{
		return false;
	}
};

function postDataForm(params,url) {
	return axios.post(API_ROOT+url, params,
		{
			withCredentials: true,
			headers: {
				'Content-Type': 'multipart/form-data'
			}
		})
		.then(function (res) {
			if (res.data.code == 10009){
				window.location.href = baseUrl+'/AmoskiActivity/memberUser/loginOAuth';
			} else {
				return res.data;
			}
		})
		.catch(function (res) {
			return res;
		});
};


function GetRequest(){
	var url = location.search; //获取url中"?"符后的字串
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for(var i = 0; i < strs.length; i ++) {
			theRequest[strs[i].split("=")[0]]=decodeURI((strs[i].split("=")[1]));
		}
	}
	return theRequest;
}

function getFmtTime(t,fmt){
	var time=new Date();
	fmt=fmt||"yyyy-MM-dd";
	if(t){
		time=new Date(t);
	}else{
		return "";
	}
	//年
	if(fmt.indexOf("yyyy")>=0){
		fmt=fmt.replace("yyyy",time.getFullYear());
	}

	//月
	if(fmt.indexOf("MM")>=0){
		var month=time.getMonth()+1;
		if(month<10){
			month="0"+month;
		}
		fmt=fmt.replace("MM",month);
	}else if(fmt.indexOf("M")>=0){
		fmt=fmt.replace("M",time.getMonth()+1);
	}

	//日
	if(fmt.indexOf("dd")>=0){
		var date=time.getDate();
		if(date<10){
			date="0"+date;
		}
		fmt=fmt.replace("dd",date);
	}else if(fmt.indexOf("d")>=0){
		fmt=fmt.replace("d",time.getDate());
	}

	//时
	if(fmt.indexOf("HH")>=0){
		var hour=time.getHours();
		if(hour<10){
			hour="0"+hour;
		}
		fmt=fmt.replace("HH",hour);
	}else if(fmt.indexOf("H")>=0){
		fmt=fmt.replace("H",time.getHours());
	}

	//分
	if(fmt.indexOf("mm")>=0){
		var minute=time.getMinutes();
		if(minute<10){
			minute="0"+minute;
		}
		fmt=fmt.replace("mm",minute);
	}else if(fmt.indexOf("m")>=0){
		fmt=fmt.replace("m",time.getMinutes());
	}

	//秒
	if(fmt.indexOf("ss")>=0){
		var second=time.getSeconds();
		if(second<10){
			second="0"+second;
		}
		fmt=fmt.replace("ss",second);
	}else if(fmt.indexOf("s")>=0){
		fmt=fmt.replace("s",time.getSeconds());
	}
	return fmt;
}

function TestEleven(now){//获取最近七天数据
	//字符串日期转成时间戳
	var stringTime = now;
	stringTime=stringTime+" 12:00:00";
	var timestampTemp = Date.parse(new Date(stringTime));
	//构造最近8天的日期数组
	var results=[];
	for(var i=0;i<7;i++){
		results.push(this.getFmtTime(timestampTemp-(i*86400*1000),'yyyy-MM-dd'));
	}
	return results;
}

function scuessTip(scuesstxt,alertTipscuess){//成功提示
	var html = '<div class="box" v-show='+alertTipscuess+'><div class="box1"><span class="scuessIcon"><img src="../img/scuessIcon.png" width="100%" height="100%"></span><p class="text">'+scuesstxt+'</p></div></div>';
	var divObj=document.createElement("div");
	divObj.innerHTML=html;
	if(alertTipscuess)
	{
		document.body.appendChild(divObj);
		setTimeout(function(){
			document.body.removeChild(divObj);
		},2000)
	}else{
		document.body.removeChild(divObj);
	}
}

function errorTip(errortxt,alertTiperror){//失败提示
	var html = '<div class="box" v-show='+alertTiperror+'><div class="box1"><span class="scuessIcon"><img src="../img/errorIcon.png" width="100%" height="100%"></span><p class="text">'+errortxt+'</p></div></div>';
	var divObj=document.createElement("div");
	divObj.innerHTML=html;
	// document.body.appendChild(divObj);
	if(alertTiperror)
	{
		document.body.appendChild(divObj);
		setTimeout(function(){
			document.body.removeChild(divObj);
		},2000)
	}else{
		document.body.removeChild(divObj);
	}
}

function inputreal(name){
	document.body.scrollTop = document.body.scrollHeight;
	//let _this = this;
	setTimeout(function(){
		document.getElementById(name).scrollIntoView(true);
	}, 100);
}

//    使用canvas对大图片进行压缩
function compress(img,_this) {
	var initSize = img.src.length;
	var width = img.width;
	var height = img.height;
	var ctx = _this.canvas.getContext('2d');
	var tctx = _this.tCanvas.getContext("2d");
	//如果图片大于四百万像素，计算压缩比并将大小压至400万以下
	var ratio;
	if ((ratio = width * height / 4000000) > 1) {
		ratio = Math.sqrt(ratio);
		width /= ratio;
		height /= ratio;
	} else {
		ratio = 1;
	}
	_this.canvas.width = width;
	_this.canvas.height = height;
//        铺底色
	ctx.fillStyle = "#fff";
	ctx.fillRect(0, 0, _this.canvas.width, _this.canvas.height);
	//如果图片像素大于100万则使用瓦片绘制
	var count;
	if ((count = width * height / 1000000) > 1) {
		count = ~~(Math.sqrt(count) + 1); //计算要分成多少块瓦片
//            计算每块瓦片的宽和高
		var nw = ~~(width / count);
		var nh = ~~(height / count);
		_this.tCanvas.width = nw;
		_this.tCanvas.height = nh;
		for (var i = 0; i < count; i++) {
			for (var j = 0; j < count; j++) {
				tctx.drawImage(img, i * nw * ratio, j * nh * ratio, nw * ratio, nh * ratio, 0, 0, nw, nh);
				ctx.drawImage(_this.tCanvas, i * nw, j * nh, nw, nh);
			}
		}
	} else {
		ctx.drawImage(img, 0, 0, width, height);
	}
	//进行最小压缩
	var ndata = _this.canvas.toDataURL('image/jpeg', 0.6);
	console.log('压缩前：' + initSize);
	console.log('压缩后：' + ndata.length);
	console.log('压缩率：' + ~~(100 * (initSize - ndata.length) / initSize) + "%");
	_this.tCanvas.width = _this.tCanvas.height = _this.canvas.width = _this.canvas.height = 0;


	return ndata;
}

//base64转二进制
function dataURLtoBlob(dataurl) {
	var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
		bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
	while(n--){
		u8arr[n] = bstr.charCodeAt(n);
	}
	return new Blob([u8arr], {type:mime});
}

//秒转时分秒
function formatSeconds(value) {fillmeal
	var secondTime = parseInt(value);// 秒
	var minuteTime = 0;// 分
	var hourTime = 0;// 小时
	if(secondTime > 60) {//如果秒数大于60，将秒数转换成整数
		//获取分钟，除以60取整数，得到整数分钟
		minuteTime = parseInt(secondTime / 60);
		//获取秒数，秒数取佘，得到整数秒数
		secondTime = parseInt(secondTime % 60);
		//如果分钟大于60，将分钟转换成小时
		if(minuteTime > 60) {
			//获取小时，获取分钟除以60，得到整数小时
			hourTime = parseInt(minuteTime / 60);
			//获取小时后取佘的分，获取分钟除以60取佘的分
			minuteTime = parseInt(minuteTime % 60);
		}
	}
	var result = "" + parseInt(secondTime) + "";

	if(minuteTime > 0) {
		result = "" + parseInt(minuteTime) + ":" + result;
	}
	if(hourTime > 0) {
		result = "" + parseInt(hourTime) + ":" + result;
	}
	return result;
}