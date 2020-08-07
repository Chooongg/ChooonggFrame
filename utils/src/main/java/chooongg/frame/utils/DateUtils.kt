package chooongg.frame.utils

import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object DateUtils {
    /**
     * 日期类型 *
     */
    const val yyyyMMDD = "yyyy-MM-dd"
    const val yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"
    const val HHmmss = "HH:mm:ss"
    const val LOCALE_DATE_FORMAT = "yyyy年M月d日 HH:mm:ss"
    const val DB_DATA_FORMAT = "yyyy-MM-DD HH:mm:ss"
    const val NEWS_ITEM_DATE_FORMAT = "hh:mm M月d日 yyyy"

    @Throws(Exception::class)
    fun dateToString(date: Date, pattern: String): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
    }

    @Throws(Exception::class)
    fun stringToDate(dateStr: String, pattern: String): Date? {
        return SimpleDateFormat(pattern, Locale.getDefault()).parse(dateStr)
    }

    /**
     * 将Date类型转换为日期字符串
     *
     * @param date Date对象
     * @param type 需要的日期格式
     * @return 按照需求格式的日期字符串
     */
    fun formatDate(date: Date, type: String): String? {
        try {
            val df = SimpleDateFormat(type, Locale.getDefault())
            return df.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 将日期字符串转换为Date类型
     *
     * @param dateStr 日期字符串
     * @param type    日期字符串格式
     * @return Date对象
     */
    fun parseDate(dateStr: String, type: String): Date? {
        val df = SimpleDateFormat(type, Locale.getDefault())
        var date: Date? = null
        try {
            date = df.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    /**
     * 得到年
     *
     * @param date Date对象
     * @return 年
     */
    fun getYear(date: Date): Int {
        val c: Calendar = Calendar.getInstance()
        c.time = date
        return c.get(Calendar.YEAR)
    }

    /**
     * 得到月
     *
     * @param date Date对象
     * @return 月
     */
    fun getMonth(date: Date): Int {
        val c: Calendar = Calendar.getInstance()
        c.time = date
        return c.get(Calendar.MONTH) + 1
    }

    /**
     * 得到日
     *
     * @param date Date对象
     * @return 日
     */
    fun getDay(date: Date): Int {
        val c: Calendar = Calendar.getInstance()
        c.time = date
        return c.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 转换日期 将日期转为今天, 昨天, 前天, XXXX-XX-XX, ...
     *
     * @param time 时间
     * @return 当前日期转换为更容易理解的方式
     */
    fun translateDate(time: Long): String {
        val oneDay = 24 * 60 * 60 * 1000.toLong()
        val current: Calendar = Calendar.getInstance()
        val today: Calendar = Calendar.getInstance() //今天
        today.set(Calendar.YEAR, current.get(Calendar.YEAR))
        today.set(Calendar.MONTH, current.get(Calendar.MONTH))
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH))
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        val todayStartTime: Long = today.timeInMillis
        return if (time >= todayStartTime && time < todayStartTime + oneDay) { // today
            "今天"
        } else if (time >= todayStartTime - oneDay && time < todayStartTime) { // yesterday
            "昨天"
        } else if (time >= todayStartTime - oneDay * 2 && time < todayStartTime - oneDay) { // the day before yesterday
            "前天"
        } else if (time > todayStartTime + oneDay) { // future
            "将来某一天"
        } else {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = Date(time)
            dateFormat.format(date)
        }
    }

    /**
     * 转换日期 转换为更为人性化的时间
     *
     * @param time 时间
     * @return
     */
    private fun translateDate(time: Long, curTime: Long): String {
        val oneDay = 24 * 60 * 60.toLong()
        val today: Calendar = Calendar.getInstance() //今天
        today.timeInMillis = curTime * 1000
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        val todayStartTime: Long = today.timeInMillis / 1000
        return if (time >= todayStartTime) {
            val d = curTime - time
            when {
                d <= 60 -> "1分钟前"
                d <= 60 * 60 -> {
                    var m = d / 60
                    if (m <= 0) {
                        m = 1
                    }
                    m.toString() + "分钟前"
                }
                else -> {
                    val dateFormat = SimpleDateFormat("今天 HH:mm", Locale.getDefault())
                    val date = Date(time * 1000)
                    var dateStr: String = dateFormat.format(date)
                    if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                        dateStr = dateStr.replace(" 0", " ")
                    }
                    dateStr
                }
            }
        } else {
            if (time < todayStartTime && time > todayStartTime - oneDay) {
                val dateFormat = SimpleDateFormat("昨天 HH:mm", Locale.getDefault())
                val date = Date(time * 1000)
                var dateStr: String = dateFormat.format(date)
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ")
                }
                dateStr
            } else if (time < todayStartTime - oneDay && time > todayStartTime - 2 * oneDay) {
                val dateFormat = SimpleDateFormat("前天 HH:mm", Locale.getDefault())
                val date = Date(time * 1000)
                var dateStr: String = dateFormat.format(date)
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ")
                }
                dateStr
            } else {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val date = Date(time * 1000)
                var dateStr: String = dateFormat.format(date)
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ")
                }
                dateStr
            }
        }
    }
}