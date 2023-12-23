package com.codegenetics.extensions.helper

import android.content.Context
import android.os.Build
import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Locale

@Keep
object LocaleHelper {
    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    private const val LANGUAGE_PREF = "language_pref"

    fun onAttach(context: Context): Context {
        return runBlocking {
            val lang = async(Dispatchers.IO) { getPersistedData(context, Locale.getDefault().language) }.await()
            context.setLocale(lang)
        }
    }

    fun getLanguage(context: Context): String {
        return runBlocking {
            async(Dispatchers.IO) { getPersistedData(context, Locale.getDefault().language) }.await()
        }
    }

    fun Context.setLocale(language: String): Context {
        persist(this, language)
        return updateResourcesLegacy(language)
    }

    private suspend fun getPersistedData(context: Context, defaultLanguage: String): String {
        return withContext(Dispatchers.IO) {
            val preferences = context.getSharedPreferences(LANGUAGE_PREF, Context.MODE_PRIVATE)
            preferences.getString(SELECTED_LANGUAGE, defaultLanguage) ?: "en"
        }
    }

    private fun persist(context: Context, language: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val preferences = context.getSharedPreferences(LANGUAGE_PREF, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString(SELECTED_LANGUAGE, language)
            editor.apply()
        }
    }

    private fun Context.updateResourcesLegacy(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) configuration.setLocale(locale)
        else configuration.locale = locale
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return this
    }
/*
    @TargetApi(Build.VERSION_CODES.N)
    private fun Context.updateResources(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return createConfigurationContext(configuration)
    }
*/

//    private fun Context.updateResourcesLegacy(language: String): Context {
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//        val resources = resources
//        val configuration = resources.configuration
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) configuration.setLocale(locale)
//        else configuration.locale = locale
//        configuration.setLayoutDirection(locale)
//        resources.updateConfiguration(configuration, resources.displayMetrics)
//        return this
//    }
}