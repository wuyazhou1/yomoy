axios.defaults.withCredentials=true;
// var API_ROOT = 'http://192.168.5.177:8111/';//测试环境
var API_ROOT = 'http://192.168.5.177:8111/';//测试环境
function axiosSendPost(url,param,successFun,error){
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
    }).then(successFun).catch(error)
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

function TestEleven(now){
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

