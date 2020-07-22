package chooongg.frame.log.handler

import chooongg.frame.log.L
import chooongg.frame.log.LoggerPrinter
import chooongg.frame.log.bean.JSONConfig
import chooongg.frame.log.formatJSON
import chooongg.frame.log.formatter.Formatter
import chooongg.frame.log.isPrimitiveType
import chooongg.frame.log.parser.Parser
import org.json.JSONObject
import java.lang.ref.Reference

class ReferenceHandler : BaseHandler(), Parser<Reference<*>> {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {
        if (obj is Reference<*>) {
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

    override fun parseString(t: Reference<*>, formatter: Formatter): String {
        val actual = t.get()
        var msg =
            "${t.javaClass.canonicalName}<${actual?.javaClass.toString()}>${LoggerPrinter.BR}${formatter.spliter()}"
        val isPrimitiveType = isPrimitiveType(actual)
        msg += if (isPrimitiveType) "{$actual}" else {
            L.getConverter()?.takeIf { actual != null }?.let {
                JSONObject(it.toJson(actual!!)).formatJSON()
                    .replace("\n", "\n${formatter.spliter()}")
            } ?: ""
        }
        return msg
    }
}