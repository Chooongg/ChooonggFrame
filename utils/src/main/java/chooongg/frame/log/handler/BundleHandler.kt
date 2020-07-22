package chooongg.frame.log.handler

import android.os.Bundle
import chooongg.frame.log.L
import chooongg.frame.log.LoggerPrinter
import chooongg.frame.log.bean.JSONConfig
import chooongg.frame.log.formatJSON
import chooongg.frame.log.formatter.Formatter
import chooongg.frame.log.parseBundle
import chooongg.frame.log.parser.Parser
import org.json.JSONObject

/**
 * Created by tony on 2017/11/27.
 */
class BundleHandler : BaseHandler(), Parser<Bundle> {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {
        if (obj is Bundle) {
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

    override fun parseString(t: Bundle, formatter: Formatter): String {
        val msg = t.javaClass.toString() + LoggerPrinter.BR + formatter.spliter()
        return msg + JSONObject().parseBundle(t).formatJSON()
            .replace("\n", "\n${formatter.spliter()}")
    }
}