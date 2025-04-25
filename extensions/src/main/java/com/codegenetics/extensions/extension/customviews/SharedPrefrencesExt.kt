package com.codegenetics.extensions.extension.customviews

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> SharedPreferences.putList(key: String, list: List<T>) {
    val gson = Gson()
    val json = gson.toJson(list)
    edit { putString(key, json) }
}

inline fun <reified T> SharedPreferences.getList(key: String): List<T> {
    val gson = Gson()
    val json = getString(key, null) ?: return emptyList()
    val type = object : TypeToken<List<T>>() {}.type
    return gson.fromJson(json, type)
}