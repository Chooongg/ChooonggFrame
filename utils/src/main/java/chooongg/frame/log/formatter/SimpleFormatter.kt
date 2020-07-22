package chooongg.frame.log.formatter

import chooongg.frame.log.LoggerPrinter

object SimpleFormatter : Formatter {

    override fun top() = LoggerPrinter.BR

    override fun middle() = LoggerPrinter.COMMA + LoggerPrinter.BLANK

    override fun bottom() = LoggerPrinter.BR

    override fun spliter() = ""
}