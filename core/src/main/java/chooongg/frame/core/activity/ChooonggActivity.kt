package chooongg.frame.core.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import chooongg.frame.core.interfaces.Init
import chooongg.frame.log.L

abstract class ChooonggActivity : AppCompatActivity(), Init {

    var isCreated = false
        private set

    @Deprecated("使用使用init方法初始化")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(getContentLayout())
        } catch (e: Exception) {
            L.e("${javaClass.simpleName} setContentLayout operation there is an exception", e)
            return
        }
        try {
            initConfig(savedInstanceState)
            isCreated = true
        } catch (e: Exception) {
            L.e("${javaClass.simpleName} initConfig() there is an exception", e)
            return
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (isCreated.not()) return
        try {
            initContent(savedInstanceState)
        } catch (e: Exception) {
            L.e("${javaClass.simpleName} initContent() there is an exception", e)
            return
        }
    }

}