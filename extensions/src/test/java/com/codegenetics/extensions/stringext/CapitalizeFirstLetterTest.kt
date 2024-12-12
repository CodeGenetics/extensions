package com.codegenetics.extensions.stringext

import com.codegenetics.extensions.extension.capitalizeFirstLetter
import com.codegenetics.extensions.extension.capitalizeFirstLetterAndAfterSpace
import org.junit.Assert.assertEquals
import org.junit.Test

class CapitalizeFirstLetterTest {
    // -----------------------------
    // Tests for Empty Strings
    // -----------------------------

    @Test
    fun testCapitalizeFirstLetter_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing an empty string should return an empty string.", expected, actual)
    }

    // -----------------------------
    // Tests for Single Characters
    // -----------------------------

    @Test
    fun testCapitalizeFirstLetter_singleLowercaseCharacter() {
        val input = "a"
        val expected = "A"
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing a single lowercase character should return its uppercase.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetter_singleUppercaseCharacter() {
        val input = "A"
        val expected = "A"
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing a single uppercase character should return it unchanged.", expected, actual)
    }

    // -----------------------------
    // Tests for Multiple Characters
    // -----------------------------

    @Test
    fun testCapitalizeFirstLetter_multipleCharacters_firstLowercase() {
        val input = "hello world"
        val expected = "Hello world"
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing the first letter of a lowercase string should uppercase it.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetter_multipleCharacters_firstUppercase() {
        val input = "Hello World"
        val expected = "Hello World"
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing the first letter of an uppercase string should leave it unchanged.", expected, actual)
    }

    // -----------------------------
    // Tests for Non-Letter First Characters
    // -----------------------------

    @Test
    fun testCapitalizeFirstLetter_firstCharacterNumber() {
        val input = "1apple"
        val expected = "1apple" // Numbers remain unchanged
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing a string starting with a number should leave it unchanged.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetter_firstCharacterSymbol() {
        val input = "!hello"
        val expected = "!hello" // Symbols remain unchanged
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing a string starting with a symbol should leave it unchanged.", expected, actual)
    }

    // -----------------------------
    // Tests for Unicode and Emojis
    // -----------------------------

    @Test
    fun testCapitalizeFirstLetter_firstCharacterUnicodeLetter() {
        val input = "ñandú"
        val expected = "Ñandú"
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing a string starting with a Unicode letter should uppercase it.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetter_firstCharacterEmoji() {
        val input = "😊happy"
        val expected = "😊happy" // Emojis remain unchanged
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing a string starting with an emoji should leave it unchanged.", expected, actual)
    }

    // -----------------------------
    // Tests for Leading Whitespaces
    // -----------------------------

    @Test
    fun testCapitalizeFirstLetter_leadingWhitespaces() {
        val input = "   hello"
        val expected = "   hello" // Leading spaces remain, first non-space character is not capitalized
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing a string with leading whitespaces should only capitalize the first character.", expected, actual)
    }

    // -----------------------------
    // Additional Edge Cases
    // -----------------------------

    @Test
    fun testCapitalizeFirstLetter_allLowercase() {
        val input = "all lowercase"
        val expected = "All lowercase"
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing a fully lowercase string should uppercase the first character.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetter_allUppercase() {
        val input = "ALL UPPERCASE"
        val expected = "ALL UPPERCASE"
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing a fully uppercase string should leave it unchanged.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetter_mixedCase() {
        val input = "hElLo WoRLd"
        val expected = "HElLo WoRLd"
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing a mixed-case string should uppercase only the first character.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetter_firstCharacterNonAscii() {
        val input = "ßeta"
        val expected = "ßeta" // 'ß' does not have an uppercase equivalent in some contexts
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing a string starting with 'ß' should leave it unchanged.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetter_firstCharacterExtendedUnicode() {
        val input = "😊hello"
        val expected = "😊hello"
        val actual = input.capitalizeFirstLetter()
        assertEquals("Capitalizing a string starting with an extended Unicode character should leave it unchanged.", expected, actual)
    }


    // -----------------------------
    // Tests for Empty Strings
    // -----------------------------

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing an empty string should return an empty string.", expected, actual)
    }

    // -----------------------------
    // Tests for Single Characters
    // -----------------------------

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_singleLowercaseCharacter() {
        val input = "a"
        val expected = "A"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a single lowercase character should return its uppercase.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_singleUppercaseCharacter() {
        val input = "A"
        val expected = "A"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a single uppercase character should leave it unchanged.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_singleNonLetterCharacter() {
        val input = "1"
        val expected = "1"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a single non-letter character should leave it unchanged.", expected, actual)
    }

    // -----------------------------
    // Tests for Multiple Characters
    // -----------------------------

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_multipleCharacters_firstLowercase() {
        val input = "hello world"
        val expected = "Hello World"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing the first letter of a lowercase string should uppercase it.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_multipleCharacters_firstUppercase() {
        val input = "Hello World"
        val expected = "Hello World"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing the first letter of an uppercase string should leave it unchanged.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_multipleWords_withExtraSpaces() {
        val input = "hello   world"
        val expected = "Hello   World"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a string with multiple spaces between words should uppercase each word correctly.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_multipleWords_withTabsAndNewlines() {
        val input = "hello\tworld\nthis is a test"
        val expected = "Hello\tWorld\nThis Is A Test"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a string with tabs and newlines should uppercase each word correctly.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_stringWithSymbols() {
        val input = "hello-world! this is a test."
        val expected = "Hello-world! This Is A Test."
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a string with symbols should uppercase letters after spaces correctly.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_stringWithEmojis() {
        val input = "hello 😊 world"
        val expected = "Hello 😊 World"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a string with emojis should uppercase letters after spaces correctly.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_stringWithMixedCases() {
        val input = "hElLo wOrLd"
        val expected = "HElLo WOrLd"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a mixed-case string should uppercase the first character of each word.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_stringStartingWithNonLetter() {
        val input = "123hello world"
        val expected = "123Hello World"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a string starting with non-letter characters should uppercase letters after spaces.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_stringWithLeadingAndTrailingSpaces() {
        val input = "   hello world   "
        val expected = "   Hello World   "
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a string with leading and trailing spaces should uppercase letters correctly.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_stringWithOnlyWhitespaces() {
        val input = "     "
        val expected = "     "
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a string with only whitespaces should leave it unchanged.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_stringWithMultipleWhitespaceTypes() {
        val input = "hello \t world \n this is\ta test"
        val expected = "Hello \t World \n This Is\tA Test"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a string with multiple types of whitespaces should uppercase letters correctly.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_stringWithUnicodeLetters() {
        val input = "héllo wørld"
        val expected = "Héllo Wørld"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a string with Unicode letters should uppercase them correctly.", expected, actual)
    }

    // -----------------------------
    // Additional Edge Cases
    // -----------------------------

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_stringWithNoSpaces() {
        val input = "helloworld"
        val expected = "Helloworld"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a string with no spaces should only uppercase the first character.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_stringWithConsecutiveSpaces() {
        val input = "hello  world"
        val expected = "Hello  World"
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a string with consecutive spaces should uppercase letters after each space.", expected, actual)
    }

    @Test
    fun testCapitalizeFirstLetterAndAfterSpace_stringWithSpecialCharacters() {
        val input = "hello_world! this-is a test."
        val expected = "Hello_world! This-is A Test."
        val actual = input.capitalizeFirstLetterAndAfterSpace()
        assertEquals("Capitalizing a string with special characters should uppercase letters after spaces correctly.", expected, actual)
    }

}