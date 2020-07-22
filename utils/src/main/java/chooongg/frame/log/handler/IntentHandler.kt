package chooongg.frame.log.handler

import android.content.Intent
import android.os.Bundle
import chooongg.frame.log.L
import chooongg.frame.log.LoggerPrinter
import chooongg.frame.log.bean.JSONConfig
import chooongg.frame.log.formatJSON
import chooongg.frame.log.formatter.Formatter
import chooongg.frame.log.parseBundle
import chooongg.frame.log.parser.Parser
import org.json.JSONObject

class IntentHandler : BaseHandler(), Parser<Intent> {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {
        if (obj is Intent) {
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

    override fun parseString(t: Intent, formatter: Formatter): String {
        val msg = t.javaClass.toString() + LoggerPrinter.BR + formatter.spliter()
        return msg + JSONObject().apply {
            put("Scheme", t.scheme)
            put("Action", t.action)
            put("DataString", t.dataString)
            put("Type", t.type)
            put("Package", t.`package`)
            put("ComponentInfo", t.component)
            put("Categories", t.categories)
            t.extras?.let {
                put("Extras", JSONObject(parseBundleString(it)))
            }
        }.formatJSON().replace("\n", "\n${formatter.spliter()}")
    }

    private fun parseBundleString(extras: Bundle) = JSONObject().parseBundle(extras).formatJSON()
}