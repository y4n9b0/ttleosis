package y4n9b0.ttleosis.app

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ly.genjidialog.extensions.newGenjiDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import y4n9b0.ttleosis.TtleosisTinker
import y4n9b0.ttleosis.app.databinding.ActivityTtleosisReproduceBinding

class TtleosisReproduceActivity : AppCompatActivity() {

    private var transactionDataMocked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityTtleosisReproduceBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        listOf(binding.ok, binding.notOk).forEach {
            it.setOnClickListener { view ->
                binding.ok.isEnabled = false
                binding.notOk.isEnabled = false
                application.unregisterActivityLifecycleCallbacks(TtleosisTinker)
                if (view.id == binding.ok.id) TtleosisTinker.register(application)
                lifecycleScope.launch {
                    mockTransactionData()
                    binding.ok.isEnabled = true
                    binding.notOk.isEnabled = true
                    EmptyActivity.start(this@TtleosisReproduceActivity)
                }
            }
        }
    }

    private suspend fun mockTransactionData() {
        if (transactionDataMocked) return
        // increase repeat time if TransactionTooLargeException not throw
        repeat(300) {
            val dialog = newGenjiDialog {
                dimAmount = 0f
                unLeak = true
            }.showOnWindow(supportFragmentManager)
            delay(40)
            dialog.dismiss()
        }
        transactionDataMocked = true
        delay(100)
    }
}