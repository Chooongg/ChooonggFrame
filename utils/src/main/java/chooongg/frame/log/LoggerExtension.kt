package chooongg.frame.log

import android.os.Bundle
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * 解析 collection ，并存储到 JSONArray
 */
fun Collection<*>.parseToJSONArray(jsonArray: JSONArray): JSONArray {
    this.map {
        it?.let {
            L.getConverter()?.toJson(it)?.run {
                try {
                    val jsonObject = JSONObject(this)
                    jsonArray.put(jsonObject)
                } catch (e: JSONException) {
                    L.e("Invalid Json")
                }
            }
        }
    }
    return jsonArray
}

fun JSONArray.formatJSON(): String = this.toString(LoggerPrinter.JSON_INDENT)

fun JSONObject.formatJSON(): String = this.toString(LoggerPrinter.JSON_INDENT)

/**
 * 解析 bundle ，并存储到 JSONObject
 */
fun JSONObject.parseBundle(bundle: Bundle): JSONObject {
    bundle.keySet().map {
        bundle.get(it)?.run {
            val isPrimitiveType = isPrimitiveType(this)
            try {
                if (isPrimitiveType) {
                    put(it, bundle.get(it))
                } else {
                    put(it, JSONObject(L.getConverter()?.toJson(this) ?: "{}"))
                }
            } catch (e: JSONException) {
                L.e("Invalid Json")
            }
        }
    }
    return this
}

/**
 * 解析 map ，并存储到 JSONObject
 */
fun JSONObject.parseMap(map: Map<*, *>): JSONObject {
    val keys = map.keys
    val values = map.values
    val value = values.firstOrNull()
    val isPrimitiveType = isPrimitiveType(value)
    keys.map {
        it?.let {
            try {
                if (isPrimitiveType) {
                    put(it.toString(), map[it])
                } else {
                    put(
                        it.toString(),
                        JSONObject(L.getConverter()?.toJson(map[it] ?: "{}") ?: "{}")
                    )
                }
            } catch (e: JSONException) {
                L.e("Invalid Json")
            }
        }
    }
    return this
}

/**
 * 判断 Any 是否为基本类型
 */
fun isPrimitiveType(value: Any?) = when (value) {
    is Boolean -> true
    is String -> true
    is Int -> true
    is Float -> true
    is Double -> true
    else -> false
}