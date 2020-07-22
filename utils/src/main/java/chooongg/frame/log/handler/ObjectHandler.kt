package chooongg.frame.log.handler

import chooongg.frame.log.L
import chooongg.frame.log.LoggerPrinter
import chooongg.frame.log.bean.JSONConfig
import chooongg.frame.log.formatJSON
import org.json.JSONObject

class ObjectHandler : BaseHandler() {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {
        if (L.getConverter() != null) {
            jsonConfig.printers.map {
                val formatter = it.formatter
                val msg = obj.javaClass.toString() + LoggerPrinter.BR + formatter.spliter()
                val message = L.getConverter()!!.toJson(obj).run {
                    JSONObject(this)
                }.formatJSON().replace("\n", "\n${formatter.spliter()}")
                val s = L.getMethodNames(formatter)
                it.printLog(jsonConfig.logLevel, jsonConfig.tag, String.format(s, msg + message))
            }
            return true
        }
        return false
    }
}