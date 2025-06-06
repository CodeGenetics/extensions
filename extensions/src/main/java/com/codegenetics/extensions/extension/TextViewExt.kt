package com.codegenetics.extensions.extension

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.Log
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.codegenetics.extensions.utils.ThickerStrikethroughSpan
import java.security.Key
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Tints the compound drawables of a TextView with the specified color resource.
 *
 * @param colorId The color resource ID to apply to the drawables.
 *
 * ### Usage Example:
 * ```kotlin
 * textView.tintViewDrawable(R.color.blue)
 * ```
 */
fun TextView.tintViewDrawable(@ColorRes colorId: Int) {
    val drawables = compoundDrawables
    for (drawable in drawables) {
        drawable?.setColorFilter(
            ContextCompat.getColor(context, colorId), PorterDuff.Mode.SRC_ATOP
        )
    }
}

/**
 * Sets the text color of the TextView using a color resource.
 *
 * @param colorId The color resource ID to apply to the text.
 *
 * ### Usage Example:
 * ```kotlin
 * textView.textColor(R.color.red)
 * ```
 */
fun TextView.textColor(@ColorRes colorId: Int) {
    setTextColor(ContextCompat.getColor(context, colorId))
}

/**
 * Adds an underline to the TextView text.
 *
 * ### Usage Example:
 * ```kotlin
 * textView.underLine()
 * ```
 */
fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}

/**
 * Removes the underline from the text of this TextView.
 *
 * This function modifies the paint flags of the TextView to remove the
 * `Paint.UNDERLINE_TEXT_FLAG`. After changing the flags, it calls `invalidate()`
 * to force a redraw of the view, ensuring the underline is visually removed.
 */
fun TextView.removeUnderline() {
    paint.flags = paint.flags and Paint.UNDERLINE_TEXT_FLAG.inv()
    invalidate()
}


/**
 * Adds a strikethrough effect to the TextView text.
 *
 * ### Usage Example:
 * ```kotlin
 * textView.deleteLine()
 * ```
 */
fun TextView.strikethrough() {
    paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
    paint.isAntiAlias = true
}

/**
 * Removes the strikethrough effect from the TextView.
 *
 * This function modifies the paint flags of the TextView to clear the
 * STRIKE_THRU_TEXT_FLAG, effectively removing any strikethrough line
 * that was previously applied. It then calls `invalidate()` to trigger
 * a redraw of the TextView, ensuring the change is visually reflected.
 */
fun TextView.removeStrikethrough() {
    paint.flags = paint.flags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    invalidate()
}

/**
 * Applies a strikethrough effect to a specified substring within the TextView.
 *
 * This extension function allows for customizing the thickness of the strikethrough line.
 * If the specified substring is not found in the TextView's text, no action is taken.
 *
 * @receiver The TextView to which the strikethrough will be applied.
 * @param substring The specific portion of the text to strike through.
 *                  Defaults to the entire text of the TextView.
 * @param thickness The desired thickness of the strikethrough line in pixels.
 *                  Defaults to 2f.
 */
fun TextView.strikeThroughCustom(
    substring: String = this.text.toString(),
    thickness: Float = 2f
) {
    val start = text.indexOf(substring)
    if (start >= 0) {
        val spannable = SpannableString(text)
        spannable.setSpan(
            ThickerStrikethroughSpan(thickness),
            start,
            start + substring.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = spannable
    }
}

/**
 * Removes any custom thicker strikethrough spans from the TextView's text.
 *
 * This function iterates through all `ThickerStrikethroughSpan` instances
 * applied to the `TextView`'s text and removes them.
 *
 * If the `TextView`'s text is not a `Spannable` (meaning it doesn't support spans),
 * this function will do nothing.
 */
fun TextView.removeStrikeThroughCustom() {
    val spannable = text as? Spannable ?: return
    val spans = spannable.getSpans(0, spannable.length, ThickerStrikethroughSpan::class.java)
    for (span in spans) {
        spannable.removeSpan(span)
    }
    text = spannable
}
/**
 * Makes the TextView text bold.
 *
 * ### Usage Example:
 * ```kotlin
 * textView.bold()
 * ```
 */
fun TextView.bold() {
    paint.isFakeBoldText = true
    paint.isAntiAlias = true
}

/**
 * Removes the bold formatting from the TextView.
 *
 * This function sets the `isFakeBoldText` property of the TextView's Paint object to `false`,
 * effectively turning off any simulated bold effect that might have been applied.
 * It then calls `invalidate()` to trigger a redraw of the TextView, ensuring the change
 * in text appearance is immediately visible.
 *
 * This is particularly useful for undoing a bold effect that was programmatically
 * applied using `paint.isFakeBoldText = true`.
 */
fun TextView.removeBold() {
    paint.isFakeBoldText = false
    invalidate()
}

/**
 * Sets the text style of the TextView to italic.
 *
 * This function modifies the TextView's typeface to display its text in an italic style.
 * It also enables anti-aliasing for smoother text rendering.
 *
 * @receiver The TextView on which to apply the italic style.
 */
fun TextView.italic() {
    setTypeface(typeface, Typeface.ITALIC)
    paint.isAntiAlias = true
}

/**
 * Removes the italic style from the TextView.
 *
 * This function sets the typeface of the TextView to `Typeface.NORMAL`,
 * which effectively removes any italic styling that might have been applied.
 * It then calls `invalidate()` to ensure the TextView is redrawn with the updated style.
 */
fun TextView.removeItalic() {
    setTypeface(null, Typeface.NORMAL)
    invalidate()
}

/**
 * Sets the typeface of the TextView to bold and italic.
 *
 * This function modifies the TextView's typeface to display text in a bold and italic style.
 * It also enables anti-aliasing for smoother text rendering.
 *
 * Example usage:
 * ```kotlin
 * myTextView.boldItalic()
 * ```
 */
fun TextView.boldItalic() {
    setTypeface(typeface, Typeface.BOLD_ITALIC)
    paint.isAntiAlias = true
}

/**
 * Resets all text styles applied to the TextView.
 *
 * This function clears any existing paint flags (like underline, strikethrough),
 * removes fake bolding, and sets the typeface back to the default normal style.
 * It then invalidates the view to ensure the changes are redrawn.
 *
 * This is useful for clearing any programmatically or XML-defined text styling
 * and starting fresh.
 */
fun TextView.resetTextStyles() {
    paint.flags = 0
    paint.isFakeBoldText = false
    setTypeface(null, Typeface.NORMAL)
    invalidate()
}


/**
 * Applies a header style to the TextView.
 *
 * This function modifies the TextView's appearance to resemble a header by:
 * - Setting the text color to the specified color resource.
 * - Making the text bold.
 * - Setting the text size to 20sp.
 *
 * @param colorId The resource ID of the color to use for the text.
 *                This should be a color resource (e.g., `R.color.my_header_color`).
 */
fun TextView.headerStyle(@ColorRes colorId: Int) {
    textColor(colorId)
    bold()
    textSize = 20f
}

/**
 * Applies a caption-style to the TextView.
 *
 * This includes setting the text color, making the text italic, and setting the text size to 12f.
 *
 * @param colorId The resource ID of the color to use for the text.
 */
fun TextView.captionStyle(@ColorRes colorId: Int) {
    textColor(colorId)
    italic()
    textSize = 12f
}

/**
 * Applies a standardized button style to a TextView.
 *
 * This function modifies the TextView's appearance to resemble a button by:
 * - Setting the text color using the provided color resource.
 * - Making the text bold.
 * - Setting the text size to 16sp.
 * - Converting all text to uppercase.
 *
 * @param colorId The resource ID of the color to be used for the text.
 *                Example: `R.color.my_button_color`
 */
fun TextView.buttonStyle(@ColorRes colorId: Int) {
    textColor(colorId)
    bold()
    textSize = 16f
    isAllCaps = true
}


/**
 * Changes the color of a specific substring within the TextView text.
 *
 * @param substring The substring whose color should be changed.
 * @param color The color resource ID to apply to the substring.
 *
 * ### Usage Example:
 * ```kotlin
 * textView.setColorOfSubstring("hello", R.color.green)
 * ```
 */
fun TextView.setColorOfSubstring(substring: String, color: Int) {
    try {
        try {
            val spannable = SpannableString(text)
            val start = text.indexOf(substring)
            if (start >= 0) {
                spannable.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, color)),
                    start,
                    start + substring.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                text = spannable
            }
        } catch (e: Exception) {
            Log.d("ViewExtensions", "Exception in setColorOfSubstring: $e")
        }
    } catch (e: Exception) {
        Log.d("ViewExtensions", "Exception in setColorOfSubstring: $e")
    }
}


/**
 * Sets a custom font for the TextView.
 *
 * @param font The font file name (must be in the `assets/fonts` directory).
 *
 * ### Usage Example:
 * ```kotlin
 * textView.font("custom_font")
 * ```
 */
fun TextView.font(font: String) {
    typeface = Typeface.createFromAsset(context.assets, "fonts/$font.ttf")
}

/**
 * Sets a drawable to the left of the TextView.
 *
 * @param drawable The drawable resource ID.
 */
fun TextView.setDrawableLeft(drawable: Int) {
    setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

/**
 * Sets a drawable to the right of the TextView.
 *
 * @param drawable The drawable resource ID.
 */
fun TextView.setDrawableRight(drawable: Int) {
    setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
}

/**
 * Sets a drawable to the top of the TextView.
 *
 * @param drawable The drawable resource ID.
 */
fun TextView.setDrawableTop(drawable: Int) {
    setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0)
}

/**
 * Sets a drawable to the bottom of the TextView.
 *
 * @param drawable The drawable resource ID.
 */
fun TextView.setDrawableBottom(drawable: Int) {
    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawable)
}

/**
 * Extension method to get md5 string.
 */
fun String.md5() = encrypt(this, "MD5")

/**
 * Extension method to get sha1 string.
 */
fun String.sha1() = encrypt(this, "SHA-1")

/**
 * Checks if the string is a valid phone number.
 *
 * @return `true` if valid, `false` otherwise.
 *
 * ### Usage Example:
 * ```kotlin
 * println("1234567890".isPhone()) // Output: false
 * ```
 */
fun String.isPhone(): Boolean {
    val regex = "^1([34578])\\d{9}$".toRegex()
    return matches(regex)
}

/**
 * Checks if the string is a valid email address.
 *
 * @return `true` if valid, `false` otherwise.
 *
 * ### Usage Example:
 * ```kotlin
 * println("test@example.com".isEmail()) // Output: true
 * ```
 */
fun String.isEmail(): Boolean {
    val regex = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$".toRegex()
    return matches(regex)
}

/**
 * Checks if the string contains only numeric characters.
 *
 * @return `true` if numeric, `false` otherwise.
 *
 * ### Usage Example:
 * ```kotlin
 * println("12345".isNumeric()) // Output: true
 * ```
 */
fun String.isNumeric(): Boolean {
    val regex = "^[0-9]+$".toRegex()
    return matches(regex)
}

/**
 * Extension method to check String equalsIgnoreCase
 */
fun String.equalsIgnoreCase(other: String) = this.lowercase().contentEquals(other.lowercase())

/**
 * Extension method to get encrypted string.
 */
private fun encrypt(string: String?, type: String): String {
    if (string.isNullOrEmpty()) {
        return ""
    }
    val md5: MessageDigest
    return try {
        md5 = MessageDigest.getInstance(type)
        val bytes = md5.digest(string!!.toByteArray())
        bytes2Hex(bytes)
    } catch (e: NoSuchAlgorithmException) {
        ""
    }
}

/**
 * Extension method to convert byteArray to String.
 */
private fun bytes2Hex(bts: ByteArray): String {
    var des = ""
    var tmp: String
    for (i in bts.indices) {
        tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}


private const val AES = "AES"
private const val AES_MODE = "AES/CBC/PKCS5Padding"
/**
 * Encrypts the given string using AES encryption with CBC mode and PKCS5 padding.
 *
 * @param key The encryption key (must be 16 bytes).
 * @return A Base64-encoded string containing the IV and encrypted data.
 *
 * ### Usage Example:
 * ```kotlin
 * val encrypted = "HelloWorld".encryptAES("mysecurekey12345")
 * println(encrypted) // Encrypted Base64 string
 * ```
 */
fun String.encryptAES(key: String): String {
    return try {
        val cipher = Cipher.getInstance(AES_MODE)
        val secretKey = generateKey(key)

        // Generate a random IV
        val iv = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val ivSpec = IvParameterSpec(iv)

        // Initialize the cipher
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)

        // Encrypt the data
        val encryptedBytes = cipher.doFinal(this.toByteArray(Charsets.UTF_8))

        // Combine IV and encrypted data and encode in Base64
        val combined = iv + encryptedBytes
        Base64.encodeToString(combined, Base64.DEFAULT)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * Decrypts the given AES-encrypted string using the specified key.
 *
 * @param key The decryption key (must match the key used for encryption).
 * @return The original decrypted string.
 *
 * ### Usage Example:
 * ```kotlin
 * val decrypted = encrypted.decryptAES("mysecurekey12345")
 * println(decrypted) // "HelloWorld"
 * ```
 */
fun String.decryptAES(key: String): String {
    return try {
        val cipher = Cipher.getInstance(AES_MODE)
        val secretKey = generateKey(key)

        // Decode Base64 and separate IV and encrypted data
        val combined = Base64.decode(this, Base64.DEFAULT)
        val iv = combined.copyOfRange(0, 16)
        val encryptedBytes = combined.copyOfRange(16, combined.size)
        val ivSpec = IvParameterSpec(iv)

        // Initialize the cipher
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

        // Decrypt the data
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        String(decryptedBytes, Charsets.UTF_8)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
/**
 * Generates a secret key for AES encryption.
 *
 * @param key The key string (must be 16, 24, or 32 characters long).
 * @return A SecretKeySpec object for encryption/decryption.
 */
private fun generateKey(key: String): Key {
    val keyBytes = key.toByteArray(Charsets.UTF_8)
    return SecretKeySpec(keyBytes.copyOf(16), AES)
}

/**
 * Extension function for TextView that colorizes the text based on character type.
 *
 * This function iterates through each character in the TextView's text and applies a color span
 * based on the following rules:
 * - **Uppercase letters:** Red
 * - **Lowercase letters:** Blue
 * - **Digits:** Green
 * - **Special characters:** Magenta
 *
 * The function uses [SpannableString] to apply [ForegroundColorSpan] to individual characters.
 * [Spannable.SPAN_EXCLUSIVE_EXCLUSIVE] flag ensures that new characters added at the start and end
 * of the text will not be included in the previous span.
 *
 * Example usage:
 * ```kotlin
 * val myTextView: TextView = findViewById(R.id.myTextView)
 * myTextView.text = "Hello World 123!"
 * myTextView.colorizeText()
 * ```
 * After calling `colorizeText()`, the `myTextView` will display:
 * - "H" and "W" in red.
 * - "ello" and "orld" in blue.
 * - "1", "2", and "3" in green.
 * - " " and "!" in magenta.
 *
 * @receiver TextView The TextView to apply the colorization to.
 */
fun TextView.colorizeText() {
    val spannable = SpannableString(text)
    text.forEachIndexed { index, char ->
        val color = when {
            char.isUpperCase() -> Color.RED          // Capital letters → Red
            char.isLowerCase() -> Color.BLUE         // Lowercase letters → Blue
            char.isDigit() -> Color.GREEN            // Digits → Green
            else -> Color.MAGENTA                    // Special characters → Magenta
        }
        spannable.setSpan(
            ForegroundColorSpan(color),
            index, index + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.text= spannable
}

/**
 * Starts a countdown timer and displays the remaining time in the specified TextView.
 *
 * This function extends the TextView class, allowing you to easily start a countdown
 * timer directly on a TextView instance. The timer will update the text of the TextView
 * every second, showing the remaining time in "minutes:seconds" format.
 *
 * @param startTimeInSeconds The total duration of the countdown in seconds.
 * @param prefix An optional string to be displayed before the time in the TextView. Defaults to an empty string.
 * @param onFinish An optional lambda function that will be invoked when the countdown finishes. Defaults to null.
 * @return A CountDownTimer instance. This can be used to cancel the timer if needed.
 *
 * Example Usage:
 *
 * ```kotlin
 * val myTextView = findViewById<TextView>(R.id.my_text_view)
 * val timer = myTextView.startCountdown(60, "Time Left: ") {
 *     // This code will be executed when the timer finishes.
 *     Toast.makeText(this, "Countdown finished!", Toast.LENGTH_SHORT).show()
 * }
 *
 * // To cancel the timer before it finishes:
 * timer.cancel()
 * ```
 *
 * Notes:
 *
 * - The `startTimeInSeconds` should be a positive integer value.
 * - The countdown display will format the time as "minutes:seconds".
 * - When the timer reaches zero, the TextView will display "$prefix 0:00".
 * - The `onFinish` callback is executed on the main thread.
 * - Make sure to cancel the timer when the activity or fragment is destroyed if you no longer need it.
 */
fun TextView.startCountdown(
    startTimeInSeconds: Long,
    prefix: String = "",
    onFinish: (() -> Unit)? = null
): CountDownTimer {
    this.text = String.format("%d:%02d", startTimeInSeconds / 60, startTimeInSeconds % 60)

    val timer = object : CountDownTimer(startTimeInSeconds * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val secondsRemaining = (millisUntilFinished / 1000).toInt()
            val minutes = secondsRemaining / 60
            val seconds = secondsRemaining % 60
            this@startCountdown.text = "$prefix $minutes:$seconds"
//            this@startCountdown.text = String.format("%d:%02d", minutes, seconds)
        }

        override fun onFinish() {
            this@startCountdown.text = "$prefix 0:00"
            onFinish?.invoke()
        }
    }
    timer.start()
    return timer
}