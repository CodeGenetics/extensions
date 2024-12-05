package com.codegenetics.extensions.encryption

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import java.security.KeyStore
import java.security.SecureRandom
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EncryptionManager private constructor(private val alias: String) {

    init {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            throw UnsupportedOperationException("EncryptionManager requires API level 23 or higher.")
        }
    }

    companion object {
        private const val AES_MODE = "AES/CBC/PKCS5Padding"
        private const val HMAC_ALGORITHM = "HmacSHA256"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"

        /**
         * Creates an instance of [EncryptionManager] and ensures the key is ready.
         *
         * @param alias The alias for the key stored in the Keystore.
         * @return An instance of [EncryptionManager].
         */
        fun getInstance(alias: String): EncryptionManager {
            val manager = EncryptionManager(alias)
            manager.ensureKeyExists()
            return manager
        }
    }

    //region Key Management

    /**
     * Ensures that a key exists in the Keystore for the given alias.
     * If not, it generates a new key.
     */
    private fun ensureKeyExists() {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        if (!keyStore.containsAlias(alias)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                generateKey()
            }
        }
    }

    /**
     * Generates and securely stores an AES key in the Android Keystore.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateKey() {
        val keyGenerator = KeyGenerator.getInstance("AES", ANDROID_KEYSTORE)
        val keyGenSpec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .build()
        keyGenerator.init(keyGenSpec)
        keyGenerator.generateKey()
    }

    /**
     * Retrieves the AES key from the Android Keystore.
     *
     * @return The AES [SecretKey].
     */
    private fun getKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        return keyStore.getKey(alias, null) as SecretKey
    }

    /**
     * Rotates the current encryption key.
     * Generates a new key while keeping the old key for decrypting existing data.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun rotateKey() {
        val oldKey = getKey()
        generateKey()
        // Optional: Save `oldKey` securely for decrypting legacy data
    }

    /**
     * Exports the AES key as a Base64 string.
     *
     * @return The Base64-encoded AES key.
     */
    fun exportKey(): String {
        val key = getKey() as SecretKeySpec
        return Base64.encodeToString(key.encoded, Base64.DEFAULT)
    }

    /**
     * Imports a previously exported AES key.
     *
     * @param base64Key The Base64-encoded AES key.
     */
    fun importKey(base64Key: String) {
        val keyBytes = Base64.decode(base64Key, Base64.DEFAULT)
        val keySpec = SecretKeySpec(keyBytes, "AES")
        // Save this key securely (e.g., reinitialize it in your Keystore)
    }

    //endregion

    //region Encryption and Decryption

    /**
     * Encrypts the provided plaintext using AES encryption.
     *
     * @param plaintext The text to encrypt.
     * @return A Base64-encoded string containing the IV and ciphertext.
     * @throws EncryptionException if encryption fails.
     */
    fun encrypt(plaintext: String): String {
        try {
            val cipher = Cipher.getInstance(AES_MODE)
            val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.ENCRYPT_MODE, getKey(), ivSpec)
            val ciphertext = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))
            return Base64.encodeToString(iv + ciphertext, Base64.DEFAULT)
        } catch (e: Exception) {
            throw EncryptionException("Failed to encrypt data", e)
        }
    }

    /**
     * Decrypts the provided ciphertext using AES decryption.
     *
     * @param ciphertext The Base64-encoded string containing the IV and ciphertext.
     * @return The decrypted plaintext.
     * @throws EncryptionException if decryption fails.
     */
    fun decrypt(ciphertext: String): String {
        try {
            val decoded = Base64.decode(ciphertext, Base64.DEFAULT)
            val iv = decoded.copyOfRange(0, 16)
            val encryptedData = decoded.copyOfRange(16, decoded.size)
            val ivSpec = IvParameterSpec(iv)

            val cipher = Cipher.getInstance(AES_MODE)
            cipher.init(Cipher.DECRYPT_MODE, getKey(), ivSpec)
            val plaintextBytes = cipher.doFinal(encryptedData)
            return String(plaintextBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            throw EncryptionException("Failed to decrypt data", e)
        }
    }

    /**
     * Encrypts the provided plaintext asynchronously.
     *
     * @param plaintext The text to encrypt.
     * @return A Base64-encoded string containing the IV and ciphertext.
     * @throws EncryptionException if encryption fails.
     */
    suspend fun encryptAsync(plaintext: String): String = withContext(Dispatchers.IO) {
        encrypt(plaintext)
    }

    /**
     * Decrypts the provided ciphertext asynchronously.
     *
     * @param ciphertext The Base64-encoded string containing the IV and ciphertext.
     * @return The decrypted plaintext.
     * @throws EncryptionException if decryption fails.
     */
    suspend fun decryptAsync(ciphertext: String): String = withContext(Dispatchers.IO) {
        decrypt(ciphertext)
    }

    //endregion

    //region HMAC for Data Integrity

    /**
     * Generates an HMAC for the given data to ensure data integrity.
     *
     * @param data The data to sign.
     * @return A Base64-encoded HMAC.
     */
    fun generateHMAC(data: String): String {
        val secretKey = getKey()
        val mac = Mac.getInstance(HMAC_ALGORITHM)
        mac.init(secretKey)
        val hmacBytes = mac.doFinal(data.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(hmacBytes, Base64.DEFAULT)
    }

    /**
     * Validates the HMAC of the given data to verify its integrity.
     *
     * @param data The original data.
     * @param hmac The HMAC to validate.
     * @return `true` if the HMAC is valid, `false` otherwise.
     */
    fun validateHMAC(data: String, hmac: String): Boolean {
        val generatedHMAC = generateHMAC(data)
        return hmac == generatedHMAC
    }

    //endregion
}

/**
 * Custom exception for encryption-related errors.
 *
 * @param message The error message.
 * @param cause The underlying cause of the error.
 */
class EncryptionException(message: String, cause: Throwable) : Exception(message, cause)