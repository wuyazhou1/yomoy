var divImg = document.getElementById('lodingDiv');
if(divImg == '' ||divImg=='undefined'|| divImg=='null'){
    var divObj=document.createElement("div");
    divObj.setAttribute("id","loadingDiv");
    divObj.innerHTML='<div style="background:#000;opacity:0.5;filter:alpha(opacity=50);width:64px;height:64px;border-radius:5px;position:fixed;left:50%;top:50%;z-index:9999;transform:translate(-32px,-32px);"></div><img src="../img/loading.gif" style="width:48px;height:48px;position:fixed;left:50%;top:50%;z-index:9998;transform:translate(-24px,-24px);">';
    document.body.appendChild('divObj');


    document.onreadystatechange = function completeLoading() {
        //加载状态为complete时移除loading效果
        if (document.readyState == "complete") {
            document.body.removeChild(divObj);
        }
    }
}