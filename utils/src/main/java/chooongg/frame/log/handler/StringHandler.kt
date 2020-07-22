package chooongg.frame.log.handler

import chooongg.frame.log.L
import chooongg.frame.log.LogLevel
import chooongg.frame.log.bean.JSONConfig
import chooongg.frame.log.formatJSON
import chooongg.frame.log.formatter.Formatter
import chooongg.frame.log.parser.Parser
import chooongg.frame.log.printer.Printer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class StringHandler : BaseHandler(), Parser<String> {

    companion object {
        private const val MAX_STRING_LENGTH = 4000
    }

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {
        if (obj is String) {
            val json = obj.trim { it <= ' ' }
            jsonConfig.printers.map {
                val s = L.getMethodNames(it.formatter)
                val logString = String.format(s, parseString(json, it.formatter))
                printLongLog(jsonConfig.logLevel, jsonConfig.tag, logString, it)
            }
            return true
        }
        return false
    }

    /**
     * 打印超过 4000 行的日志
     */
    private fun printLongLog(logLevel: LogLevel, tag: String, logString: String, printer: Printer) {
        if (logString.length > MAX_STRING_LENGTH) {
            var i = 0
            while (i < logString.length) {
                if (i + MAX_STRING_LENGTH < logString.length) {
                    if (i == 0) {
                        printer.printLog(
                            logLevel,
                            tag,
                            logString.substring(i, i + MAX_STRING_LENGTH)
                        )
                    } else {
                        printer.printLog(
                            logLevel,
                            "",
                            logString.substring(i, i + MAX_STRING_LENGTH)
                        )
                    }
                } else
                    printer.printLog(logLevel, "", logString.substring(i, logString.length))
                i += MAX_STRING_LENGTH
            }
        } else printer.printLog(logLevel, tag, logString)
    }

    override fun parseString(t: String, formatter: Formatter): String {
        var message = ""
        try {
            message = when {
                t.startsWith("{") -> {
                    val jsonObject = JSONObject(t)
                    jsonObject.formatJSON().replace("\n", "\n${formatter.spliter()}")
                }
                t.startsWith("[") -> {
                    val jsonArray = JSONArray(t)
                    jsonArray.formatJSON().replace("\n", "\n${formatter.spliter()}")

                }
                else -> { // 普通的字符串
                    t.replace("\n", "\n${formatter.spliter()}")
                }
            }
        } catch (e: JSONException) {
            L.e("Invalid Json: $t")
        }
        return message
    }
}