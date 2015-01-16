package cn.debi.cordova;

import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.tencent.tauth.Tencent;

import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.share.QzoneShare;
import com.tencent.connect.UserInfo;

public class qqlogin extends CordovaPlugin {
	public static QQAuth mQQAuth;
	private UserInfo mInfo;
	private int shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
	public static final String APPID = "222222";
	// private static final String APPID = "100363349";
	private Tencent mTencent = null;
	private CallbackContext mCallbackContext = null;
	JSONObject useInfo = new JSONObject();
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				JSONObject response = (JSONObject) msg.obj;
				if (response.has("nickname")) {
					try {

						Log.d("nickname", response.getString("nickname"));
						Log.d("figureurl", response.getString("figureurl"));
						Log.d("gender", response.getString("gender"));
						Log.d("figureurl_qq_1",
								response.getString("figureurl_qq_1"));
						Log.d("figureurl_qq_2",
								response.getString("figureurl_qq_2"));
						Log.d("figureurl_1", response.getString("figureurl_1"));
						Log.d("figureurl_2", response.getString("figureurl_2"));
						useInfo.put("nickname", response.getString("nickname"));
						useInfo.put("figureurl",
								response.getString("figureurl"));
						useInfo.put("gender", response.getString("gender"));
						useInfo.put("figureurl_qq_1",
								response.getString("figureurl_qq_1"));
						mCallbackContext.success(useInfo);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (msg.what == 1) {
				Bitmap bitmap = (Bitmap) msg.obj;
			}
		}

	};

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) {
		mCallbackContext = callbackContext;
		if (action.equals("qqlogins")) {
		 this.Login();
			
		} else if (action.equals("logout")) {

		} else {
			return false;
		}
		return true;
	}

	/**
	 * QQ登录
	 */
	public void Login() {
		final IUiListener listener = new BaseUiListener() {
			@Override
			protected void doComplete(JSONObject values) {
				updateUserInfo();

			}
		};
		// 创建授权认证信息
		final Activity activity = this.cordova.getActivity();
		Context context = this.cordova.getActivity().getApplicationContext();
		// 创建实例
		mTencent = Tencent.createInstance(APPID, context);
		this.cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mTencent.login(activity, "all", listener);

			}
		});
	}

	// QQ登录 end
	//
	/**
	 * 获取用户登录的信息
	 */
	private void updateUserInfo() {
		final Activity activity = this.cordova.getActivity();

		Context context = this.cordova.getActivity().getApplicationContext();
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onComplete(final Object response) {
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					mHandler.sendMessage(msg);
					new Thread() {
						@Override
						public void run() {
							JSONObject json = (JSONObject) response;
							if (json.has("figureurl")) {
								Bitmap bitmap = null;
								try {
									Log.d("bitmap",
											json.getString("figureurl_qq_2"));
								} catch (JSONException e) {

								}
								Message msg = new Message();
								msg.obj = bitmap;
								msg.what = 1;
								mHandler.sendMessage(msg);
							}
						}

					}.start();
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub

				}
			};
			// MainActivity.mTencent.requestAsync(Constants.GRAPH_SIMPLE_USER_INFO,
			// null,
			// Constants.HTTP_GET, requestListener, null);
			mInfo = new UserInfo(context, mTencent.getQQToken());
			mInfo.getUserInfo(listener);

		} else {

		}
	}

	//
	/**
	 * 退出QQ登录
	 */
	public void logout() {
		Context context = this.cordova.getActivity().getApplicationContext();
		mTencent.logout(context);
		mCallbackContext.success();
	}

	// 实现回调 IUiListener
	private class BaseUiListener implements IUiListener {

		// public void onComplete(Object response) {
		// String uid=mTencent.getOpenId();
		// String token=mTencent.getAccessToken();
		// Log.d("uid111",uid);
		//
		//
		// JSONObject res=new JSONObject();
		// try {
		//
		// res.put("uid", uid);
		// res.put("token", token);
		// mCallbackContext.success(res);
		//
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// mCallbackContext.error(0);
		// e.printStackTrace();
		// }
		//
		// }
		// 我添加的
		@Override
		public void onComplete(Object response) {

			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			mCallbackContext.error(0);

		}

		@Override
		public void onCancel() {

			mCallbackContext.error(0);

		}

	}
}