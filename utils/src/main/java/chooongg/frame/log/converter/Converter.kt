package chooongg.frame.log.converter

import java.lang.reflect.Type

/**
 * 转换器
 */
interface Converter {

    /**
     * 将字符串转换成type类型的对象
     */
    fun <T> fromJson(json: String, type: Type): T

    /**
     * 将对象序列化成字符串对象
     */
    fun toJson(data: Any): String
}