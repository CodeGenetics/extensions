package com.codegenetics.extensions.extension

/**
 * Retrieves the simple name of the class of the calling object.
 *
 * ### Usage Example:
 * ```kotlin
 * val className = someObject.simpleName()
 * println("Simple Name: $className")
 * ```
 *
 * @return The simple name of the class, or "Unknown" if not available.
 */
inline fun <reified T : Any> T.simpleName(): String {
    return T::class.simpleName ?: "Unknown"
}

/**
 * Retrieves the fully qualified name of the class of the calling object.
 *
 * ### Usage Example:
 * ```kotlin
 * val className = someObject.name()
 * println("Qualified Name: $className")
 * ```
 *
 * @return The fully qualified name of the class, or "Unknown" if not available.
 */
inline fun <reified T : Any> T.name(): String {
    return T::class.qualifiedName ?: "Unknown"
}

/**
 * Generates a tag string for the class of the calling object, useful for logging or debugging.
 *
 * ### Usage Example:
 * ```kotlin
 * val classTag = someObject.tag()
 * println("Class Tag: $classTag")
 * ```
 *
 * @return A formatted tag string containing the fully qualified class name.
 */
inline fun <reified T : Any> T.tag(): String {
    return "~: ${T::class.qualifiedName ?: "Unknown"} :~"
}