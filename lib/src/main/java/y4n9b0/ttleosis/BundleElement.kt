package y4n9b0.ttleosis

data class BundleElement(
    val key: String,
    val value: Any,
) {
    // 日志缩进的前缀
    var indent: String = ""
    // 是否是所属 Bundle 的最后一个元素
    var isLast: Boolean = false
}