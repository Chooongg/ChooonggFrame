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
        .showThreadInfo(true)

    private fun getDefaultLogAdapter() = object : AndroidLogAdapter(formatStrategy) {
        override fun isLoggable(priority: Int, tag: String?) = ChooonggMMKVConst.IsLogEnable.get
    }
}

fun logD(message: String, vararg anys: Any?) = Logger.d(message, anys)

fun logD(vararg anys: Any?) = Logger.d(anys)


