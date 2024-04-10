package y4n9b0.ttleosis

import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.util.SparseArray
import y4n9b0.ttleosis.TtleosisTinker.TAG
import java.util.Stack

internal fun Bundle.log() {
    val bundle = clone() as Bundle
    val parcel = Parcel.obtain()
    bundle.writeToParcel(parcel, 0)
    val size = parcel.dataSize()
    parcel.recycle()
    if (size > 200.shl(10)) {
        Log.w(TAG, "${bundle.javaClass.simpleName} parcelSize=$size")
    } else {
        Log.d(TAG, "${bundle.javaClass.simpleName} parcelSize=$size")
    }
    val stack = Stack<BundleElement>()
    bundle.flat().reversed().forEachIndexed { index, element ->
        stack.push(
            element.apply {
                indent = ""
                isLast = index == 0
            }
        )
    }

    while (stack.isNotEmpty()) {
        val element = stack.pop()
        val key = element.key
        val value = element.value
        val name = value.javaClass.simpleName
        var suffix = ""
        when (value) {
            is Bundle -> {
                val p = Parcel.obtain()
                value.writeToParcel(p, 0)
                val s = p.dataSize()
                p.recycle()
                suffix = "parcelSize=$s"
                value.flat().reversed().forEachIndexed { i, e ->
                    stack.push(
                        e.apply {
                            indent = element.indent + if (element.isLast) "   " else "│  "
                            isLast = i == 0
                        }
                    )
                }
            }

            is Array<*> -> suffix = "size=${value.size}"
            is SparseArray<*> -> suffix = "size=${value.size()}"
            is List<*> -> suffix = "size=${value.size}"
            else -> {}
        }
        Log.d(TAG, "${element.indent + if (element.isLast) "└─ " else "├─ "}$name@$key $suffix")
    }
}

private fun Bundle.flat(): List<BundleElement> = keySet().mapNotNull { key ->
    get(key)?.let { value ->
        BundleElement(key, value)
    }
}