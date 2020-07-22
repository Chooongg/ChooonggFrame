package chooongg.frame.log.formatter

import chooongg.frame.log.LoggerPrinter

object BorderFormatter : Formatter {

    override fun top() =
        LoggerPrinter.BR + LoggerPrinter.TOP_BORDER + LoggerPrinter.BR + LoggerPrinter.HORIZONTAL_DOUBLE_LINE

    override fun middle() = LoggerPrinter.BR + LoggerPrinter.MIDDLE_BORDER + LoggerPrinter.BR

    override fun bottom() = LoggerPrinter.BR + LoggerPrinter.BOTTOM_BORDER + LoggerPrinter.BR

    override fun spliter() = LoggerPrinter.HORIZONTAL_DOUBLE_LINE
}