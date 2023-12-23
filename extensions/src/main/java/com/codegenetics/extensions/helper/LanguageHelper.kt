package com.codegenetics.extensions.helper

import com.codegenetics.extensions.models.Language
import java.util.Locale

object LanguageHelper {
    /** @return language code in string
     * e.g en, eu*/
    fun getDefaultLanguage(): String {
        return Locale.getDefault().language // This will return a string like "en" for English, "es" for Spanish, etc.
    }

    /** @return language name in string
     * e.g English,Arabic
     * @param languageCode e.g eu,en*/
    fun getLanguageDisplayName(languageCode: String): String {
        val locale = Locale(languageCode)
        return locale.getDisplayLanguage(Locale.getDefault())
    }

    fun getAllLanguagesMap(): Map<String, String> {
        val languageMap = mutableMapOf<String, String>()
        val locales = Locale.getAvailableLocales()

        for (locale in locales) {
            val languageCode = locale.language
            val displayName = locale.getDisplayLanguage(Locale.ENGLISH)
            if (displayName.isNotEmpty() && languageCode.isNotEmpty()) {
                languageMap[languageCode] = displayName
            }
        }

        return languageMap
    }

    fun getAllLanguagesList(): List<Language> {
        val languageList: MutableList<Language> = mutableListOf()
        val locales = Locale.getAvailableLocales()

        for (locale in locales) {
            val languageCode = locale.language
            val displayName = locale.getDisplayLanguage(Locale.ENGLISH)
            if (displayName.isNotEmpty() && languageCode.isNotEmpty()) {
                languageList.add(Language(englishName = displayName, code = languageCode))

            }
        }

        return languageList
    }

    fun getSupportedLanguages(): List<Language> {
        return listOf(
            Language("Afrikaans", "Afrikaans", "af"),
            Language("Albanian", "Shqip", "sq"),
            Language("Amharic", "አማርኛ", "am"),
            Language("Arabic", "العربية", "ar"),
            Language("Armenian", "Հայերեն", "hy"),
            Language("Azerbaijani", "Azərbaycan", "az"),
            Language("Basque", "Euskara", "eu"),
            Language("Belarusian", "Беларуская", "be"),
            Language("Bengali", "বাংলা", "bn"),
            Language("Bosnian", "Bosanski", "bs"),
            Language("Bulgarian", "Български", "bg"),
            Language("Catalan", "Català", "ca"),
            Language("Cebuano", "Cebuano", "ceb"),
            Language("Chichewa", "Chichewa", "ny"),
            Language("Chinese (Simplified)", "简体中文", "zh-CN"),
            Language("Chinese (Traditional)", "繁體中文", "zh-TW"),
            Language("Corsican", "Corsu", "co"),
            Language("Croatian", "Hrvatski", "hr"),
            Language("Czech", "Čeština", "cs"),
            Language("Danish", "Dansk", "da"),
            Language("Dutch", "Nederlands", "nl"),
            Language("English", "English", "en"),
            Language("Esperanto", "Esperanto", "eo"),
            Language("Estonian", "Eesti", "et"),
            Language("Filipino", "Filipino", "fil"),
            Language("Finnish", "Suomi", "fi"),
            Language("French", "Français", "fr"),
            Language("Frisian", "Frysk", "fy"),
            Language("Galician", "Galego", "gl"),
            Language("Georgian", "ქართული", "ka"),
            Language("German", "Deutsch", "de"),
            Language("Greek", "Ελληνικά", "el"),
            Language("Gujarati", "ગુજરાતી", "gu"),
            Language("Haitian Creole", "Kreyòl Ayisyen", "ht"),
            Language("Hausa", "Hausa", "ha"),
            Language("Hawaiian", "ʻŌlelo Hawaiʻi", "haw"),
            Language("Hebrew", "עברית", "iw"),
            Language("Hindi", "हिंदी", "hi"),
            Language("Hmong", "Hmoob", "hmn"),
            Language("Hungarian", "Magyar", "hu"),
            Language("Icelandic", "Íslenska", "is"),
            Language("Igbo", "Igbo", "ig"),
            Language("Indonesian", "Indonesia", "id"),
            Language("Irish", "Gaeilge", "ga"),
            Language("Italian", "Italiano", "it"),
            Language("Japanese", "日本語", "ja"),
            Language("Javanese", "Basa Jawa", "jw"),
            Language("Kannada", "ಕನ್ನಡ", "kn"),
            Language("Kazakh", "Қазақша", "kk"),
            Language("Khmer", "ភាសាខ្មែរ", "km"),
            Language("Korean", "한국어", "ko"),
            Language("Kurdish (Kurmanji)", "Kurmancî", "ku"),
            Language("Kyrgyz", "Кыргызча", "ky"),
            Language("Lao", "ລາວ", "lo"),
            Language("Latin", "Latina", "la"),
            Language("Latvian", "Latviešu", "lv"),
            Language("Lithuanian", "Lietuvių", "lt"),
            Language("Luxembourgish", "Lëtzebuergesch", "lb"),
            Language("Macedonian", "Македонски", "mk"),
            Language("Malagasy", "Malagasy", "mg"),
            Language("Malay", "Bahasa Melayu", "ms"),
            Language("Malayalam", "മലയാളം", "ml"),
            Language("Maltese", "Malti", "mt"),
            Language("Maori", "Te Reo Māori", "mi"),
            Language("Marathi", "मराठी", "mr"),
            Language("Mongolian", "Монгол", "mn"),
            Language("Myanmar (Burmese)", "မြန်မာ", "my"),
            Language("Nepali", "नेपाली", "ne"),
            Language("Norwegian", "Norsk", "no"),
            Language("Odia", "ଓଡ଼ିଆ", "or"),
            Language("Pashto", "پښتو", "ps"),
            Language("Persian", "فارسی", "fa"),
            Language("Polish", "Polski", "pl"),
            Language("Portuguese", "Português", "pt"),
            Language("Punjabi", "ਪੰਜਾਬੀ", "pa"),
            Language("Romanian", "Română", "ro"),
            Language("Russian", "Русский", "ru"),
            Language("Samoan", "Gagana Samoa", "sm"),
            Language("Scots Gaelic", "Gàidhlig", "gd"),
            Language("Serbian", "Српски", "sr"),
            Language("Sesotho", "Sesotho", "st"),
            Language("Shona", "Shona", "sn"),
            Language("Sindhi", "سنڌي", "sd"),
            Language("Sinhala", "සිංහල", "si"),
            Language("Slovak", "Slovenčina", "sk"),
            Language("Slovenian", "Slovenščina", "sl"),
            Language("Somali", "Soomaali", "so"),
            Language("Spanish", "Español", "es"),
            Language("Sundanese", "Basa Sunda", "su"),
            Language("Swahili", "Kiswahili", "sw"),
            Language("Swedish", "Svenska", "sv"),
            Language("Tajik", "Тоҷикӣ", "tg"),
            Language("Tamil", "தமிழ்", "ta"),
            Language("Telugu", "తెలుగు", "te"),
            Language("Thai", "ไทย", "th"),
            Language("Turkish", "Türkçe", "tr"),
            Language("Ukrainian", "Українська", "uk"),
            Language("Urdu", "اردو", "ur"),
            Language("Uzbek", "Oʻzbek", "uz"),
            Language("Vietnamese", "Tiếng Việt", "vi"),
            Language("Welsh", "Cymraeg", "cy"),
            Language("Xhosa", "IsiXhosa", "xh"),
            Language("Yiddish", "ייִדיש", "yi"),
            Language("Yoruba", "Yorùbá", "yo"),
            Language("Zulu", "IsiZulu", "zu"),
        )
    }


}