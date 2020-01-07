axios.defaults.withCredentials=true;
//var API_ROOT = 'http://192.168.5.155/';//测试环境
// var API_ROOT = 'http://192.168.5.181/';//测试环境
var API_ROOT = '/';//测试环境
 function postData(params,url){

     // var divImg = document.getElementsByClassName('lodingDiv');
     // if(divImg.length == 0){
     //     var warpDiv = document.createElement('div');
     //     warpDiv.innerHTML = '<div style="background:#000;opacity:0.5;filter:alpha(opacity=50);width:64px;height:64px;border-radius:5px;position:fixed;left:50%;top:50%;z-index:9999;transform:translate(-32px,-32px);"></div><img src="../img/loading.gif" class="lodingDiv" style="width:48px;height:48px;position:fixed;left:50%;top:50%;z-index:9999;transform:translate(-24px,-24px);">';
     //     document.body.appendChild(warpDiv);
     // }

     return axios.post(API_ROOT+url, params,
        {
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            }
        })
};

function backUrl(url,bl){
    // window.history.go(-1)
    if(url){
        let Request = new Object();
        Request = GetRequest();
        var type=Request.type;//获取appToken
        if(type=="app"){
            window.location.href="gotoApp";
        }else{
            window.location.href=API_ROOT+'AmoskiWebActivity/personalcenter/'+url+(bl?"?type=app":'');
        }
    }else{
        window.history.go(-1);
    }
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