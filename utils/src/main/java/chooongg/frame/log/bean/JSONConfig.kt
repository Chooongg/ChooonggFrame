package chooongg.frame.log.bean

import androidx.annotation.Keep
import chooongg.frame.log.LogLevel
import chooongg.frame.log.printer.Printer

@Keep
data class JSONConfig(
    val logLevel: LogLevel = LogLevel.INFO,
    val tag: String,
    val printers: MutableSet<Printer>
)