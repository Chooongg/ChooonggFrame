package chooongg.frame.entity

import chooongg.frame.utils.BuildConfig
import chooongg.frame.utils.MMKVKey

object ChooonggMMKVConst {

    object IsLogEnable : MMKVKey<Boolean>("IsLogEnable", BuildConfig.DEBUG)
}