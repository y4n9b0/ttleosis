package y4n9b0.ttleosis

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.util.SparseArray
import y4n9b0.ttleosis.TtleosisTinker.TAG
import java.util.ArrayList
import java.util.Stack

internal fun Bundle.log() {
    val bundle = clone() as Bundle
    val parcel = Parcel.obtain()
    parcel.writeBundle(bundle)
    val size = parcel.dataSize()
    parcel.recycle()
    if (size > 200.shl(10)) {
        Log.w(TAG, "${bundle.javaClass.simpleName} parcelSize=$size")
    } else {
        Log.d(TAG, "${bundle.javaClass.simpleName} parcelSize=$size")
    }
    val stack = Stack<BundleElement>()
    bundle.flat().reversed().forEach(stack::push)

    while (stack.isNotEmpty()) {
        val element = stack.pop()
        val key = element.key
        val value = element.value
        val name = value.javaClass.simpleName
        var suffix = ""
        when (value) {
            is Bundle -> {
                val p = Parcel.obtain()
                p.writeBundle(value)
                val s = p.dataSize()
                p.recycle()
                suffix = "parcelSize=$s"
                val indent = element.indent + if (element.isLast) "   " else "│  "
                value.flat(indent).reversed().forEach(stack::push)
            }

            is Array<*> -> suffix = "size=${value.size}"
            is SparseArray<*> -> suffix = "size=${value.size()}"
            is List<*> -> suffix = "size=${value.size}"
            else -> {
                if (logAndroidxFragmentManagerState(element)) {
                    continue
                } else if (value.javaClass.name == "android.app.FragmentManagerState") {
                    val p = Parcel.obtain()
                    (value as Parcelable).writeToParcel(p, 0)
                    val s = p.dataSize()
                    p.recycle()
                    suffix = "parcelSize=$s"
                }
            }
        }
        Log.d(TAG, "${element.indent + if (element.isLast) "└─ " else "├─ "}$name@$key $suffix")
    }
}

@Suppress("DEPRECATION")
private fun Bundle.flat(indent: String = ""): List<BundleElement> {
    return keySet().mapNotNull { key ->
        get(key)?.let { value ->
            BundleElement(key, value, indent)
        }
    }.apply {
        lastOrNull()?.isLast = true
    }
}

@Suppress("UNCHECKED_CAST")
private fun logAndroidxFragmentManagerState(element: BundleElement): Boolean {
    if (element.value.javaClass.name != "androidx.fragment.app.FragmentManagerState") return false
    var logged = false
    val fragmentManagerState = element.value
    try {
        val activeFragmentStates = fragmentManagerState.javaClass.getDeclaredField("mActive").let {
            it.isAccessible = true
            it.get(fragmentManagerState) as ArrayList<Any>
        }
        val size = activeFragmentStates.size
        if (size == 0) return false
        val fmsIndent = element.indent + if (element.isLast) "└─ " else "├─ "
        val fmsName = fragmentManagerState.javaClass.simpleName
        Log.d(TAG, "$fmsIndent$fmsName@${element.key} size=$size")
        logged = true
        val fragmentStateClass = Class.forName("androidx.fragment.app.FragmentState")
        val nameField = fragmentStateClass.getDeclaredField("mClassName")
        val whoField = fragmentStateClass.getDeclaredField("mWho")
        nameField.isAccessible = true
        whoField.isAccessible = true
        activeFragmentStates.forEachIndexed { index, fragmentState ->
            val parcel = Parcel.obtain()
            (fragmentState as Parcelable).writeToParcel(parcel, 0)
            val parcelSize = parcel.dataSize()
            parcel.recycle()
            val name = nameField.get(fragmentState) as String
            val who = whoField.get(fragmentState) as String
            val indent = element.indent + (if (element.isLast) "   " else "│  ") + (if (index == size - 1) "└─ " else "├─ ")
            Log.d(TAG, "$indent${fragmentState.javaClass.simpleName}@$name($who) parcelSize=$parcelSize")
        }
    } catch (e: Exception) {
        // ignore
    }
    return logged
}