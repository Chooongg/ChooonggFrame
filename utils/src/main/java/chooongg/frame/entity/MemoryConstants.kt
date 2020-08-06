package chooongg.frame.entity

import androidx.annotation.IntDef

/**
 * 内存常量
 */
object MemoryConstants {

    const val BYTE = 1
    const val KB = 1024
    const val MB = 1048576
    const val GB = 1073741824

    @IntDef(BYTE, KB, MB, GB)
    annotation class Unit
}