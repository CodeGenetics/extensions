package com.codegenetics.extensions.extension

inline fun <reified T : Any> T.simpleName(): String {
    return this::class.java.simpleName
}

inline fun <reified T : Any> T.name(): String {
    return this::class.java.name
}

inline fun <reified T : Any> T.tag(): String {
    return "~: ${this::class.java.name} :~"
}