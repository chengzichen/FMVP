package com.dhc.library.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils

import java.io.File

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/7/30 13:52
 * @description： app相关辅助类
 */
object AppUtil {


    /**
     * 获取应用程序名称
     *
     * @param context
     * @return
     */
    fun getAppName(context: Context): String? {

        val packageManager = context.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.resources.getString(labelRes)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获取应用程序版本名称信息
     *
     * @param context
     * @return 当前应用的版本名称
     */
    fun getVersionName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(
                    context.packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获取应用程序的版本Code信息
     *
     * @param context
     * @return 版本code
     */
    fun getVersionCode(context: Context): Int {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return 0
    }

    /**
     * 获取当前进程名
     * @param context
     * @return 进程名
     */
    fun getProcessName(context: Context): String {
        var processName: String? = null

        // ActivityManager
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        while (true) {
            for (info in am.runningAppProcesses) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName

                    break
                }
            }
            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName!!
            }
            // take a rest and again
            try {
                Thread.sleep(100L)
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }

        }
    }


    /**
     * 判断包是否安装
     * 邓浩宸
     *
     * @param context
     * @param packageName
     * @return
     */
    fun isInstalled(context: Context, packageName: String): Boolean {
        val manager = context.packageManager
        try {
            manager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)

            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }

    }

    /**
     * 安装应用程序
     *
     * @param context
     * @param apkFile
     */
    fun installApp(context: Context, apkFile: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
        context.startActivity(intent)
    }

    /**
     * 打开应用程序
     *
     * @param context
     * @param packageName
     */
    fun openApp(context: Context, packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        context.startActivity(intent)
    }


    private var isDebug: Boolean? = null

    fun isDebug(): Boolean {
        return if (isDebug == null) false else isDebug!!
    }

    /**
     * Sync lib debug with app's debug value. Should be called in module Application
     *
     * @param context
     */
    fun syncIsDebug(context: Context) {
        if (isDebug == null) {
            isDebug = context.applicationInfo != null && context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        }
    }

    /**
     * 开启权限设置页面
     * @param context
     */
    fun startAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        context.startActivity(intent)//this
    }
    /**
     * android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
     * at android.app.ContextImpl.startActivity(ContextImpl.java:672)
     * at android.app.ContextImpl.startActivity(ContextImpl.java:659)
     * at android.content.ContextWrapper.startActivity(ContextWrapper.java:331)
     * at com.kairu.library.utils.AppUtil.startAppSettings(AppUtil.java:181)
     * at com.kairu.tutudao.ui.home.MainActivity$1$1.onBtnClickR(MainActivity.java:125)
     * at com.kairu.library.rxpermissions.RxPermissions$5.onBtnClick(RxPermissions.java:306)
     * at com.flyco.dialog.widget.internal.BaseAlertDialog$2.onClick(BaseAlertDialog.java:162)
     * at android.view.View.performClick(View.java:5198)
     * at android.view.View$PerformClick.run(View.java:21147)
     * at android.os.Handler.handleCallback(Handler.java:739)
     * at android.os.Handler.dispatchMessage(Handler.java:95)
     * at android.os.Looper.loop(Looper.java:148)
     * at android.app.ActivityThread.main(ActivityThread.java:5417)
     * at java.lang.reflect.Method.invoke(Native Method)
     * at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:726)
     * at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:616)
     *
     */
}