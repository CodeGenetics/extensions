package com.codegenetics.extensions.extension


    /**
     * Concatenates the name of the Enum receiver with an underscore and the eventKeys.
     * If eventKeys is an Enum, uses its name; otherwise, uses its string representation.
     *
     * @param T The type of eventKeys, which can be an Enum or a String.
     * @return A concatenated string in the format "ReceiverName_eventKeys".
     */
    infix fun <E : Enum<E>, T> E?.underscore(eventKeys: T?): String {
        val receiverStr = this?.name ?: "UNKNOWN"
        val eventKeysStr = when (eventKeys) {
            is Enum<*> -> eventKeys.name
            is String -> eventKeys
            null -> "NULL"
            else -> eventKeys.toString()
        }
        return "${receiverStr}_$eventKeysStr"
    }
    /**
     * Concatenates the String receiver with an underscore and the eventKeys Enum's name.
     *
     * @param E The type of the Enum.
     * @return A concatenated string in the format "Receiver_eventKeysName".
     */
    infix fun <E : Enum<E>> String.underscore(eventKeys: E): String {
        return "${this}_${eventKeys.name}"
    }
/**
 * Concatenates the String receiver with an underscore and the eventKeys.
 * If eventKeys is an Enum, uses its name; otherwise, uses its string representation.
 *
 * @param T The type of eventKeys, which can be an Enum or any other type.
 * @return A concatenated string in the format "Receiver_eventKeys".
 */
infix fun <T> String.underscore(eventKeys: T): String {
    val eventKeysStr = when (eventKeys) {
        is Enum<*> -> eventKeys.name
        is String -> eventKeys
        else -> eventKeys.toString()
    }
    return "${this}_$eventKeysStr"
}