/*
var lastError;
console.oldError = console.error;
console.error = function(str) {
    window.top.windowLocationUrlLogin();
    console.oldError(str);
    lastError = str;
}*/
var consoleb={};
consoleb.oldlog = console.log;
consoleb.oldwarn = console.warn;
consoleb.olddebug = console.debug;
consoleb.oldinfo = console.info;
consoleb.olderror = console.error;
consoleb.oldtime = console.time;
consoleb.olddir = console.dir;
consoleb.oldprofile = console.profile;
consoleb.oldclear = console.clear;
consoleb.oldexception = console.exception;
consoleb.oldtrace = console.trace;
consoleb.oldassert = console.assert;
consoleb.olddirxml = console.dirxml;
consoleb.oldtimeEnd = console.timeEnd;
consoleb.oldgroup = console.group;
consoleb.oldgroupEnd = console.groupEnd;


console.log = function(str){
    consoleb.oldlog(str);
};
console.warn = function(str){
    consoleb.oldwarn(str);
};
console.debug = function(str){
    consoleb.olddebug(str);
};
console.info = function(str){
    consoleb.oldinfo(str);
};
console.error = function(str){
    consoleb.olderror(str);
};
console.time = function(str){
    consoleb.oldtime(str);
};
console.dir = function(str){
    consoleb.olddir(str);
};
console.profile = function(str){
    consoleb.oldprofile(str);
};
console.clear = function(str){
    consoleb.oldclear(str);
};
console.exception = function(str){
    consoleb.oldexception(str);
};
console.trace = function(str){
    consoleb.oldtrace(str);
};
console.assert = function(str){
    consoleb.oldassert(str);
};

