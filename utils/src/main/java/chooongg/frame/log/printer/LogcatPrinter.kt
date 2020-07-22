package chooongg.frame.log.printer

import android.util.Log
import chooongg.frame.log.LogLevel
import chooongg.frame.log.formatter.BorderFormatter
import chooongg.frame.log.formatter.Formatter

/**
 * 打印到 Logcat 的 Printer
 */
class LogcatPrinter(override val formatter: Formatter = BorderFormatter) : Printer {

    override fun printLog(logLevel: LogLevel, tag: String, message: String) {
        when (logLevel) {
            LogLevel.ERROR -> Log.e(tag, message)
            LogLevel.WARN -> Log.w(tag, message)
            LogLevel.INFO -> Log.i(tag, message)
            LogLevel.DEBUG -> Log.d(tag, message)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as LogcatPrinter
        if (formatter != other.formatter) return false
        return true
    }

    override fun hashCode() = formatter.hashCode()
}