package chooongg.frame.entity

import chooongg.frame.utils.MMKVKey
import chooongg.frame.utils.isDebug

object ChooonggMMKVConst {

    object IsLogEnable : MMKVKey<Boolean>("IsLogEnable", isDebug())
}