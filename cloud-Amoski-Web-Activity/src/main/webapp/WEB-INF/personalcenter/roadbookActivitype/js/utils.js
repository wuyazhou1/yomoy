// axios.defaults.withCredentials=true;
//var API_ROOT = 'http://192.168.5.155/';//测试环境
var API_ROOT = '/';//测试环境
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
        try {
            document.body.removeChild(warpDiv);
        } catch (e) {
            console.log(e);
        }
        if(res.data.code=='0'){
            successFun(res);
        }else if (res.data.code == 10009) {
            window.location.href = APT_Root2+'/loginregister.html';
        } else {
            return res.data;
        }
    }).catch(error)
}


function axiosSendPost1(url,param,successFun,error){
    var divImg = document.getElementsByClassName('lodingDiv');
    if(divImg.length == 0){
        var warpDiv = document.createElement('div');
        warpDiv.innerHTML = '<div style="background:#000;opacity:0.5;filter:alpha(opacity=50);width:64px;height:64px;border-radius:5px;position:fixed;left:50%;top:50%;z-index:9999;transform:translate(-32px,-32px);"></div><img src="../img/loading.gif" class="lodingDiv" style="width:48px;height:48px;position:fixed;left:50%;top:50%;z-index:9999;transform:translate(-24px,-24px);">';
        document.body.appendChild(warpDiv);
    }

    if(param){
        param=Qs.stringify(param);
    }
    if (typeof successFun != 'function'){
        return ;
    }
    if (typeof error != 'function'){
        return ;
    }
    axios.post(API_ROOT+url,param,{
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        }
    }).then(function(res){
        document.body.removeChild(warpDiv);
        if(res.code==undefined){
            successFun(res.data);
            return;
        }else{
            if(res.code=='0'){
                successFun(res);
            }else if (res.data.code == 10009) {
                //window.location.href = APT_Root2+'/loginregister.html';
            } else {
                return res.data;
            }
        }
    }).catch(function(error){
        if(error.response!=null){
            if(error.response.headers!=null){
                if(error.response.headers.location!=null){
                    if(error.response.headers.location.indexOf("/AmoskiWebUser/AMOSKI/loginNameUser")!=-1){
                        //location.href="/AmoskiWebUser/AMOSKI/loginNameUser";
                        //window.parent
                        window.parent.windowLocationUrlLogin();
                    }
                }
            }
        }
    })
}
function postData(params,url) {
    var divImg = document.getElementsByClassName('lodingDiv');
    if(divImg.length == 0){
        var warpDiv = document.createElement('div');
        warpDiv.innerHTML = '<div style="background:#000;opacity:0.5;filter:alpha(opacity=50);width:64px;height:64px;border-radius:5px;position:fixed;left:50%;top:50%;z-index:9999;transform:translate(-32px,-32px);"></div><img src="../img/loading.gif" class="lodingDiv" style="width:48px;height:48px;position:fixed;left:50%;top:50%;z-index:9999;transform:translate(-24px,-24px);">';
        document.body.appendChild(warpDiv);
    }

    var headers={
        'Content-Type': 'application/json;charset=utf-8',
    }
    let Request = new Object();
    Request = GetRequest();
    var appToken=Request.appToken;//获取appToken
    if(appToken){
        headers['appToken']=appToken;
        sessionStorage.setItem("appToken",appToken);
    }else{
        appToken=sessionStorage.getItem("appToken");
        if(appToken){
            headers['appToken']=appToken;
        }
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

            }else if(res.data.code == 10005){//用户不存在
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
}

function backUrl(url,bl){
    if(url){
        let Request = new Object();
        Request = GetRequest();
        var type=Request.type;//获取appToken
        var from=Request.from;
        if(type=="app" && !from){
            window.location.href="gotoApp";
        }else{
            window.location.href=API_ROOT+'AmoskiWebActivity/personalcenter/'+url+(bl?"?type=app":'');
        }
    }else{
        window.history.go(-1);
    }
}
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
        time=new Date(t.replace(/-/g,'/'));
    }else{
        return "";
    }
    //time.setHours(time.getHours()-13);
    //return time.toLocaleString().split(" ")[0].replace(new RegExp('/',"gm"),'-');
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
var timeoutArr=[];
//endTime:结束时间，obj:字段名，idx：索引
function countdown (endTime,obj,idx) {
    // 目标日期时间戳
    const end = Date.parse(new Date(endTime))
    // 当前时间戳
    const now = Date.parse(new Date())
    // 相差的毫秒数
    const msec = end - now
    // 计算时分秒数
    let day = parseInt(msec / 1000 / 60 / 60 / 24)
    let hr = parseInt(msec / 1000 / 60 / 60 % 24)
    let min = parseInt(msec / 1000 / 60 % 60)
    let sec = parseInt(msec / 1000 % 60)
    // 个位数前补零
    hr = hr > 9 ? hr : '0' + hr
    min = min > 9 ? min : '0' + min
    sec = sec > 9 ? sec : '0' + sec
    if(day > 0 || hr>0 || min>0 || sec>0){
    // 控制台打印
        app._data.useData[idx][obj]= `${day} / ${hr} : ${min} : ${sec}`;
    }else{
        app._data.useData[idx][obj]=0;
    }
    // app._data.useData[idx].msec = msec;
    // 一秒后递归
    var tl= setTimeout(function (){
        countdown(endTime,obj,idx)
    }, 1000)
    app._data.useData[idx].timeoutObj=tl;

}

function swperLoop(){
    var swiper = new Swiper('.swiper-container', {
        // slidesPerView: 3,
        // spaceBetween: 30,
        // centeredSlides: true,
        // loop: true,
        pagination: {
            el: '.swiper-pagination',
            clickable: true,
        },
        on: {
            transitionEnd: function(){
                // alert(this.activeIndex);
                app._data.reundList = app._data.reundData.list[this.activeIndex];
                console.log(app._data.reundList);
            },
        }
    });
}

function formatSeconds(value) {
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



window.onload = function(){

}

//判断是否为app
let appplate = true;
let Request = new Object();
Request = GetRequest();
appplate =  Request.platform;
if(appplate == 'app')
{
    appplate = false;

}else{
    appplate = true
}

var u = navigator.userAgent, app = navigator.appVersion;
var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
if(isAndroid)
{
    document.getElementsByTagName('body')[0].className = 'uh_ios7';
}