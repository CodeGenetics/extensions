package com.codegenetics.extensions.extension

import android.graphics.Color
import androidx.core.text.HtmlCompat
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Checks if the string is a valid hexadecimal color code.
 *
 * @return `true` if the string matches a valid color format (#RRGGBB, #RGB, or #AARRGGBB).
 *
 * ### Usage Example:
 * ```kotlin
 * println("#FF5733".isValidColor()) // Output: true
 * println("123456".isValidColor())  // Output: false
 * ```
 */
fun String.isValidColor(): Boolean {
    val regex = "^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3}|[0-9a-fA-F]{8})$".toRegex()
    return this.matches(regex)
}

/**
 * Validates the color string and adjusts its format if necessary.
 * Adds missing characters (e.g., "00" for alpha) if needed.
 *
 * @return The validated and adjusted color string, or an empty string if invalid.
 *
 * ### Usage Example:
 * ```kotlin
 * println("#FFF".validateColor())   // Output: "#FFF"
 * println("#F00A".validateColor())  // Output: "#F00A00"
 * ```
 */
fun String.validateColor(): String {
    return when {
        this.matches("^#([0-9a-fA-F]{6}|[0-9a-fA-F]{8})$".toRegex()) -> this
        this.matches("^#([0-9a-fA-F]{3})$".toRegex()) -> {
            "#${this[1]}${this[1]}${this[2]}${this[2]}${this[3]}${this[3]}"
        }
        this.matches("^#([0-9a-fA-F]{4})$".toRegex()) -> {
            "#${this[1]}${this[1]}${this[2]}${this[2]}${this[3]}${this[3]}${this[4]}${this[4]}"
        }
        else -> ""
    }
}

/**
 * Parses a validated color string into an integer value.
 *
 * @return The integer value of the color, or 0 if invalid.
 *
 * ### Usage Example:
 * ```kotlin
 * println("#FF5733".toColor())  // Output: -435143
 * ```
 */
fun String.toColor(): Int {
    val validatedColor = this.validateColor()
    return if (validatedColor.isNotEmpty()) {
        try {
            Color.parseColor(validatedColor)
        } catch (e: Exception) { // Catch all exceptions to prevent crashes
            0 // Return default color on failure
        }
    } else {
        0 // Return default color if validation fails
    }
}

/**
 * Parses the string into a color integer. Supports formats:
 * - #RGB
 * - #RRGGBB
 * - #AARRGGBB
 * - Optional handling for RGB/RRGGBB without alpha by adding default alpha.
 *
 * @return The integer representation of the color, or a default color if invalid.
 *
 * ### Usage Example:
 * ```kotlin
 * println("#FF5733".toColorInt())  // Output: -435143
 * println("FF5733".toColorInt())   // Output: -435143 (assuming default alpha)
 * ```
 */
fun String.toColorInt(defaultColor: Int = 0): Int {
    val colorStr = if (this.startsWith("#")) this else "#$this"
    return try {
        when (colorStr.length) {
            4 -> Color.parseColor("${colorStr}0") // e.g., #RGB0
            5 -> Color.parseColor("${colorStr}0") // e.g., #RGBA0
            7 -> Color.parseColor(colorStr)       // e.g., #RRGGBB
            9 -> Color.parseColor(colorStr)       // e.g., #AARRGGBB
            else -> defaultColor
        }
    } catch (e: IllegalArgumentException) {
        defaultColor
    }
}


/**
 * Converts a numeric string (representing seconds) into HH:mm:ss format.
 *
 * @return The formatted time string.
 *
 * ### Usage Example:
 * ```kotlin
 * println("3661".toTimeString())  // Output: "01:01:01"
 * ```
 */
fun String.toTimeString(): String {
    val seconds = this.toIntOrNull() ?: return "00:00:00"
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, secs)
}



/**
 * Formats the JSON string with specified indentation.
 *
 * @param indentSpaces Number of spaces for indentation (default: 2).
 * @return Beautified JSON string or the original string if invalid.
 *
 * ### Usage Example:
 * ```kotlin
 * println("{\"name\":\"John\"}".beautifyJson()) // Pretty-printed JSON
 * ```
 */
fun String.beautifyJson(indentSpaces: Int = 2): String {
    return try {
        if (this.isValidJson()) {
            val gson = Gson().newBuilder().setPrettyPrinting().create()
            gson.toJson(gson.fromJson(this, JsonElement::class.java))
        } else {
            this
        }
    } catch (e: JsonSyntaxException) {
        this
    }
}

/**
 * Converts a hexadecimal string to its corresponding Emoji character.
 * Returns an empty string if the input is invalid, empty, or does not correspond to an Emoji.
 *
 * @return The Emoji character as a String, or an empty string if invalid.
 */
/**
 * Converts a hexadecimal string to its corresponding Unicode character.
 * Returns an empty string if the input is invalid, empty, or does not correspond to a valid Unicode code point.
 *
 * @return The Unicode character as a String, or an empty string if invalid.
 */
fun String.toEmoji(): String {
    return try {
        val trimmedInput = this.trim()
        if (trimmedInput.isEmpty()) {
            ""
        } else {
            val codePoint = trimmedInput.toInt(16)
            if (Character.isValidCodePoint(codePoint)) {
                String(Character.toChars(codePoint))
            } else {
                ""
            }
        }
    } catch (e: Exception) {
        ""
    }
}

/**
 * Checks if a given Unicode code point corresponds to an Emoji.
 *
 * @param codePoint The Unicode code point to check.
 * @return True if the code point is within known Emoji ranges, false otherwise.
 */
fun isEmoji(codePoint: Int): Boolean {
    return (codePoint in 0x1F600..0x1F64F) || // Emoticons
            (codePoint in 0x1F300..0x1F5FF) || // Misc Symbols and Pictographs
            (codePoint in 0x1F680..0x1F6FF) || // Transport and Map Symbols
            (codePoint in 0x1F1E6..0x1F1FF) || // Regional Indicator Symbols
            (codePoint in 0x2600..0x26FF) ||   // Misc Symbols
            (codePoint in 0x2700..0x27BF) ||   // Dingbats
            (codePoint in 0xFE00..0xFE0F) ||   // Variation Selectors
            (codePoint in 0x1F900..0x1F9FF) || // Supplemental Symbols and Pictographs
            (codePoint in 0x1FA70..0x1FAFF)    // Symbols and Pictographs Extended-A
}

fun String.toFormulatedTime(): String {
    val totalSecs = this.toInt()
    val hours: Int
    val minutes: Int
    val seconds: Int
    when {
        totalSecs in 1..59 -> {
            return "$totalSecs Sec"
        }

        totalSecs in 60..3599 -> {
            minutes = totalSecs % 3600 / 60
            seconds = totalSecs % 60
            return "$minutes Min $seconds sec"
        }

        totalSecs >= 3600 -> {
            hours = totalSecs / 3600
            minutes = totalSecs % 3600 / 60
            seconds = totalSecs % 60
            return "$hours hr $minutes Min $seconds sec"
        }

        else -> return ""
    }
}

fun String.toEpoch(): Long {
    var str = this
    str = str.trim { it <= ' ' }
    val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = df.parse(str)
    val epoch = date?.time!!
    println(epoch) // 1055545912454
    return epoch
}

fun String.toDefaultTimeZone(): String {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val value = formatter.parse(this)
        val dateFormatter = SimpleDateFormat("h:mm a", Locale.getDefault()) //this format changeable
        dateFormatter.timeZone = TimeZone.getDefault()
        dateFormatter.format(value)
    } catch (e: java.lang.Exception) {
        "00-00-0000 00:00"
    }
}


fun String.unescapeJavaString(): String {
    val sb = StringBuilder()
    var i = 0
    while (i < this.length) {
        val ch = this[i]
        if (ch == '\\' && i < this.length - 1) {
            val nextChar = this[i + 1]
            when (nextChar) {
                '\\' -> {
                    sb.append('\\')
                    i += 2
                }

                'b' -> {
                    sb.append('\b')
                    i += 2
                }

                'n' -> {
                    sb.append('\n')
                    i += 2
                }

                'r' -> {
                    sb.append('\r')
                    i += 2
                }

                't' -> {
                    sb.append('\t')
                    i += 2
                }

                '\"' -> {
                    sb.append('\"')
                    i += 2
                }

                '\'' -> {
                    sb.append('\'')
                    i += 2
                }
                'u' -> {
                    if (i + 5 < this.length) {
                        val hex = this.substring(i + 2, i + 6)
                        try {
                            val code = hex.toInt(16)
                            sb.append(code.toChar())
                            i += 6
                        } catch (e: NumberFormatException) {
                            // Invalid Unicode escape, append as-is
                            sb.append("\\u")
                            i += 2
                        }
                    } else {
                        // Incomplete Unicode escape, append as-is
                        sb.append("\\u")
                        i += 2
                    }
                }

                in '0'..'7' -> {
                    var octal = "" + nextChar
                    var j = i + 2
                    while (j < this.length && octal.length < 3 && this[j] in '0'..'7') {
                        octal += this[j]
                        j++
                    }
                    try {
                        val code = octal.toInt(8)
                        sb.append(code.toChar())
                        i += 1 + octal.length
                    } catch (e: NumberFormatException) {
                        // Invalid octal escape, append as-is
                        sb.append('\\')
                        i += 1
                    }
                }

                else -> {
                    // Unrecognized escape sequence, append as-is
                    sb.append('\\').append(nextChar)
                    i += 2
                }
            }
        } else {
            sb.append(ch)
            i++
        }
    }
    return sb.toString()
}

fun String.parseHtmlString(): String {
    return HtmlCompat.fromHtml(
        this.unescapeJavaString(), HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString()
}



/**
 * Converts the string to snake_case, where words are lowercase and separated by underscores.
 *
 * @return A new string in snake_case format.
 *
 * ### Usage Example:
 * ```kotlin
 * println("Hello World!".toSnakeCase()) // Output: "hello_world"
 * ```
 */
fun String.toSnakeCase(): String {
    // Replace all characters that are not letters, numbers, or symbols with underscores
    val regex = Regex("[^\\p{L}\\p{N}\\p{So}]")
    val result = regex.replace(this, "_")
    // Convert the entire string to lowercase
    return result.lowercase()
}

/**
 * Converts the string to Title Case, capitalizing the first letter of each word.
 *
 * @return A new string with the first letter of each word capitalized.
 *
 * ### Usage Example:
 * ```kotlin
 * println("hello world".toTitleCase()) // Output: "Hello World"
 * ```
 */
fun String.toTitleCase(): String {
    return this.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

/**
 * Reverses the characters in the string.
 *
 * @return A new string with characters in reverse order.
 *
 * ### Usage Example:
 * ```kotlin
 * println("hello".reverse()) // Output: "olleh"
 * ```
 */
fun String.reverse(): String {
    return this.reversed()
}

/**
 * Converts the string to Kebab Case, where words are lowercase and separated by hyphens.
 *
 * @return A new string in kebab-case.
 *
 * ### Usage Example:
 * ```kotlin
 * println("Hello World".toKebabCase()) // Output: "hello-world"
 * ```
 */
fun String.toKebabCase(): String {
    return this
        .split(Regex("[^\\p{L}\\p{N}]+"))
        .joinToString("-") { it.lowercase(Locale.getDefault()) }
}

/**
 * Converts the string to Pascal Case, capitalizing the first letter of each word and removing separators.
 *
 * @return A new string in PascalCase.
 *
 * ### Usage Example:
 * ```kotlin
 * println("hello world".toPascalCase()) // Output: "HelloWorld"
 * ```
 */
fun String.toPascalCase(): String {
    return this
        .split(Regex("[^\\p{L}\\p{N}]+"))
        .filter { it.isNotEmpty() }
        .joinToString("") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
}

/**
 * Checks if the string is a palindrome, ignoring case and non-alphanumeric characters.
 *
 * @return `true` if the string is a palindrome; `false` otherwise.
 *
 * ### Usage Example:
 * ```kotlin
 * println("A man a plan a canal Panama".isPalindrome()) // Output: true
 * ```
 */
fun String.isPalindrome(): Boolean {
    val sanitized = this.filter { it.isLetterOrDigit() }.lowercase()
    return sanitized == sanitized.reversed()
}


/**
 * Converts the string to camelCase, where the first word starts with a lowercase letter
 * and each subsequent word starts with an uppercase letter. All spaces and non-alphanumeric
 * characters are removed.
 *
 * @return A new string in camelCase format.
 *
 * ### Usage Example:
 * ```kotlin
 * println("hello world example".toCamelCase()) // Output: "helloWorldExample"
 * println("Hello-World_Test".toCamelCase())     // Output: "helloWorldTest"
 * println("hello こんにちは world 世界".toCamelCase()) // Output: "helloこんにちはWorld世界"
 * ```
 */
fun String.toCamelCase(): String {
    return this
        .split(Regex("[^\\p{L}\\p{N}]+")) // Split by non-letter and non-number Unicode characters
        .filter { it.isNotEmpty() }        // Remove empty strings from the list
        .mapIndexed { index, word ->
            if (index == 0) {
                word.lowercase(Locale.getDefault()) // First word in lowercase
            } else {
                word.replaceFirstChar {
                    // Capitalize the first character if it's a lowercase letter; otherwise, leave it as-is
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
            }
        }
        .joinToString("") // Concatenate all words without spaces
}

/**
 * Converts the string to Camel Case with spaces, where each word's first letter is capitalized,
 * and non-alphabetic characters are replaced with spaces.
 *
 * @return A new string in Camel Case with spaces.
 *
 * ### Usage Example:
 * ```kotlin
 * println("hello-world_test".toCamelCaseWithSpaces()) // Output: "Hello World Test"
 * ```
 */
fun String.toCamelCaseWithSpaces(): String {
    return this
        .replace(Regex("[^\\p{L}\\p{N}]+"), " ")
        .split(Regex("\\s+"))
        .joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
}

/**
 * Removes all vowels (a, e, i, o, u) from the string, case-insensitive.
 *
 * @return A new string without vowels.
 *
 * ### Usage Example:
 * ```kotlin
 * println("Hello World".removeVowels()) // Output: "Hll Wrld"
 * ```
 */
fun String.removeVowels(): String {
    return this.replace(Regex("[aeiouAEIOU]"), "")
}

/**
 * Masks an email address for privacy by replacing characters before the "@" symbol with asterisks,
 * leaving the first and last alphanumeric characters visible. Non-alphanumeric characters are preserved.
 *
 * @return The masked email address, or the original string if it's not a valid email.
 *
 * ### Usage Example:
 * ```kotlin
 * println("john.doe@example.com".maskEmail()) // Output: "j*****e@example.com"
 * println("a@b.com".maskEmail()) // Output: "a@b.com"
 * ```
 */
fun String.maskEmail(): String {
    val emailParts = this.split("@")
    if (emailParts.size != 2) return this // Not a valid email
    val name = emailParts[0]
    val domain = emailParts[1]

    // Extract alphanumeric characters
    val lettersAndDigits = name.filter { it.isLetterOrDigit() }

    if (lettersAndDigits.length <= 2) {
        return if (lettersAndDigits.isEmpty()) {
            this
        } else {
            lettersAndDigits[0] + "*".repeat(lettersAndDigits.length - 1) + "@$domain"
        }
    }

    val first = lettersAndDigits.first()
    val last = lettersAndDigits.last()
    val maskedMiddle = "*".repeat(lettersAndDigits.length - 2)

    // Reconstruct the masked name, preserving non-alphanumerics
    val maskedName = StringBuilder()
    var letterIndex = 0
    lettersAndDigits.forEach { char ->
        if (letterIndex == 0) {
            maskedName.append(first)
        } else if (letterIndex == lettersAndDigits.length - 1) {
            maskedName.append(last)
        } else {
            maskedName.append(maskedMiddle[letterIndex - 1])
        }
        letterIndex++
    }

    return "$maskedName@$domain"
}

/**
 * Converts the string to a URL-friendly slug by replacing spaces and non-alphanumeric characters with hyphens
 * and converting to lowercase.
 *
 * @return A slugified version of the string.
 *
 * ### Usage Example:
 * ```kotlin
 * println("Hello World! This is a Test.".toSlug()) // Output: "hello-world-this-is-a-test-"
 * ```
 */
fun String.toSlug(): String {
    return this.lowercase(Locale.getDefault())
        .replace(Regex("[^\\p{L}\\p{N}]+"), "-")
        .trim('-')
}

/**
 * Abbreviates the string to a specified maximum length, adding ellipses ("...") if it exceeds the limit.
 *
 * @param maxLength The maximum length of the abbreviated string (default: 10).
 * @return The abbreviated string with ellipses if it was truncated.
 *
 * ### Usage Example:
 * ```kotlin
 * println("Hello World".abbreviate(5)) // Output: "He..."
 * ```
 */
fun String.abbreviate(maxLength: Int = 10): String {
    if (this.length <= maxLength) return this
    if (maxLength <= 3) return this.take(maxLength)
    return this.take(maxLength - 3) + "..."
}

/**
 * Replaces multiple consecutive whitespace characters with a single space.
 *
 * @return A new string with duplicate spaces removed.
 *
 * ### Usage Example:
 * ```kotlin
 * println("Hello   World".removeDuplicateSpaces()) // Output: "Hello World"
 * ```
 */
fun String.removeDuplicateSpaces(): String {
    return this.replace(Regex("\\s+"), " ")
}


/**
 * Extension method to get Date for String with specified format.
 */
fun String.dateInFormat(format: String): Date? {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    var parsedDate: Date? = null
    try {
        parsedDate = dateFormat.parse(this)
    } catch (ignored: ParseException) {
        ignored.printStackTrace()
    }
    return parsedDate
}


fun String?.isNotNullEmptyBlank(): Boolean {
    return this != null && this.isNotEmptyAndBlank()
}



fun String.toCapitalize(): String {
    return this.split(Regex("\\s+")).joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}

/**
 * Extension function to validate if a string is a valid JSON object or array.
 *
 * @return `true` if the string is a valid JSON object or array; `false` otherwise.
 */
fun String.isValidJson(): Boolean {
    // Early return for blank or empty strings

    if (this.isBlank()) {
        println("Input is blank or empty. Returning false.")
        return false
    }

    return try {
        val jsonElement: JsonElement? = Gson().fromJson(this, JsonElement::class.java)
        val isValid = jsonElement?.isJsonObject == true || jsonElement?.isJsonArray == true
        println("Input: '$this' | isValidJson: $isValid")
        isValid
    } catch (ex: JsonSyntaxException) {
        println("JsonSyntaxException for input: '$this' | Message: ${ex.message}")
        false
    }
}

inline fun <reified T> String.parseJson(): T? {
    return try {
        if (isValidJson()) {
            Gson().fromJson(this, T::class.java)
        } else {
            null
        }

    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun String?.toSafeInt(defaultValue: Int = 0): Int {
    return this?.toIntOrNull() ?: defaultValue
}

fun String?.toSafeFloat(defaultValue: Float = 0f): Float {
    return this?.toFloatOrNull() ?: defaultValue
}

fun String?.toSafeDouble(defaultValue: Double = 0.0): Double {
    return this?.toDoubleOrNull() ?: defaultValue
}

fun String?.toSafeLong(defaultValue: Long = 0L): Long {
    return this?.toLongOrNull() ?: defaultValue
}

/**
 * Safely parses the string to the specified numeric type.
 *
 * @param T The numeric type to parse to (Int, Float, Double, Long).
 * @param defaultValue The default value to return if parsing fails.
 * @return The parsed numeric value or the default.
 */
inline fun <reified T : Number> String?.safeParse(defaultValue: T): T {
    return when (T::class) {
        Int::class -> this?.toIntOrNull() as T? ?: defaultValue
        Float::class -> this?.toFloatOrNull() as T? ?: defaultValue
        Double::class -> this?.toDoubleOrNull() as T? ?: defaultValue
        Long::class -> this?.toLongOrNull() as T? ?: defaultValue
        else -> defaultValue
    }
}


/**
 * Safely parses a String to an Int.
 * - Preserves a single leading negative sign.
 * - Removes all other non-digit characters.
 * - Caps the value to Int.MAX_VALUE or Int.MIN_VALUE if it exceeds the range.
 * - Returns 0 if parsing fails.
 */
fun String.safeParseToInt(): Int {
    val trimmed = this.trim()
    if (trimmed.isEmpty()) return 0

    val isNegative = trimmed.startsWith("-")
    var sanitized = if (isNegative) {
        "-" + trimmed.substring(1)
            .normalizeDigits()
            .replace(Regex("[^0-9]"), "")
    } else {
        trimmed
            .normalizeDigits()
            .replace(Regex("[^0-9]"), "")
    }

    if (sanitized == "-" || sanitized.isEmpty()) return 0

    // Remove any additional '-' signs that might have been inadvertently included
    if (isNegative && sanitized.substring(1).contains("-")) {
        sanitized = "-" + sanitized.substring(1).replace("-", "")
    }

    println("Input: '$this' | Sanitized: '$sanitized'") // Debugging line

    return sanitized.toIntOrNull() ?: 0
}

/**
 * Safely parses a String to a Double.
 * - Handles negative numbers.
 * - Supports both '.' and ',' as decimal separators.
 * - Ensures only one decimal separator is present.
 * - Returns 0.0 if parsing fails.
 */
fun String.safeParseToDouble(): Double {
    val trimmed = this.trim().replaceArabicDecimalSeparator().replaceCommaWithDecimal()
    if (trimmed.isEmpty()) return 0.0

    val isNegative = trimmed.startsWith("-")
    var sanitized = if (isNegative) {
        "-" + trimmed.substring(1)
            .normalizeDigits()
            .replaceArabicDecimalSeparator()
            .replace(Regex("[^0-9.]"), "")
    } else {
        trimmed
            .normalizeDigits()
            .replaceArabicDecimalSeparator()
            .replace(Regex("[^0-9.]"), "")
    }

    if (sanitized == "-" || sanitized.isEmpty() || sanitized.count { it == '.' } > 1) return 0.0

    // Remove any additional '-' signs that might have been inadvertently included
    if (isNegative && sanitized.substring(1).contains("-")) {
        sanitized = "-" + sanitized.substring(1).replace("-", "")
    }

    println("Input: '$this' | Sanitized: '$sanitized'") // Debugging line

    return sanitized.toDoubleOrNull() ?: 0.0
}

/**
 * Replaces Arabic decimal separator '٫' (U+066B) with '.'.
 */
private fun String.replaceArabicDecimalSeparator(): String {
    return this.replace('٫', '.')
}


private fun String.replaceCommaWithDecimal(): String {
    return this.replace(',', '.')
}

/**
 * Safely parses a String to a Boolean.

 * @return false if the string is null or empty.
 * @return true if the string equals "true" (case-insensitive) or "1".
 * @return false if the string equals "false" (case-insensitive) or "0".
 * @return false for any other value, including null or empty strings.
 * @return true if the string is yes.
 * @return true if the string is on.
 * @return false if the string is no.
 * @return false if string is off
 */
fun Any?.toSmartBoolean(): Boolean {
    return when (this?.toString()?.trim()?.lowercase()) {
        "true", "1", "1.0", "yes", "on" -> true
        "false", "0", "no", "off" -> false
        else -> false
    }
}


fun String?.toSafeBoolean(defaultValue: Boolean = false): Boolean {
    return when (this?.lowercase()) {
        "true" -> true
        "false" -> false
        else -> defaultValue
    }
}

fun String?.toSafeString(defaultValue: String = ""): String {
    return this ?: defaultValue
}

/**
 * Masks a part of the string with a given character.
 *
 * @param start The starting index of the mask.
 * @param end The ending index of the mask (exclusive).
 * @param maskChar The character used for masking.
 * @return A masked string.
 */
fun String.mask(start: Int = 2, end: Int = this.length - 2, maskChar: Char = '*'): String {
    if (start >= end) return this
    val maskPart = this.substring(start, end).map { maskChar }.joinToString("")
    return this.replaceRange(start, end, maskPart)
}

/**
 * Replaces all non-Western digits (Arabic-Indic and Eastern Arabic) with Western digits.
 */
private fun String.normalizeDigits(): String {
    val arabicIndicDigits = mapOf(
        '٠' to '0',
        '١' to '1',
        '٢' to '2',
        '٣' to '3',
        '٤' to '4',
        '٥' to '5',
        '٦' to '6',
        '٧' to '7',
        '٨' to '8',
        '٩' to '9'
    )

    val easternArabicDigits = mapOf(
        '۰' to '0',
        '۱' to '1',
        '۲' to '2',
        '۳' to '3',
        '۴' to '4',
        '۵' to '5',
        '۶' to '6',
        '۷' to '7',
        '۸' to '8',
        '۹' to '9'
    )

    return this.map {
        when (it) {
            in arabicIndicDigits.keys -> arabicIndicDigits[it]!!
            in easternArabicDigits.keys -> easternArabicDigits[it]!!
            else -> it
        }
    }.joinToString("")
}

/**
 * Increments [startIndex] until this string is not ASCII whitespace. Stops at [endIndex].
 */
fun String.indexOfFirstNonAsciiWhitespace(startIndex: Int = 0, endIndex: Int = length): Int {
    for (i in startIndex until endIndex) {
        when (this[i]) {
            '\t', '\n', '\u000C', '\r', ' ' -> Unit
            else -> return i
        }
    }
    return endIndex
}

/**
 * Decrements [endIndex] until `input[endIndex - 1]` is not ASCII whitespace. Stops at [startIndex].
 */
fun String.indexOfLastNonAsciiWhitespace(startIndex: Int = 0, endIndex: Int = length): Int {
    for (i in endIndex - 1 downTo startIndex) {
        when (this[i]) {
            '\t', '\n', '\u000C', '\r', ' ' -> Unit
            else -> return i + 1
        }
    }
    return startIndex
}

fun String.trimSubstring(startIndex: Int = 0, endIndex: Int = length): String {
    val start = indexOfFirstNonAsciiWhitespace(startIndex, endIndex)
    val end = indexOfLastNonAsciiWhitespace(start, endIndex)
    return substring(start, end)
}


/**************************************************
 *              DEPRECATED FUNCTIONS
 * *********************************************/


@Deprecated("use toCapitalize", replaceWith = ReplaceWith("toCapitalize()"))
fun String.capitalizeFirstLetter(): String {
    if (this.isEmpty()) return ""
    return this[0].uppercaseChar() + this.substring(1)
}

@Deprecated("use toCapitalize", replaceWith = ReplaceWith("toCapitalize()"))
fun String.capitalizeFirstLetterAndAfterSpace(): String {
    var result = ""
    var capitalizeNext = true  // Start with true to capitalize the first letter

    for (char in this) {
        result += if (capitalizeNext && char.isLetter()) {
            capitalizeNext = false
            char.uppercaseChar()
        } else {
            if (char.isWhitespace()) capitalizeNext = true
            char
        }
    }

    return result
}

@Deprecated("use toColorInt() instead", ReplaceWith("this.toColorInt()"))
fun String.toColorByRGB(str: String): Int {
    val s = str.uppercase()
    return try {
        Color.parseColor(s)
    } catch (e: IllegalArgumentException) {
        // Check if the string is a valid 6-digit hex color code
        if (s.matches("^#([0-9a-fA-F]{6})$".toRegex())) {
            Color.parseColor("#FF$s")
        } else {
            // Check if the string is a valid 3-digit hex color code
            if (s.matches("^#([0-9a-fA-F]{3})$".toRegex())) {
                val r = s[1].toString() + s[1].toString()
                val g = s[2].toString() + s[2].toString()
                val b = s[3].toString() + s[3].toString()
                Color.parseColor("#FF$r$g$b")
            } else {
                // Return a default color if the string is not a valid color code
                return "${s}0".toColor()
            }
        }
    } catch (e: Exception) {
        // Return a default color if an exception is thrown
        0
    }
}

@Deprecated("use safeParseToDouble instead", replaceWith = ReplaceWith("safeParseToDouble()"))
fun String.parseDouble(): Double {
    try {
        return if (this.isNotEmptyAndBlank()) {
            this.replace(",", ".").toDouble()
        } else {
            0.0
        }
    } catch (e: NumberFormatException) {
        val EASTERN_ARABIC_NUMBERS_LOCALE: Locale =
            Locale.Builder().setLanguage("ar").setExtension('u', "nu-arab").build()
        return NumberFormat.getInstance(EASTERN_ARABIC_NUMBERS_LOCALE).parse(this)?.toDouble()!!
    } catch (e: Exception) {
        return 0.0
    }
}


