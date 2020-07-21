package chooongg.frame.utils

import android.Manifest
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import chooongg.frame.manager.APP

object PhoneUtils {

    /**
     * 是否是手机
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun isPhone(): Boolean {
        return APP.telephonyManager.phoneType != TelephonyManager.PHONE_TYPE_NONE
    }

    /**
     * SIM卡是否可用
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun isSimCardReady(): Boolean {
        return APP.telephonyManager.simState == TelephonyManager.SIM_STATE_READY
    }

    /**
     * 获取SIM卡运营商名称
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getSimOperatorName() = APP.telephonyManager.simOperatorName

    /**
     * 获取SIM卡运营商名称
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getSimOperatorByMnc(): String? {
        val operator = APP.telephonyManager.simOperator ?: return ""
        return when (operator) {
            "46000", "46002", "46007", "46020" -> "中国移动"
            "46001", "46006", "46009" -> "中国联通"
            "46003", "46005", "46011" -> "中国电信"
            else -> operator
        }
    }
}