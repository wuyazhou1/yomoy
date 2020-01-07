var API_ROOT = '';//测试环境
let imgWidth = 630;
let imgHeight = 336;


/*var script=document.createElement("script");
script.type="text/javascript";
script.src="/AmoskiWebHtmlUser/js/qs.js";
document.getElementsByTagName('head')[0].appendChild(script);
script.onload=function(){

}*/

function axiosSendPost(url,param,successFun,error){
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
 function postData(url,params,successFun,error) {
    return axios.post(API_ROOT+url, params,
        {
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            }
        })
        .then(function (res) {
            if(res.code==undefined){
                successFun(res);
                return;
            }else{
                if(res.code=='0'){
                    successFun(res);
                }
            }
        })
        .catch(function (res) {
            return res;
        });
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
            console.log(">>>>>>>>>>>>>res:"+res);
        })
        .catch(function (res) {
            return res;
        });
};

/**
 * 文件上传
 * @returns {Object}
 * @constructor
 */
function fileUploadSuccess(res){
    var filePathArr=[];
    if(res.code=="00"){
        var data=res.resultData;
        for(var i=0;i<data.length;i++){
            var obj=data[i];
            filePathArr.push(joinImgSrc(obj.projectUrl+obj.filePathUrl,obj.fileNameUrl));
        }
    }
    return filePathArr;
}
function joinImgSrc(getFileUrl,src){
    if(src){
        return "/"+getFileUrl+"?filePath="+src;
    }
    return "";
}
function packageImgData(obj){
    var newObj=new TbActivityBillImageEntity();
    if(obj){
        newObj.id=obj.id;
        newObj.fileNameUrl=obj.fileNameUrl;
        newObj.filePathUrl=obj.filePathUrl;
        newObj.projectUrl=obj.projectUrl;
    }
    return newObj;
}
/**
 * 图片数据获取
 * @returns {Object}
 * @constructor
 */
function getImgFile(filePath){
    var requestUrl="/AmoskiUser/ActivityCreate/getFile";
    axiosSendPost(requestUrl,{fileNameUrl:filePath},function(res){

    },function(res){
    });
}

/**
 * 查询字典数据
 * @returns {Object}
 * @constructor
 */
function queryDict(code,listName,func){
    var requestUrl="/AmoskiWebHtmlUser/Dict/GetDictZtree";
    axiosSendPost(requestUrl,{parentCode:code},function(res){
        if(typeof func == 'function'){
            func(res);
        }else{
            app._data[listName]=res;
            app.$forceUpdate();
        }
    },function(res){
    });
}

/**
 * 字符数字判断 并转成int
 */
function strToNum(str){
    if(!str||isNaN(str)){
        return 0;
    }else{
        return parseInt(str);
    }
}

/**
 * 保存发布活动
 * @returns {Object}
 * @constructor
 */
var ajaxParam
function saveOrPutActivity(state){
    if(window["onlocadWindow"]!=null){
        window["onlocadWindow"]();
    }
    if(window["isOrPutActivity"]!=null){
        var bool = window["isOrPutActivity"]();
        if(!bool){
            return;
        }
    }
    window.parent.ShowLayerWait();
    window.top.activityCreateList[window.top.activityIndex].tbActivityBasicsEntity.state=state;
    console.log(window.top.activityCreateList);
    ajaxParam=JSON.stringify(window.top.activityCreateList);
    setTimeout(function(){
        console.log(ajaxParam);
        var requestUrl="/AmoskiUser/ActivityCreate/SaveActivity";
        axiosSendPost(requestUrl,{activity:ajaxParam},function(res){
            if(res.code=="000"){
                window.parent.ShowLayerMsg(res.message);
                window.location.href="../activityManage/index.html";
            }else{
                window.parent.ShowLayerMsg(res.message);
            }
        },function(res){
            window.parent.ShowLayerMsg("操作失败");
        });
    },500);
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
    document.body.appendChild(divObj);
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
var app;
//禁用 启用表单
function lookDisabled(bl){
    if(bl==null||bl==undefined){
        bl=window.top.lookActivity;
    }
    app._data.formDisabled=bl;
}


function backUrl(){
    window.history.go(-1)
}


