package chooongg.frame.log.handler

import chooongg.frame.log.*
import chooongg.frame.log.bean.JSONConfig
import chooongg.frame.log.formatter.Formatter
import chooongg.frame.log.parser.Parser
import org.json.JSONArray

class CollectionHandler : BaseHandler(), Parser<Collection<*>> {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {
        if (obj is Collection<*>) {
            val value = obj.firstOrNull()
            val isPrimitiveType = isPrimitiveType(value)
            if (isPrimitiveType) {
                val simpleName = obj.javaClass
                var msg = "%s size = %d" + LoggerPrinter.BR
                jsonConfig.printers.map {
                    msg = String.format(msg, simpleName, obj.size) + it.formatter.spliter()
                    val s = L.getMethodNames(it.formatter)
                    it.printLog(
                        jsonConfig.logLevel,
                        jsonConfig.tag,
                        String.format(s, msg + obj.toString())
                    )
                }
                return true
            }
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

    override fun parseString(t: Collection<*>, formatter: Formatter): String {
        val jsonArray = JSONArray()
        val simpleName = t.javaClass
        var msg = "%s size = %d" + LoggerPrinter.BR
        msg = String.format(msg, simpleName, t.size) + formatter.spliter()
        return msg + t.parseToJSONArray(jsonArray).formatJSON()
            .replace("\n", "\n${formatter.spliter()}")
    }
}