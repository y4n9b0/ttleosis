package y4n9b0.ttleosis

import android.app.Application
import android.content.Context
import androidx.startup.Initializer

class TtleosisStartupInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val application = context.applicationContext as Application
        TtleosisTinker.register(application)
    }

    override fun dependencies() = emptyList<Class<out Initializer<*>>>()
}
