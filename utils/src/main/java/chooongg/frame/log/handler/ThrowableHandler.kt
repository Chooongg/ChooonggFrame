package chooongg.frame.log.handler

import chooongg.frame.log.L
import chooongg.frame.log.bean.JSONConfig
import chooongg.frame.log.formatter.Formatter
import chooongg.frame.log.parser.Parser
import java.io.PrintWriter
import java.io.StringWriter

class ThrowableHandler : BaseHandler(), Parser<Throwable> {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {
        if (obj is Throwable) {
            jsonConfig.printers.map {
                val s = L.getMethodNames(it.formatter)
                it.printLog(
                    jsonConfig.logLevel,
                    jsonConfig.tag,
                    String.format(s, parseString(obj, it.formatter))
                )
            }
            return true
        }
        return false
    }

    override fun parseString(t: Throwable, formatter: Formatter): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        t.printStackTrace(pw)
        pw.flush()
        return sw.toString().replace("\n", "\n${formatter.spliter()}")
    }
}