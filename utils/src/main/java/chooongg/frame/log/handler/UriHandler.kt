package chooongg.frame.log.handler

import android.net.Uri
import chooongg.frame.log.L
import chooongg.frame.log.LoggerPrinter
import chooongg.frame.log.bean.JSONConfig
import chooongg.frame.log.formatJSON
import chooongg.frame.log.formatter.Formatter
import chooongg.frame.log.parser.Parser
import org.json.JSONObject

class UriHandler : BaseHandler(), Parser<Uri> {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {
        if (obj is Uri) {
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

    override fun parseString(t: Uri, formatter: Formatter): String {
        val msg = t.javaClass.toString() + LoggerPrinter.BR + formatter.spliter()
        return msg + JSONObject().apply {
            put("Scheme", t.scheme)
            put("Host", t.host)
            put("Port", t.port)
            put("Path", t.path)
            put("Query", t.query)
            put("Fragment", t.fragment)
        }.formatJSON().replace("\n", "\n${formatter.spliter()}")
    }
}