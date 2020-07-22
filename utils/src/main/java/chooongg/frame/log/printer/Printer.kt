package chooongg.frame.log.printer

import chooongg.frame.log.LogLevel
import chooongg.frame.log.formatter.Formatter

/**
 * 打印日志
 */
interface Printer {
    val formatter: Formatter
    fun printLog(logLevel: LogLevel, tag: String, message: String)
}