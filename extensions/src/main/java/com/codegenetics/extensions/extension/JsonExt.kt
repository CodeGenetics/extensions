package com.codegenetics.extensions.extension

import org.json.JSONArray
import org.json.JSONObject

fun JSONObject.getStringIfExist(key: String): String {
    try {
        return if (has(key)) getString(key)
        else ""
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun JSONObject.getBooleanIfExist(key: String): Boolean {
    try {
        return if (has(key)) getBoolean(key)
        else false
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

fun JSONObject.getDoubleIfExist(key: String): Double {
    try {
        return if (has(key)) getDouble(key)
        else 0.0
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0.0
}

fun JSONObject.getIntIfExist(key: String): Int {
    try {
        return if (has(key)) getInt(key)
        else 0
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}

fun JSONObject.getObjectIfExist(key: String): JSONObject {
    try {
        return if (has(key)) getJSONObject(key)
        else JSONObject()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return JSONObject()
}

fun JSONObject.getArrayIfExist(key: String): JSONArray {
    try {
        return if (has(key)) getJSONArray(key)
        else JSONArray()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return JSONArray()
}