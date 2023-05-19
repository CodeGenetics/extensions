package com.codegenetics.extensions.extension

fun <K, V> Map<K, V>.getIfExists(key: K): V? {
    return this[key]
}

fun Map<String, String>.getIfExists(key: String): String {
    return if (this.containsKey(key)){
        this[key]!!
    }
    else ""
}

fun <K, V> Map<K, V>.getIfExist(key: K, defaultValue: V): V {
    return this[key] ?: defaultValue
}




