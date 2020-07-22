package chooongg.frame.log.formatter

/**
 * 格式化日志
 */
interface Formatter {
    fun top(): String
    fun middle(): String
    fun bottom(): String
    fun spliter(): String
}