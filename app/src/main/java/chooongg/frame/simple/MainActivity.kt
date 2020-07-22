package chooongg.frame.simple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import chooongg.frame.log.L

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        L.e("测试打印")
    }
}