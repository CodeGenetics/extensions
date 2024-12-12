package com.codegenetics.extensions.stringext

import com.codegenetics.extensions.extension.toSnakeCase
import org.junit.Assert.assertEquals
import org.junit.Test

class SnakeCaseTest {
    // -----------------------------
    // Tests for Empty Strings
    // -----------------------------

    @Test
    fun testToSnakeCase_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.toSnakeCase()
        assertEquals("Converting an empty string should return an empty string.", expected, actual)
    }

    // -----------------------------
    // Tests for Single Words
    // -----------------------------

    @Test
    fun testToSnakeCase_singleLowercaseWord() {
        val input = "hello"
        val expected = "hello"
        val actual = input.toSnakeCase()
        assertEquals("Converting a single lowercase word should return the same word.", expected, actual)
    }

    @Test
    fun testToSnakeCase_singleMixedCaseWord() {
        val input = "HelloWorld"
        val expected = "helloworld"
        val actual = input.toSnakeCase()
        assertEquals("Converting a mixed-case single word should return all lowercase letters.", expected, actual)
    }

    // -----------------------------
    // Tests for Multiple Words
    // -----------------------------

    @Test
    fun testToSnakeCase_multipleWordsWithSpaces() {
        val input = "Hello World"
        val expected = "hello_world"
        val actual = input.toSnakeCase()
        assertEquals("Converting multiple words with spaces should replace spaces with underscores and lowercase letters.", expected, actual)
    }

    @Test
    fun testToSnakeCase_multipleWordsWithHyphensAndDots() {
        val input = "Hello-World.Test"
        val expected = "hello_world_test"
        val actual = input.toSnakeCase()
        assertEquals("Converting multiple words with hyphens and dots should replace them with underscores and lowercase letters.", expected, actual)
    }

    @Test
    fun testToSnakeCase_multipleWordsWithMultipleNonAlphanumerics() {
        val input = "Hello--World!!Test"
        val expected = "hello__world__test"
        val actual = input.toSnakeCase()
        assertEquals("Converting multiple words with multiple non-alphanumeric characters should replace each with an underscore and lowercase letters.", expected, actual)
    }

    // -----------------------------
    // Tests for Leading and Trailing Non-Alphanumerics
    // -----------------------------

    @Test
    fun testToSnakeCase_leadingNonAlphanumerics() {
        val input = "@@Hello World"
        val expected = "__hello_world"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with leading non-alphanumeric characters should replace them with underscores and lowercase letters.", expected, actual)
    }

    @Test
    fun testToSnakeCase_trailingNonAlphanumerics() {
        val input = "Hello World!!"
        val expected = "hello_world__"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with trailing non-alphanumeric characters should replace them with underscores and lowercase letters.", expected, actual)
    }

    @Test
    fun testToSnakeCase_leadingAndTrailingNonAlphanumerics() {
        val input = "**Hello World!!"
        val expected = "__hello_world__"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with leading and trailing non-alphanumeric characters should replace them with underscores and lowercase letters.", expected, actual)
    }

    // -----------------------------
    // Tests for Only Non-Alphanumeric Characters
    // -----------------------------

    @Test
    fun testToSnakeCase_onlyNonAlphanumerics() {
        val input = "!!!@@@###"
        val expected = "_________"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with only non-alphanumeric characters should replace each with an underscore.", expected, actual)
    }

    // -----------------------------
    // Tests for Strings with Numbers
    // -----------------------------

    @Test
    fun testToSnakeCase_stringWithNumbers() {
        val input = "Hello World 123"
        val expected = "hello_world_123"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with numbers should retain numbers and replace spaces with underscores.", expected, actual)
    }

    @Test
    fun testToSnakeCase_stringWithMixedNumbersAndLetters() {
        val input = "H3llo W0rld"
        val expected = "h3llo_w0rld"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with mixed numbers and letters should retain them and replace spaces with underscores.", expected, actual)
    }

    // -----------------------------
    // Tests for Strings with Existing Underscores
    // -----------------------------

    @Test
    fun testToSnakeCase_stringWithExistingUnderscores() {
        val input = "Hello_World"
        val expected = "hello_world"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with existing underscores should retain them and lowercase letters.", expected, actual)
    }

    @Test
    fun testToSnakeCase_stringWithMixedNonAlphanumericsIncludingUnderscores() {
        val input = "Hello__World!!"
        val expected = "hello__world__"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with mixed non-alphanumerics including underscores should replace them accordingly and lowercase letters.", expected, actual)
    }

    // -----------------------------
    // Tests for Unicode Characters
    // -----------------------------

    @Test
    fun testToSnakeCase_stringWithUnicodeLetters() {
        val input = "Héllo Wörld"
        val expected = "héllo_wörld"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with Unicode letters should retain them and replace spaces with underscores.", expected, actual)
    }

    @Test
    fun testToSnakeCase_stringWithNonASCIICharacters() {
        val input = "こんにちは 世界"
        val expected = "こんにちは_世界"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with non-ASCII characters should replace spaces with underscores.", expected, actual)
    }

    // -----------------------------
    // Tests for Strings with Emojis and Special Symbols
    // -----------------------------

    @Test
    fun testToSnakeCase_stringWithEmojis() {
        val input = "Hello 😊 World 🌍"
        val expected = "hello_😊_world_🌍"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with emojis should retain them and replace spaces with underscores.", expected, actual)
    }

    @Test
    fun testToSnakeCase_stringWithSpecialSymbols() {
        val input = "Hello@World#2024!"
        val expected = "hello_world_2024_"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with special symbols should replace them with underscores and lowercase letters.", expected, actual)
    }

    // -----------------------------
    // Tests for Consecutive Non-Alphanumeric Characters
    // -----------------------------

    @Test
    fun testToSnakeCase_stringWithConsecutiveNonAlphanumerics() {
        val input = "Hello---World!!!"
        val expected = "hello___world___"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with consecutive non-alphanumeric characters should replace each with an underscore.", expected, actual)
    }

    @Test
    fun testToSnakeCase_stringWithMixedConsecutiveNonAlphanumerics() {
        val input = "Hello-_-World"
        val expected = "hello___world"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with mixed consecutive non-alphanumeric characters should replace each with underscores.", expected, actual)
    }

    // -----------------------------
    // Additional Edge Cases
    // -----------------------------

    @Test
    fun testToSnakeCase_stringWithLeadingAndTrailingSpaces() {
        val input = "   Hello World   "
        val expected = "___hello_world___"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with leading and trailing spaces should replace them with underscores and lowercase letters.", expected, actual)
    }

    @Test
    fun testToSnakeCase_stringWithNoSpacesOrNonAlphanumerics() {
        val input = "helloworld"
        val expected = "helloworld"
        val actual = input.toSnakeCase()
        assertEquals("Converting a string with no spaces or non-alphanumerics should return the lowercase version.", expected, actual)
    }

    @Test
    fun testToSnakeCase_stringWithMixedCaseAndNonAlphanumerics() {
        val input = "HeLLo-WoRLd_Test"
        val expected = "hello_world_test"
        val actual = input.toSnakeCase()
        assertEquals("Converting a mixed-case string with non-alphanumerics should replace them with underscores and lowercase letters.", expected, actual)
    }
}