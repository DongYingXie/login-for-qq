
var exec = require('cordova/exec');

module.exports={
	Login:function(messege,success, error) {
    exec(success, error, "qqlogin", "qqlogins", [messege]);
},
   Logout:function(messege,_title,success, error) {
    exec(success, error, "qqlogin", "logout", [messege]);
}
Sharetoqq:function(messege,success, error) {
    exec(success, error, "qqlogin", "sharetoqq", [messege]);
}
}