package chooongg.frame.manager

import chooongg.frame.ChooonggFrame
import chooongg.frame.entity.ChooonggMMKVConst
import com.orhanobut.logger.*

object LoggerManager {

    private var formatStrategy: FormatStrategy = getDefaultPrettyFormatBuilder().build()

    fun initialize() {
        changeLogAdapter()
    }

    fun isEnable() = ChooonggMMKVConst.IsLogEnable.get

    fun logEnable(enable: Boolean) {
        if (enable != ChooonggMMKVConst.IsLogEnable.get) {
            ChooonggMMKVConst.IsLogEnable.put(enable)
            changeLogAdapter()
        }
    }

    fun changeDefault() {
        this.formatStrategy = getDefaultPrettyFormatBuilder().build()
        changeLogAdapter()
    }

    fun changeFormatStrategy(formatStrategy: FormatStrategy) {
        this.formatStrategy = formatStrategy
        changeLogAdapter()
    }

    fun changeLogAdapter(logAdapter: LogAdapter = getDefaultLogAdapter()) {
        Logger.clearLogAdapters()
        Logger.addLogAdapter(logAdapter)
    }

    fun getDefaultPrettyFormatBuilder() = PrettyFormatStrategy.newBuilder()
        .tag(ChooonggFrame.TAG)
        .methodCount(1)
        .showThreadInfo(true)

    private fun getDefaultLogAdapter() = object : AndroidLogAdapter(formatStrategy) {
        override fun isLoggable(priority: Int, tag: String?) = ChooonggMMKVConst.IsLogEnable.get
    }
}

fun logTag(tag: String?) = Logger.t(tag)

fun log(priority: Int, tag: String?, message: String?, throwable: Throwable?) =
    Logger.log(priority, tag, message, throwable)

fun logD(message: String, vararg args: Any?) = Logger.d(message, *args)

fun logD(`object`: Any?) = Logger.d(`object`)

fun logE(message: String, vararg args: Any?) = Logger.e(message, *args)

fun logE(throwable: Throwable?, message: String, vararg args: Any?) =
    Logger.e(throwable, message, *args)

fun logI(message: String, vararg args: Any?) = Logger.i(message, *args)

fun logV(message: String, vararg args: Any?) = Logger.v(message, *args)

fun logW(message: String, vararg args: Any?) = Logger.w(message, *args)

fun logWtf(message: String, vararg args: Any?) = Logger.wtf(message, *args)

fun logJson(json: String?) = Logger.json(json)

fun logXml(xml: String?) = Logger.xml(xml)