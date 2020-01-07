/**
 * Created by unbregg on 2016/3/16.
 */

var bridge = null;
var slice = [].slice;
var native = window.na = window.native = {
  callbacks: {}
};
alert("$.device.ios"+$.device.ios);
if($.device.ios){
  function connectWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) {
      callback(WebViewJavascriptBridge)
    } else {
      document.addEventListener('WebViewJavascriptBridgeReady', function() {
        callback(WebViewJavascriptBridge)
      }, false)
    }
  }

  connectWebViewJavascriptBridge(function(bridge) {
    bridge.init(function(message, responseCallback) {
      if(message.callbackId){
        native.callback(message.data,message.callbackId);
      }
    });
    native.bridge = bridge;
  });
}


native.invoke = function (nativeMethod) {

  var argsMap = this._normalizeArgs.apply(this, arguments);
  var jsMethod = argsMap.jsMethod;
  var uuid = this._storeCallback(jsMethod);
  var nativeArgs = argsMap.nativeArgs;

  var _nativeMethod = argsMap.nativeMethod;
  if($.device.ios){
    // alert("!native.bridge"+(!native.bridge)+"arguments"+arguments+"uuid"+uuid)
    if(!native.bridge){
      setTimeout(function(){
        delete native.callbacks[uuid];
        native.invoke.apply(native,arguments);
      },100);
    }else{
      native.bridge.send({action:_nativeMethod,data:nativeArgs,callbackId:uuid});
    }
  }else{
    if(uuid){
      nativeArgs.push(uuid);
    }
   window.app[_nativeMethod].apply(window.app, nativeArgs);
  }
};


native._normalizeArgs = function () {
  var jsMethod = slice.call(arguments, -1)[0];
  var nativeMethod = arguments[0];
  var nativeArgs = [];

  if (typeof jsMethod === 'function') {
    nativeArgs = slice.call(arguments, 1, -1);
  } else {
    jsMethod = null;
    nativeArgs = slice.call(arguments, 1, arguments.length);
  }
  return {
    jsMethod: jsMethod,
    nativeMethod: nativeMethod,
    nativeArgs: nativeArgs
  };
};
native._storeCallback = function (jsMethod) {
  var uuid;

  if (typeof jsMethod === 'function') {
    uuid = this._generateUuid();
    this.callbacks[uuid] = jsMethod;
  }
  return uuid;
};
native.callback = function (args,uuid) {
  var callback = this.callbacks[uuid];
  if(typeof args === 'string'){
    args = JSON.parse(args);
  }

  if (typeof callback === 'function') {
    callback.apply(window.native, [args]);
    delete this.callbacks[uuid];
  }
};
native._jsMethodUuid = 0;
native._generateUuid = function () {
  return ++native._jsMethodUuid + '';
};
