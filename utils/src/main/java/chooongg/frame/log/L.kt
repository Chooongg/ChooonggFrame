package chooongg.frame.log

import android.util.Log
import chooongg.frame.ChooonggFrame
import chooongg.frame.log.bean.JSONConfig
import chooongg.frame.log.converter.Converter
import chooongg.frame.log.formatter.BorderFormatter
import chooongg.frame.log.formatter.Formatter
import chooongg.frame.log.handler.*
import chooongg.frame.log.printer.LogcatPrinter
import chooongg.frame.log.printer.Printer
import java.util.*

object L {

    private var TAG = ChooonggFrame.TAG
    private var enabled = true
    private var header: String? = ""
    private val handlers = LinkedList<BaseHandler>()
    private var firstHandler: BaseHandler
    private val printers = mutableSetOf<Printer>()
    private var displayThreadInfo: Boolean = true
    private var displayClassInfo: Boolean = true
    private var converter: Converter? = null

    init {
        printers.add(LogcatPrinter()) // 默认添加 LogcatPrinter
        handlers.apply {
            add(StringHandler())
            add(CollectionHandler())
            add(MapHandler())
            add(BundleHandler())
            add(IntentHandler())
            add(UriHandler())
            add(ThrowableHandler())
            add(ReferenceHandler())
            add(ObjectHandler())
        }
        val len = handlers.size
        for (i in 0 until len) {
            if (i > 0) {
                handlers[i - 1].setNextHandler(handlers[i])
            }
        }
        firstHandler = handlers[0]
    }

    var logLevel = LogLevel.DEBUG // 日志的等级，可以进行配置，最好在 Application 中进行全局的配置

    /******************* L 的配置方法 Start *******************/

    fun init(clazz: Class<*>) = apply { TAG = clazz.simpleName }

    /**
     * 支持用户自己传 tag，可扩展性更好
     * @param tag
     */
    fun init(tag: String) = apply { TAG = tag }

    fun enabled(enable: Boolean) = apply { enabled = enable }

    /**
     * header 是 L 自定义的内容，可以放 App 的信息版本号等，方便查找和调试
     */
    fun header(header: String?) = apply { L.header = header }

    /**
     * 自定义 Handler 来解析 Object
     * 插入在ObjectHandler之前
     */
    fun addCustomerHandler(handler: BaseHandler) = addCustomerHandler(handler, handlers.size - 1)


    /**
     * 自定义 Handler 来解析 Object，并指定 Handler 的位置
     */
    fun addCustomerHandler(handler: BaseHandler, index: Int) = apply {
        handlers.add(index, handler)
        val len = handlers.size
        for (i in 0 until len) {
            if (i > 0) {
                handlers[i - 1].setNextHandler(handlers[i])
            }
        }
    }

    /**
     * 添加自定义的 Printer
     */
    fun addPrinter(printer: Printer) = apply { printers.add(printer) }

    /**
     * 删除 Printer
     */
    fun removePrinter(printer: Printer) = apply { printers.remove(printer) }

    /**
     * 是否打印线程信息
     */
    fun displayThreadInfo(displayThreadInfo: Boolean) = apply {
        this.displayThreadInfo = displayThreadInfo
    }

    /**
     * 是否打印类的信息
     */
    fun displayClassInfo(displayClassInfo: Boolean) = apply {
        this.displayClassInfo = displayClassInfo
    }

    /**
     * 用于解析 json 的 Converter
     */
    fun converter(converter: Converter) = apply { this.converter = converter }

    /******************* L 的配置方法 End *******************/

    /******************* L 提供打印的方法 Start *******************/

    fun e(msg: String?) = e(TAG, msg)

    fun e(tag: String?, msg: String?) = printLog(LogLevel.ERROR, tag, msg)

    fun e(msg: String?, tr: Throwable) = e(TAG, msg, tr)

    fun e(tag: String?, msg: String?, tr: Throwable) = printThrowable(LogLevel.ERROR, tag, msg, tr)

    fun w(msg: String?) = w(TAG, msg)

    fun w(tag: String?, msg: String?) = printLog(LogLevel.WARN, tag, msg)

    fun w(msg: String?, tr: Throwable) = w(TAG, msg, tr)

    fun w(tag: String?, msg: String?, tr: Throwable) = printThrowable(LogLevel.WARN, tag, msg, tr)

    fun i(msg: String?) = i(TAG, msg)

    fun i(tag: String?, msg: String?) = printLog(LogLevel.INFO, tag, msg)

    fun i(msg: String?, tr: Throwable) = i(TAG, msg, tr)

    fun i(tag: String?, msg: String?, tr: Throwable) = printThrowable(LogLevel.INFO, tag, msg, tr)

    fun d(msg: String?) = d(TAG, msg)

    fun d(tag: String?, msg: String?) = printLog(LogLevel.DEBUG, tag, msg)

    fun d(msg: String?, tr: Throwable) = d(TAG, msg, tr)

    fun d(tag: String?, msg: String?, tr: Throwable) = printThrowable(LogLevel.DEBUG, tag, msg, tr)

    /**
     * 使用特定的 printer 进行打印日志
     */
    fun print(logLevel: LogLevel, tag: String?, msg: String?, vararg printers: Printer) =
        printLog(logLevel, tag, msg, printers.toMutableSet())

    private fun printLog(
        logLevel: LogLevel,
        tag: String?,
        msg: String?,
        set: MutableSet<Printer> = printers
    ) {
        if (!enabled) return
        if (logLevel.value <= L.logLevel.value) {
            if (tag != null && tag.isNotEmpty() && msg != null && msg.isNotEmpty()) {
                set.map {
                    val s = getMethodNames(it.formatter)
                    if (msg.contains("\n")) {
                        it.printLog(
                            logLevel,
                            tag,
                            String.format(s, msg.replace("\n", "\n${it.formatter.spliter()}"))
                        )
                    } else {
                        it.printLog(logLevel, tag, String.format(s, msg))
                    }
                }
            }
        }
    }

    private fun printThrowable(logLevel: LogLevel, tag: String?, msg: String?, tr: Throwable) {
        if (!enabled) return
        if (logLevel.value <= L.logLevel.value) {
            if (tag != null && tag.isNotEmpty() && msg != null && msg.isNotEmpty()) {
                when (logLevel) {
                    LogLevel.ERROR -> Log.e(tag, msg, tr)
                    LogLevel.WARN -> Log.w(tag, msg, tr)
                    LogLevel.INFO -> Log.i(tag, msg, tr)
                    LogLevel.DEBUG -> Log.d(tag, msg, tr)
                }
            }
        }
    }

    /**
     * 将任何对象转换成json字符串进行打印
     */
    fun json(obj: Any?, jsonConfig: JSONConfig = JSONConfig(logLevel, TAG, printers)) {
        if (obj == null) {
            e("object is null")
            return
        }
        firstHandler.handleObject(obj, jsonConfig)
    }

    /******************* L 提供打印的方法 End *******************/

    fun getMethodNames(formatter: Formatter = BorderFormatter): String {
        val sElements = Thread.currentThread().stackTrace
        var stackOffset = LoggerPrinter.getStackOffset(sElements)
        stackOffset++
        return StringBuilder().apply {
            append("  ").append(formatter.top())
        }.apply {
            header?.takeIf { it.isNotEmpty() }?.let {
                // 添加 Header
                append("Header: $it").append(formatter.middle()).append(formatter.spliter())
            }
        }.apply {
            if (displayThreadInfo) {
                // 添加当前线程名
                append("Thread: ${Thread.currentThread().name}")
                    .append(formatter.middle())
                    .append(formatter.spliter())
            }
            if (displayClassInfo) {
                // 添加类名、方法名、行数
                append(sElements[stackOffset].className)
                    .append(".")
                    .append(sElements[stackOffset].methodName)
                    .append(" ")
                    .append("(")
                    .append(sElements[stackOffset].fileName)
                    .append(":")
                    .append(sElements[stackOffset].lineNumber)
                    .append(")")
                    .append(formatter.middle())
                    .append(formatter.spliter())
            }
            append("%s").append(formatter.bottom())
        }.toString()
    }

    fun printers() = printers

    fun getConverter() = converter
}