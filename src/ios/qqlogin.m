#import "qqlogin.h"
#include <sys/types.h>
#include <sys/sysctl.h>
#import <Cordova/CDVViewController.h>
#import <TencentOpenAPI/QQApiInterface.h>
@implementation qqlogin
-(void) qqlogins:(CDVInvokedUrlCommand *)command
{
    NSString *message = [command.arguments objectAtIndex:0];//这是我JavaScripte 传来的数据；
    
    BOOL arg = YES;
    CDVPluginResult* result;//
    
    if (arg)
    {
        // Success Callback
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"我返回的数据"];
        
      NSLog(@"登录");
    _tencentOAuth = [[TencentOAuth alloc] initWithAppId:@"222222" andDelegate:self];
    _permissions = [NSArray arrayWithObjects:
                     kOPEN_PERMISSION_GET_USER_INFO,
                     kOPEN_PERMISSION_GET_SIMPLE_USER_INFO,
                     kOPEN_PERMISSION_ADD_SHARE,
                      nil];
    

    [_tencentOAuth authorize:_permissions inSafari:NO];


    //self.pendingCommand = command;
        //[self writeJavascript:[result toSuccessCallbackString:command.callbackId]];//version=3.6
        [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];//version =4.0
    }
    else
    {
        // Error Callback
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"error"];
        [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];//version =4.0
        //[self writeJavascript:[result toErrorCallbackString:command.callbackId]];
    }
}
-(void) logout:(CDVInvokedUrlCommand *)command{
    CDVPluginResult* resultwrite;
    NSLog(@"退出登录成功，请重新登录");
    resultwrite=[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"this is data that i callback"];
    [self.commandDelegate sendPluginResult:resultwrite callbackId:command.callbackId];
}

/**
 * Called when the user successfully logged in.
 */
- (void)tencentDidLogin {
    // 登录成功
    NSLog(@"登录完成");

    
    if (_tencentOAuth.accessToken
        && 0 != [_tencentOAuth.accessToken length])
    {
        NSDictionary *info=[NSDictionary dictionaryWithObjectsAndKeys:_tencentOAuth.openId,@"uid",_tencentOAuth.accessToken,@"token" , nil];
        CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:info];
        NSLog(@"获得到的accessToken 为 %@",[_tencentOAuth accessToken]);
         NSLog(@"获得到的[_tencentOAuth openId] 为 %@",[_tencentOAuth openId]);
         NSLog(@"获得到的[_tencentOAuth expirationDate]  为 %@",[_tencentOAuth expirationDate]);
    

        
     }
    else
    {
         NSLog(@"登录失败 没有获取accesstoken");
        CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        //[self.commandDelegate sendPluginResult:result
         //                           callbackId:self.pendingCommand.callbackId];
       // self.pendingCommand = nil;


    }
    
  
}


/**
 * Called when the user dismissed the dialog without logging in.
 */
- (void)tencentDidNotLogin:(BOOL)cancelled
{
    if (cancelled){
        NSLog(@"用户取消登录");

    }
    else {
         NSLog(@"登录失败");

    }
    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
   // [self.commandDelegate sendPluginResult:result
       //                         callbackId:self.pendingCommand.callbackId];
    //self.pendingCommand = nil;

    
}

/**
 * Called when the notNewWork.
 */
-(void)tencentDidNotNetWork
{
     NSLog(@"无网络连接，请设置网络");
    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
  //  [self.commandDelegate sendPluginResult:result
       //                         callbackId:self.pendingCommand.callbackId];
  //  self.pendingCommand = nil;


}
/**
 *分享到QQ空间: 纯文本分享
 *
 */
-(void)sharetoqq:(CDVInvokedUrlCommand *)command{
    QQApiTextObject *txtObj = [QQApiTextObject objectWithText:@"QQ互联测试"];
    SendMessageToQQReq *req = [SendMessageToQQReq reqWithContent:txtObj];
    //将内容分享到qq
    QQApiSendResultCode sent = [QQApiInterface sendReq:req];
}
@end