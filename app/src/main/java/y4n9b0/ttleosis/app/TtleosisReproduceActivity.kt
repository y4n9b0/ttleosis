package y4n9b0.ttleosis.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.ly.genjidialog.extensions.newGenjiDialog
import y4n9b0.ttleosis.app.databinding.ActivityTtleosisReproduceBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TtleosisReproduceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityTtleosisReproduceBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.reproduce.setOnClickListener(::reproduce)
    }

    private fun reproduce(view: View) {
        view.isVisible = false
        lifecycleScope.launch {
            // increase repeat time if TransactionTooLargeException not throw
            repeat(300) {
                val dialog = newGenjiDialog {
                    dimAmount = 0f
                    unLeak = true
                }.showOnWindow(supportFragmentManager)
                delay(40)
                dialog.dismiss()
            }

            delay(100)
            EmptyActivity.start(this@TtleosisReproduceActivity)
        }
    }
}