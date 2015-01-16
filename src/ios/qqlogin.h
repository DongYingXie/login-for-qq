#import <Cordova/CDVPlugin.h>
#import <TencentOpenAPI/TencentOAuth.h>
#import <UIKit/UIKit.h>
@interface qqlogin : CDVPlugin
{
	TencentOAuth* _tencentOAuth;
    NSMutableArray* _permissions;

}

-(void) Login:(CDVInvokedUrlCommand*)command;
-(void) Logout:(CDVInvokedUrlCommand*)command;
-(void) sharetoqq:(CDVInvokedUrlCommand*)command;
@end