var API_ROOT = 'http://192.168.5.155/';//测试环境
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
                 window.location.href = APT_Root2+'/loginregister.html';
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
