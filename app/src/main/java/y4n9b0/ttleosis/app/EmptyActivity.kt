package y4n9b0.ttleosis.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class EmptyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
        title = "Good choice 👍"
    }

    companion object {
        fun start(context: Context) = context.startActivity(Intent(context, EmptyActivity::class.java))
    }
}