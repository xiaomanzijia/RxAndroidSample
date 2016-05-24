package com.licheng.app.rxandroidsample.common;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 共通方法类
 * 
 * @author zhangGuanghua
 * 
 */
public class CommonUtil {

	/**
	 * User-Agent android:品牌|型号|子型号|操作系统|分辨率
	 * 
	 * @return user-agent
	 */
	public static String getUser_Agent(Activity activity) {
		String ua = getVendor() + "|" + getDevice() + "|" + "" + "|"
				+ getOSVersion() + "|" + getResolution(activity);
		return ua;
	}

	/**
	 * device factory name 获取 手机品牌
	 */
	public static String getVendor() {
		return Build.BRAND;
	}

	/**
	 * device model name 获取 手机型号
	 */
	public static String getDevice() {
		return Build.MODEL;
	}

	/**
	 * 获取 Android系统版本号
	 */
	public static String getOSVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取屏幕分辨率
	 */
	private static String getResolution(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		return width + "*" + height;
	}

	/**
	 * 获取APK版本号
	 * 
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int version = 0;
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			version = packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 获取APK版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {

		String versionName = null;
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			versionName = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		if (mobiles == null) {
			return false;
		}

		return isPhoneFormat(mobiles);
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isEmailAdress(String email) {
		// Pattern p = Pattern
		// .compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static boolean isPhoneFormat(String phoneNum) {
		if ("1".equals(phoneNum.subSequence(0, 1)) && phoneNum.length() == 11) {
			return true;
		} else {
			return false;
		}

		/*
		 * if(isMatch(phoneNum, Constants.CT_NUM) || isMatch(phoneNum,
		 * Constants.CM_NUM) || isMatch(phoneNum, Constants.CU_NUM)){ return
		 * true; }else{ return false; }
		 */
	}

	private static boolean isMatch(String inputValue, String regx) {
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(inputValue);
		return m.matches();
	}

	// 检查手机是否插入SIM卡
	public static boolean checkPhoneNet(Context con) {
		TelephonyManager mTelephonyManager = (TelephonyManager) con
				.getSystemService(Service.TELEPHONY_SERVICE);
		if (mTelephonyManager.getSimState() != TelephonyManager.SIM_STATE_READY) { // SIM卡没有就绪
			return false;
		} else {
			ConnectivityManager cManager = (ConnectivityManager) con
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				// 能联网
				return true;
			} else {
				// 不能联网
				return false;
			}
		}
	}

	/**
	 * 获取asset的json数据
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getData(Context context, String fileName) {
		String data = null;
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			in.close();
			data = new String(buffer, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;

	}

}
