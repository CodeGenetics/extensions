package com.codegenetics.extensions.extension

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject



/**
 * Safely retrieves a [String] value from the [JSONObject] for the given key.
 * Returns an empty string if the key does not exist or an error occurs.
 *
 * @param key The key to retrieve the value for.
 * @return The [String] value associated with the key, or an empty string if not found.
 */
fun JSONObject.getStringIfExist(key: String): String {
    return try {
        if (has(key)) getString(key) else ""
    } catch (e: JSONException) {
        e.printStackTrace()
        ""
    }
}

/**
 * Safely retrieves a [Boolean] value from the [JSONObject] for the given key.
 * Returns `false` if the key does not exist or an error occurs.
 *
 * @param key The key to retrieve the value for.
 * @return The [Boolean] value associated with the key, or `false` if not found.
 */
fun JSONObject.getBooleanIfExist(key: String): Boolean {
    return try {
        if (has(key)) getBoolean(key) else false
    } catch (e: JSONException) {
        e.printStackTrace()
        false
    }
}

/**
 * Safely retrieves a [Double] value from the [JSONObject] for the given key.
 * Returns `0.0` if the key does not exist or an error occurs.
 *
 * @param key The key to retrieve the value for.
 * @return The [Double] value associated with the key, or `0.0` if not found.
 */
fun JSONObject.getDoubleIfExist(key: String): Double {
    return try {
        if (has(key)) getDouble(key) else 0.0
    } catch (e: JSONException) {
        e.printStackTrace()
        0.0
    }
}

/**
 * Safely retrieves an [Int] value from the [JSONObject] for the given key.
 * Returns `0` if the key does not exist or an error occurs.
 *
 * @param key The key to retrieve the value for.
 * @return The [Int] value associated with the key, or `0` if not found.
 */
fun JSONObject.getIntIfExist(key: String): Int {
    return try {
        if (has(key)) getInt(key) else 0
    } catch (e: JSONException) {
        e.printStackTrace()
        0
    }
}

/**
 * Safely retrieves a nested [JSONObject] from the current [JSONObject] for the given key.
 * Returns an empty [JSONObject] if the key does not exist or an error occurs.
 *
 * @param key The key to retrieve the value for.
 * @return The [JSONObject] associated with the key, or an empty [JSONObject] if not found.
 */
fun JSONObject.getObjectIfExist(key: String): JSONObject {
    return try {
        if (has(key)) getJSONObject(key) else JSONObject()
    } catch (e: JSONException) {
        e.printStackTrace()
        JSONObject()
    }
}

/**
 * Safely retrieves a [JSONArray] from the [JSONObject] for the given key.
 * Returns an empty [JSONArray] if the key does not exist or an error occurs.
 *
 * @param key The key to retrieve the value for.
 * @return The [JSONArray] associated with the key, or an empty [JSONArray] if not found.
 */
fun JSONObject.getArrayIfExist(key: String): JSONArray {
    return try {
        if (has(key)) getJSONArray(key) else JSONArray()
    } catch (e: JSONException) {
        e.printStackTrace()
        JSONArray()
    }
}