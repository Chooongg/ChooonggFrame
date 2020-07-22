package chooongg.frame.log.handler

import chooongg.frame.log.L
import chooongg.frame.log.LoggerPrinter
import chooongg.frame.log.bean.JSONConfig
import chooongg.frame.log.formatJSON
import chooongg.frame.log.formatter.Formatter
import chooongg.frame.log.parseMap
import chooongg.frame.log.parser.Parser
import org.json.JSONObject

class MapHandler : BaseHandler(), Parser<Map<*, *>> {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {
        if (obj is Map<*, *>) {
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

    override fun parseString(t: Map<*, *>, formatter: Formatter): String {
        val msg = t.javaClass.toString() + LoggerPrinter.BR + formatter.spliter()
        return msg + JSONObject().parseMap(t).formatJSON()
            .replace("\n", "\n${formatter.spliter()}")
    }

}