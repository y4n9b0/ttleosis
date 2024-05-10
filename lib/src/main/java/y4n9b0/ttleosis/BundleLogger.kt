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
    val dataSize = parcel.dataSize()
    parcel.recycle()
    if (dataSize > 200.shl(10)) {
        Log.w(TAG, "${bundle.javaClass.simpleName} Bytes=$dataSize")
    } else {
        Log.d(TAG, "${bundle.javaClass.simpleName} Bytes=$dataSize")
    }
    val stack = Stack<BundleElement>()
    bundle.flat().reversed().forEach(stack::push)


    while (stack.isNotEmpty()) {
        val element = stack.pop()
        val key = element.key
        val value = element.value
        var name = value.javaClass.simpleName
        var prefix = ""
        var suffix = ""
        (value as? Parcelable)?.apply {
            val p = Parcel.obtain()
            value.writeToParcel(p, 0)
            val ds = p.dataSize()
            p.recycle()
            suffix = "Bytes=$ds"
        }

        when {
            value is Bundle -> {
                val indent = element.indent + if (element.isLast) "   " else "│  "
                value.flat(indent).reversed().forEach(stack::push)
            }

            value is SparseArray<*> -> {
                prefix = "[${value.size()}]"
                val indent = element.indent + if (element.isLast) "   " else "│  "
                value.flat(indent).reversed().forEach(stack::push)
            }

            androidx.customview.view.AbsSavedState::class.java.isAssignableFrom(value.javaClass)
                    or android.view.AbsSavedState::class.java.isAssignableFrom(value.javaClass)
            -> name = value.javaClass.name

            logArray(element) or logCollection(element) or logAndroidxFragmentManagerState(element) -> continue

            value.javaClass.name == "android.app.FragmentManagerState" -> {
                // todo
            }

            else -> {}
        }
        Log.d(TAG, "${element.indent + if (element.isLast) "└─ " else "├─ "}$name$prefix@$key $suffix")
    }
}

@Suppress("DEPRECATION")
private fun Bundle.flat(indent: String = ""): List<BundleElement> =
    keySet().mapNotNull { key ->
        get(key)?.let { value ->
            BundleElement(key, value, indent)
        }
    }.apply {
        lastOrNull()?.isLast = true
    }

private fun SparseArray<*>.flat(indent: String = ""): List<BundleElement> =
    List(size()) {
        BundleElement(keyAt(it).toString(), valueAt(it), indent)
    }.apply {
        lastOrNull()?.isLast = true
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
        if (size == 0) return logged
        val fmsIndent = element.indent + if (element.isLast) "└─ " else "├─ "
        val fmsName = fragmentManagerState.javaClass.simpleName
        Log.d(TAG, "$fmsIndent$fmsName@${element.key}")
        logged = true
        val fragmentStateClass = Class.forName("androidx.fragment.app.FragmentState")
        val nameField = fragmentStateClass.getDeclaredField("mClassName")
        val whoField = fragmentStateClass.getDeclaredField("mWho")
        nameField.isAccessible = true
        whoField.isAccessible = true
        activeFragmentStates.forEachIndexed { index, fragmentState ->
            val parcel = Parcel.obtain()
            (fragmentState as Parcelable).writeToParcel(parcel, 0)
            val dataSize = parcel.dataSize()
            parcel.recycle()
            val name = nameField.get(fragmentState) as String
            val who = whoField.get(fragmentState) as String
            val indent =
                element.indent + (if (element.isLast) "   " else "│  ") + (if (index == size - 1) "└─ " else "├─ ")
            Log.d(TAG, "$indent${fragmentState.javaClass.simpleName}@$name($who) Bytes=$dataSize")
        }
    } catch (e: Exception) {
        // ignore
    }
    return logged
}

private fun logArray(element: BundleElement): Boolean {
    val value = element.value
    if (value !is Array<*>) return false
    val key = element.key
    val name = value.javaClass.simpleName
    val size = value.size
    Log.d(TAG, "${element.indent + if (element.isLast) "└─ " else "├─ "}$name[$size]@$key")

    var suffix: String
    value.forEachIndexed { index, item ->
        val indent = element.indent + (if (element.isLast) "   " else "│  ") + (if (index == size - 1) "└─ " else "├─ ")
        suffix = ""
        (item as? Parcelable)?.apply {
            val p = Parcel.obtain()
            item.writeToParcel(p, 0)
            val ds = p.dataSize()
            p.recycle()
            suffix = "Bytes=$ds"
        }
        if (item != null) {
            Log.d(TAG, "$indent${item.javaClass.simpleName}=$item $suffix")
        } else {
            Log.d(TAG, "${indent}null")
        }
    }
    return true
}

private fun logCollection(element: BundleElement): Boolean {
    val value = element.value
    if (value !is Collection<*>) return false
    val key = element.key
    val name = value.javaClass.simpleName
    val size = value.size
    Log.d(TAG, "${element.indent + if (element.isLast) "└─ " else "├─ "}$name[$size]@$key")

    var suffix: String
    value.forEachIndexed { index, item ->
        val indent = element.indent + (if (element.isLast) "   " else "│  ") + (if (index == size - 1) "└─ " else "├─ ")
        suffix = ""
        (item as? Parcelable)?.apply {
            val p = Parcel.obtain()
            item.writeToParcel(p, 0)
            val ds = p.dataSize()
            p.recycle()
            suffix = "Bytes=$ds"
        }
        if (item != null) {
            Log.d(TAG, "$indent${item.javaClass.simpleName}=$item $suffix")
        } else {
            Log.d(TAG, "${indent}null")
        }
    }
    return true
}