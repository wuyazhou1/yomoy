//var API_ROOT = 'http://192.168.5.181/';//测试环境
var API_ROOT = '/';//测试环境
 function postData(params,url) {
    return axios.post(API_ROOT+url, params,
        {
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            }
        })
        .then(function (res) {
            if (res.data.code == 10009){//登陆超时
                if(isWeiXin())
                {//判断为微信浏览器时
                    window.location.href = baseUrl+'/AmoskiActivity/memberUser/loginOAuth';
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

function backUrl(){
    window.history.go(-1)
}
