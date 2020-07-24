package chooongg.frame.log.printer

import android.util.Log
import chooongg.frame.log.LogLevel
import chooongg.frame.log.formatter.BorderFormatter
import chooongg.frame.log.formatter.Formatter

/**
 * 打印到 Logcat 的 Printer
 */
class LogcatPrinter(override val formatter: Formatter = BorderFormatter) : Printer {

    override fun printLog(logLevel: LogLevel, tag: CharSequence, message: CharSequence) {
        when (logLevel) {
            LogLevel.ERROR -> Log.e(tag.toString(), message.toString())
            LogLevel.WARN -> Log.w(tag.toString(), message.toString())
            LogLevel.INFO -> Log.i(tag.toString(), message.toString())
            LogLevel.DEBUG -> Log.d(tag.toString(), message.toString())
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