package com.codegenetics.extensions.extension

/**
 * Retrieves the value associated with the given [key] in the map, if it exists.
 *
 * @param key The key to look for in the map.
 * @return The value associated with the key, or `null` if the key does not exist.
 *
 * ### Usage Example:
 * ```kotlin
 * val map = mapOf(1 to "One", 2 to "Two")
 * println(map.getIfExists(1)) // Output: "One"
 * println(map.getIfExists(3)) // Output: null
 * ```
 */
fun <K, V> Map<K, V>.getIfExists(key: K): V? {
    return this[key]
}

/**
 * Retrieves the value associated with the given [key] in a map of type [Map<String, String>].
 * If the key does not exist, returns an empty string.
 *
 * @param key The key to look for in the map.
 * @return The value associated with the key, or an empty string if the key does not exist.
 *
 * ### Usage Example:
 * ```kotlin
 * val map = mapOf("name" to "John", "age" to "30")
 * println(map.getIfExists("name")) // Output: "John"
 * println(map.getIfExists("city")) // Output: ""
 * ```
 */
fun Map<String, String>.getIfExists(key: String): String {
    return this[key] ?: ""
}

/**
 * Retrieves the value associated with the given [key] in the map, if it exists.
 * If the key does not exist, returns the provided [defaultValue].
 *
 * @param key The key to look for in the map.
 * @param defaultValue The default value to return if the key does not exist.
 * @return The value associated with the key, or the [defaultValue] if the key does not exist.
 *
 * ### Usage Example:
 * ```kotlin
 * val map = mapOf("color" to "blue", "shape" to "circle")
 * println(map.getIfExist("color", "unknown")) // Output: "blue"
 * println(map.getIfExist("size", "unknown")) // Output: "unknown"
 * ```
 */
fun <K, V> Map<K, V>.getIfExist(key: K, defaultValue: V): V {
    return this[key] ?: defaultValue
}

/**
 * Retrieves the value associated with the first key that starts with the specified [prefix].
 *
 * @param prefix The prefix to search for.
 * @return The value associated with the first matching key, or `null` if no match is found.
 *
 * ### Usage Example:
 * ```kotlin
 * val map = mapOf("prefix_key1" to "value1", "key2" to "value2")
 * println(map.getByKeyPrefix("prefix_")) // Output: "value1"
 * println(map.getByKeyPrefix("key3")) // Output: null
 * ```
 */
fun <K : String, V> Map<K, V>.getByKeyPrefix(prefix: String): V? {
    return this.entries.firstOrNull { it.key.startsWith(prefix) }?.value
}

/**
 * Finds all keys in the map that contain the specified [substring].
 *
 * @param substring The substring to search for.
 * @return A list of keys that contain the substring.
 *
 * ### Usage Example:
 * ```kotlin
 * val map = mapOf("hello_world" to 1, "world_hello" to 2, "test" to 3)
 * println(map.keysContaining("world")) // Output: ["hello_world", "world_hello"]
 * ```
 */
fun <K : String, V> Map<K, V>.keysContaining(substring: String): List<K> {
    return this.keys.filter { it.contains(substring) }
}

/**
 * Checks if any key in the map satisfies the given [predicate].
 *
 * @param predicate A lambda to test each key.
 * @return `true` if any key matches the condition, `false` otherwise.
 *
 * ### Usage Example:
 * ```kotlin
 * val map = mapOf("key1" to 1, "key2" to 2)
 * println(map.anyKeyMatches { it.startsWith("key") }) // Output: true
 * println(map.anyKeyMatches { it.startsWith("test") }) // Output: false
 * ```
 */
fun <K, V> Map<K, V>.anyKeyMatches(predicate: (K) -> Boolean): Boolean {
    return this.keys.any(predicate)
}

/**
 * Finds all keys in the map that have the specified [value].
 *
 * @param value The value to search for.
 * @return A list of keys associated with the value.
 *
 * ### Usage Example:
 * ```kotlin
 * val map = mapOf("a" to 1, "b" to 2, "c" to 1)
 * println(map.keysForValue(1)) // Output: ["a", "c"]
 * ```
 */
fun <K, V> Map<K, V>.keysForValue(value: V): List<K> {
    return this.filterValues { it == value }.keys.toList()
}


