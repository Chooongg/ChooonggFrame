package chooongg.frame.utils

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Process
import android.util.Log
import chooongg.frame.ChooonggFrame
import chooongg.frame.manager.APP
import kotlin.system.exitProcess

/**
 * 应用工具类
 */
object AppUtils {

    /**
     * 是否是Debug应用
     */
    @JvmOverloads
    fun isAppDebug(packageName: String? = APP.packageName): Boolean {
        if (packageName.isSpace()) return false
        val info = APP.applicationInfo
        return info != null && (info.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    /**
     * 启动应用意图
     */
    fun launchApp(packageName: String): Intent? {
        val launcherActivity = getLauncherActivity(packageName)
        if (launcherActivity.isSpace()) return null
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setClassName(packageName, launcherActivity!!)
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 重启应用
     */
    fun relaunchApp(isKillProcess: Boolean = false) {
        val intent = launchApp(APP.packageName)
        if (intent == null) {
            Log.e(ChooonggFrame.TAG, "不存在的启动应用")
            return
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        APP.startActivity(intent)
        if (!isKillProcess) return
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }

    data class AppInfo(
        val packageName: String,
        val name: CharSequence,
        val icon: Drawable,
        val packagePath: String,
        val versionName: String,
        val versionCode: Long,
        val isSystem: Boolean
    )

    fun getAppInfo(packageName: String = APP.packageName): AppInfo? {
        return try {
            val pm = APP.packageManager ?: return null
            val pi = pm.getPackageInfo(packageName, 0) ?: return null
            getBean(pm, pi)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    fun getAppIcon(packageName: String = APP.packageName): Drawable? {
        if (packageName.isSpace()) return null
        return try {
            val pm = APP.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.applicationInfo?.loadIcon(pm)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    fun getAppName(packageName: String = APP.packageName): CharSequence? {
        if (packageName.isSpace()) return null
        return try {
            val pm = APP.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.applicationInfo?.loadLabel(pm)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    fun getAppVersionName(packageName: String = APP.packageName): CharSequence? {
        if (packageName.isSpace()) return null
        return try {
            val pm = APP.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.applicationInfo?.loadLabel(pm)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    @Suppress("DEPRECATION")
    fun getAppVersionCode(packageName: String = APP.packageName): Long? {
        if (packageName.isSpace()) return null
        return try {
            val pm = APP.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pi?.longVersionCode
            } else {
                pi?.versionCode?.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    fun getAppUid(packageName: String = APP.packageName): Int? {
        return try {
            APP.packageManager.getApplicationInfo(packageName, 0).uid
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getLauncherActivity(packageName: String? = APP.packageName): String? {
        if (packageName.isSpace()) return null
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.`package` = packageName
        val info = APP.packageManager.queryIntentActivities(intent, 0)
        if (info.size == 0) return null
        return info[0].activityInfo.name
    }

    @Suppress("DEPRECATION")
    private fun getBean(pm: PackageManager, pi: PackageInfo?): AppInfo? {
        if (pi == null) return null
        val ai = pi.applicationInfo
        return AppInfo(
            pi.packageName,
            ai.loadLabel(pm),
            ai.loadIcon(pm),
            ai.sourceDir,
            pi.versionName,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) pi.longVersionCode else pi.versionCode.toLong(),
            (ApplicationInfo.FLAG_SYSTEM and ai.flags) != 0
        )
    }
}