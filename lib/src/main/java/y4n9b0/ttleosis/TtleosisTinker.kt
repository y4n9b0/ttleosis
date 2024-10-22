package y4n9b0.ttleosis

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.util.Log
import java.util.UUID
import kotlin.collections.set

object TtleosisTinker : ActivityLifecycleCallbacks by noOpDelegate() {

    internal const val TAG = "ttleosis"

    var debug: Boolean? = null

    private val map = mutableMapOf<String, Bundle>()

    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        super.onActivityPostSaveInstanceState(activity, outState)
        if (debug!!) {
            Log.d(TAG, "onActivityPostSaveInstanceState activity=$activity")
            outState.log()
        }
        val uuid = UUID.randomUUID().toString()
        save(uuid, outState)
        outState.clear()
        outState.putString(activity.javaClass.name, uuid)
    }

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        savedInstanceState?.getString(activity.javaClass.name)?.let(::restore)?.apply {
            savedInstanceState.clear()
            savedInstanceState.putAll(this)
        }
        super.onActivityPreCreated(activity, savedInstanceState)
    }

    fun register(app: Application) {
        debug = debug ?: isDebuggable(app)
        app.registerActivityLifecycleCallbacks(this)
    }

    private fun save(id: String, bundle: Bundle) {
        map[id] = bundle.clone() as Bundle
    }

    private fun restore(id: String): Bundle? {
        return map.remove(id)
    }

    private fun isDebuggable(context: Context): Boolean {
        return context.applicationInfo.flags.and(ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }
}