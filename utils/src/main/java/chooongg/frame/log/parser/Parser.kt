package chooongg.frame.log.parser

import chooongg.frame.log.formatter.Formatter

/**
 * 将对象解析成特定的字符串
 */
interface Parser<T> {

    /**
     * 将对象解析成字符串
     */
    fun parseString(t: T, formatter: Formatter): String
}