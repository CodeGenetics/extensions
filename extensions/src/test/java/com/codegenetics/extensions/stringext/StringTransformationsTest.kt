package com.codegenetics.extensions.stringext

import com.codegenetics.extensions.extension.abbreviate
import com.codegenetics.extensions.extension.isPalindrome
import com.codegenetics.extensions.extension.maskEmail
import com.codegenetics.extensions.extension.removeDuplicateSpaces
import com.codegenetics.extensions.extension.removeVowels
import com.codegenetics.extensions.extension.reverse
import com.codegenetics.extensions.extension.toCamelCase
import com.codegenetics.extensions.extension.toCamelCaseWithSpaces
import com.codegenetics.extensions.extension.toEmoji
import com.codegenetics.extensions.extension.toKebabCase
import com.codegenetics.extensions.extension.toPascalCase
import com.codegenetics.extensions.extension.toSlug
import com.codegenetics.extensions.extension.toTitleCase
import org.junit.Assert.assertEquals
import org.junit.Test

class StringTransformationsTest {
    // -----------------------------
    // Tests for toTitleCase()
    // -----------------------------

    @Test
    fun testToTitleCase_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.toTitleCase()
        assertEquals("Converting an empty string should return an empty string.", expected, actual)
    }

    @Test
    fun testToTitleCase_singleWord() {
        val input = "hello"
        val expected = "Hello"
        val actual = input.toTitleCase()
        assertEquals("Converting a single word should capitalize the first letter.", expected, actual)
    }

    @Test
    fun testToTitleCase_multipleWords() {
        val input = "hello world example"
        val expected = "Hello World Example"
        val actual = input.toTitleCase()
        assertEquals("Converting multiple words should capitalize each word's first letter.", expected, actual)
    }

    @Test
    fun testToTitleCase_mixedCase() {
        val input = "hElLo wOrLd eXaMpLe"
        val expected = "HElLo WOrLd EXaMpLe"
        val actual = input.toTitleCase()
        assertEquals("Converting mixed-case words should capitalize each word's first letter.", expected, actual)
    }

    // -----------------------------
    // Tests for reverse()
    // -----------------------------

    @Test
    fun testReverse_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.reverse()
        assertEquals("Reversing an empty string should return an empty string.", expected, actual)
    }

    @Test
    fun testReverse_singleCharacter() {
        val input = "a"
        val expected = "a"
        val actual = input.reverse()
        assertEquals("Reversing a single character should return the same character.", expected, actual)
    }

    @Test
    fun testReverse_multipleCharacters() {
        val input = "hello"
        val expected = "olleh"
        val actual = input.reverse()
        assertEquals("Reversing multiple characters should return characters in reverse order.", expected, actual)
    }

    // -----------------------------
    // Tests for toKebabCase()
    // -----------------------------

    @Test
    fun testToKebabCase_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.toKebabCase()
        assertEquals("Converting an empty string should return an empty string.", expected, actual)
    }

    @Test
    fun testToKebabCase_singleWord() {
        val input = "hello"
        val expected = "hello"
        val actual = input.toKebabCase()
        assertEquals("Converting a single word should return the lowercase word.", expected, actual)
    }

    @Test
    fun testToKebabCase_multipleWords() {
        val input = "Hello World Example"
        val expected = "hello-world-example"
        val actual = input.toKebabCase()
        assertEquals("Converting multiple words should replace spaces with hyphens and lowercase letters.", expected, actual)
    }

    // -----------------------------
    // Tests for toPascalCase()
    // -----------------------------

    @Test
    fun testToPascalCase_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.toPascalCase()
        assertEquals("Converting an empty string should return an empty string.", expected, actual)
    }

    @Test
    fun testToPascalCase_singleWord() {
        val input = "hello"
        val expected = "Hello"
        val actual = input.toPascalCase()
        assertEquals("Converting a single word should capitalize the first letter.", expected, actual)
    }

    @Test
    fun testToPascalCase_multipleWords() {
        val input = "hello world example"
        val expected = "HelloWorldExample"
        val actual = input.toPascalCase()
        assertEquals("Converting multiple words should capitalize each word's first letter and remove spaces.", expected, actual)
    }

    // -----------------------------
    // Tests for isPalindrome()
    // -----------------------------

    @Test
    fun testIsPalindrome_emptyString() {
        val input = ""
        val expected = true
        val actual = input.isPalindrome()
        assertEquals("An empty string is considered a palindrome.", expected, actual)
    }

    @Test
    fun testIsPalindrome_singleCharacter() {
        val input = "a"
        val expected = true
        val actual = input.isPalindrome()
        assertEquals("A single character is considered a palindrome.", expected, actual)
    }

    @Test
    fun testIsPalindrome_simplePalindrome() {
        val input = "madam"
        val expected = true
        val actual = input.isPalindrome()
        assertEquals("The word 'madam' is a palindrome.", expected, actual)
    }

    @Test
    fun testIsPalindrome_phraseWithSpaces() {
        val input = "A man a plan a canal Panama"
        val expected = true
        val actual = input.isPalindrome()
        assertEquals("Ignoring spaces and case, the phrase is a palindrome.", expected, actual)
    }

    @Test
    fun testIsPalindrome_notAPalindrome() {
        val input = "Hello World"
        val expected = false
        val actual = input.isPalindrome()
        assertEquals("The phrase 'Hello World' is not a palindrome.", expected, actual)
    }

    // -----------------------------
    // Tests for removeVowels()
    // -----------------------------

    @Test
    fun testRemoveVowels_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.removeVowels()
        assertEquals("Removing vowels from an empty string should return an empty string.", expected, actual)
    }

    @Test
    fun testRemoveVowels_noVowels() {
        val input = "rhythm"
        val expected = "rhythm"
        val actual = input.removeVowels()
        assertEquals("Removing vowels from a string with no vowels should return the same string.", expected, actual)
    }

    @Test
    fun testRemoveVowels_withVowels() {
        val input = "Beautiful Day"
        val expected = "Btfl Dy"
        val actual = input.removeVowels()
        assertEquals("Removing vowels should return the string without vowels.", expected, actual)
    }

    // -----------------------------
    // Tests for maskEmail()
    // -----------------------------

    @Test
    fun testMaskEmail_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.maskEmail()
        assertEquals("Masking an empty string should return an empty string.", expected, actual)
    }

    @Test
    fun testMaskEmail_invalidEmail() {
        val input = "not-an-email"
        val expected = "not-an-email"
        val actual = input.maskEmail()
        assertEquals("Masking an invalid email should return the original string.", expected, actual)
    }

    @Test
    fun testMaskEmail_shortEmail() {
        val input = "a@b.com"
        val expected = "a@b.com"
        val actual = input.maskEmail()
        assertEquals("Masking a short email should handle minimal masking.", expected, actual)
    }

    @Test
    fun testMaskEmail_regularEmail() {
        val input = "john.doe@example.com"
        val expected = "j*****e@example.com"
        val actual = input.maskEmail()
        assertEquals("Masking a regular email should replace middle characters with asterisks.", expected, actual)
    }

    @Test
    fun testMaskEmail_emailWithShortName() {
        val input = "ab@example.com"
        val expected = "a*@example.com"
        val actual = input.maskEmail()
        assertEquals("Masking an email with a short name should handle accordingly.", expected, actual)
    }

    // -----------------------------
    // Tests for toSlug()
    // -----------------------------

    @Test
    fun testToSlug_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.toSlug()
        assertEquals("Converting an empty string to slug should return an empty string.", expected, actual)
    }

    @Test
    fun testToSlug_simpleString() {
        val input = "Hello World"
        val expected = "hello-world"
        val actual = input.toSlug()
        assertEquals("Converting 'Hello World' to slug should replace space with hyphen and lowercase letters.", expected, actual)
    }

    @Test
    fun testToSlug_stringWithSpecialCharacters() {
        val input = "Hello, World! This is a Test."
        val expected = "hello-world-this-is-a-test"
        val actual = input.toSlug()
        assertEquals("Converting a string with special characters to slug should replace them with hyphens and lowercase letters.", expected, actual)
    }

    @Test
    fun testToSlug_stringWithMultipleSpacesAndSymbols() {
        val input = "  Hello---World!!  "
        val expected = "hello-world"
        val actual = input.toSlug()
        assertEquals("Converting a string with multiple spaces and symbols to slug should handle them correctly.", expected, actual)
    }

    @Test
    fun testToSlug_nonASCIICharacters() {
        val input = "こんにちは 世界"
        val expected = "こんにちは-世界"
        val actual = input.toSlug()
        assertEquals("Converting a string with non-ASCII characters to slug should retain them and replace spaces with hyphens.", expected, actual)
    }

    // -----------------------------
    // Tests for abbreviate()
    // -----------------------------

    @Test
    fun testAbbreviate_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.abbreviate(5)
        assertEquals("Abbreviating an empty string should return an empty string.", expected, actual)
    }

    @Test
    fun testAbbreviate_shortString() {
        val input = "Hi"
        val expected = "Hi"
        val actual = input.abbreviate(5)
        assertEquals("Abbreviating a short string should return the original string.", expected, actual)
    }

    @Test
    fun testAbbreviate_exactLength() {
        val input = "Hello"
        val expected = "Hello"
        val actual = input.abbreviate(5)
        assertEquals("Abbreviating a string that matches the exact length should return the original string.", expected, actual)
    }

    @Test
    fun testAbbreviate_longString() {
        val input = "Hello World"
        val expected = "He..."
        val actual = input.abbreviate(5)
        assertEquals("Abbreviating a long string should truncate and add ellipses.", expected, actual)
    }

    @Test
    fun testAbbreviate_maxLengthLessThanEllipsis() {
        val input = "Hello"
        val expected = "H"
        val actual = input.abbreviate(1)
        assertEquals("Abbreviating with maxLength less than ellipsis should return truncated string.", expected, actual)
    }

    // -----------------------------
    // Tests for removeDuplicateSpaces()
    // -----------------------------

    @Test
    fun testRemoveDuplicateSpaces_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.removeDuplicateSpaces()
        assertEquals("Removing duplicate spaces from an empty string should return an empty string.", expected, actual)
    }

    @Test
    fun testRemoveDuplicateSpaces_singleSpace() {
        val input = " "
        val expected = " "
        val actual = input.removeDuplicateSpaces()
        assertEquals("Removing duplicate spaces from a single space should return a single space.", expected, actual)
    }

    @Test
    fun testRemoveDuplicateSpaces_multipleSpaces() {
        val input = "Hello   World"
        val expected = "Hello World"
        val actual = input.removeDuplicateSpaces()
        assertEquals("Removing duplicate spaces should replace multiple spaces with a single space.", expected, actual)
    }

    @Test
    fun testRemoveDuplicateSpaces_tabsAndNewlines() {
        val input = "Hello\t\tWorld\n\nThis is a test."
        val expected = "Hello World This is a test."
        val actual = input.removeDuplicateSpaces()
        assertEquals("Removing duplicate spaces should replace tabs and newlines with single spaces.", expected, actual)
    }

    // -----------------------------
    // Tests for toCamelCaseWithSpaces()
    // -----------------------------

    @Test
    fun testToCamelCaseWithSpaces_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.toCamelCaseWithSpaces()
        assertEquals("Converting an empty string to Camel Case with spaces should return an empty string.", expected, actual)
    }

    @Test
    fun testToCamelCaseWithSpaces_singleWord() {
        val input = "hello"
        val expected = "Hello"
        val actual = input.toCamelCaseWithSpaces()
        assertEquals("Converting a single word should capitalize the first letter.", expected, actual)
    }

    @Test
    fun testToCamelCaseWithSpaces_multipleWords() {
        val input = "hello world example"
        val expected = "Hello World Example"
        val actual = input.toCamelCaseWithSpaces()
        assertEquals("Converting multiple words should capitalize each word's first letter.", expected, actual)
    }

    @Test
    fun testToCamelCaseWithSpaces_withNonAlphabeticCharacters() {
        val input = "hello-world_test"
        val expected = "Hello World Test"
        val actual = input.toCamelCaseWithSpaces()
        assertEquals("Converting a string with non-alphabetic characters should replace them with spaces and capitalize each word.", expected, actual)
    }




    // -----------------------------
    // Tests for toCamelCase()
    // -----------------------------

    @Test
    fun testToCamelCase_emptyString() {
        val input = ""
        val expected = ""
        val actual = input.toCamelCase()
        assertEquals("Converting an empty string should return an empty string.", expected, actual)
    }

    @Test
    fun testToCamelCase_singleWordLowercase() {
        val input = "hello"
        val expected = "hello"
        val actual = input.toCamelCase()
        assertEquals("Converting a single lowercase word should return the same word.", expected, actual)
    }

    @Test
    fun testToCamelCase_singleWordUppercase() {
        val input = "HELLO"
        val expected = "hello"
        val actual = input.toCamelCase()
        assertEquals("Converting a single uppercase word should return the lowercase version.", expected, actual)
    }

    @Test
    fun testToCamelCase_multipleWords() {
        val input = "hello world example"
        val expected = "helloWorldExample"
        val actual = input.toCamelCase()
        assertEquals("Converting multiple words should concatenate them in camelCase.", expected, actual)
    }

    @Test
    fun testToCamelCase_withHyphensAndUnderscores() {
        val input = "hello-world_test"
        val expected = "helloWorldTest"
        val actual = input.toCamelCase()
        assertEquals("Converting words separated by hyphens and underscores should handle them correctly.", expected, actual)
    }

    @Test
    fun testToCamelCase_withMultipleDelimiters() {
        val input = "  multiple   spaces and---symbols__"
        val expected = "multipleSpacesAndSymbols"
        val actual = input.toCamelCase()
        assertEquals("Converting a string with multiple delimiters should handle them correctly.", expected, actual)
    }

    @Test
    fun testToCamelCase_withNumbers() {
        val input = "version 2 update 3"
        val expected = "version2Update3"
        val actual = input.toCamelCase()
        assertEquals("Converting a string with numbers should retain them in camelCase.", expected, actual)
    }

    @Test
    fun testToCamelCase_withMixedCase() {
        val input = "Hello World Example"
        val expected = "helloWorldExample"
        val actual = input.toCamelCase()
        assertEquals("Converting a mixed-case string should standardize to camelCase.", expected, actual)
    }

    @Test
    fun testToCamelCase_alreadyCamelCase() {
        val input = "alreadyCamelCase"
        val expected = "alreadycamelcase"
        val actual = input.toCamelCase()
        assertEquals("Converting an already camelCase string should standardize it appropriately.", expected, actual)
    }

    @Test
    fun testToCamelCase_withNonAlphanumericCharacters() {
        val input = "JSON-response_parser"
        val expected = "jsonResponseParser"
        val actual = input.toCamelCase()
        assertEquals("Converting a string with non-alphanumeric characters should handle them correctly.", expected, actual)
    }

    @Test
    fun testToCamelCase_withUnicodeCharacters() {
        val input = "こんにちは 世界"
        val expected = "こんにちは世界" // Depending on requirements, may not change
        val actual = input.toCamelCase()
        assertEquals("Converting a string with Unicode characters should handle them correctly.", expected, actual)
    }

    @Test
    fun testToCamelCase_withMixedUnicodeAndASCII() {
        val input = "hello こんにちは world 世界"
        val expected = "helloこんにちはWorld世界"
        val actual = input.toCamelCase()
        assertEquals("Converting a string with mixed Unicode and ASCII characters should handle them correctly.", expected, actual)
    }

    @Test
    fun testToCamelCase_withLeadingAndTrailingSpaces() {
        val input = "  hello world  "
        val expected = "helloWorld"
        val actual = input.toCamelCase()
        assertEquals("Converting a string with leading and trailing spaces should trim them.", expected, actual)
    }

    @Test
    fun testToCamelCase_withEmptyWords() {
        val input = "hello  world   example"
        val expected = "helloWorldExample"
        val actual = input.toCamelCase()
        assertEquals("Converting a string with empty words should handle them correctly.", expected, actual)
    }

    @Test
    fun testToCamelCase_singleCharacterWords() {
        val input = "a b c d e"
        val expected = "aBCDE"
        val actual = input.toCamelCase()
        assertEquals("Converting a string with single-character words should handle capitalization correctly.", expected, actual)
    }

    @Test
    fun testToCamelCase_nonLatinScripts() {
        val input = "Привет мир пример"
        val expected = "приветМирПример"
        val actual = input.toCamelCase()
        assertEquals("Converting a string with non-Latin scripts should handle them correctly.", expected, actual)
    }




    @Test
    fun testToEmoji_validEmoji() {
        val input = "1F600" // Unicode for 😀
        val expected = "😀"
        val result = input.toEmoji()
        assertEquals("Valid emoji code should return the correct Emoji.", expected, result)
    }

    /**
     * Tests converting a valid hexadecimal string with lowercase letters.
     * Example: "1f603" -> 😃
     */
    @Test
    fun testToEmoji_validEmojiLowercase() {
        val input = "1f603" // Unicode for 😃
        val expected = "😃"
        val result = input.toEmoji()
        assertEquals("Valid lowercase emoji code should return the correct Emoji.", expected, result)
    }

    /**
     * Tests converting an invalid hexadecimal string.
     * Example: "ZZZZZZ" should return an empty string.
     */
    @Test
    fun testToEmoji_invalidHex() {
        val input = "ZZZZZZ"
        val expected = ""
        val result = input.toEmoji()
        assertEquals("Invalid hexadecimal string should return an empty string.", expected, result)
    }

    /**
     * Tests converting an empty string.
     * Should return an empty string.
     */
    @Test
    fun testToEmoji_emptyString() {
        val input = ""
        val expected = ""
        val result = input.toEmoji()
        assertEquals("Empty string should return an empty string.", expected, result)
    }

    /**
     * Tests converting a string with invalid length.
     * Example: "1F60" (only 4 characters) should still work if it's a valid code point.
     */
    @Test
    fun testToEmoji_invalidLengthButValid() {
        val input = "1F60" // Unicode for ὠ (U+1F60)
        val expected = "ὠ" // Actual character for U+1F60
        val result = input.toEmoji()
        assertEquals("Incomplete hexadecimal string should return the correct character if valid.", expected, result)
    }

    /**
     * Tests converting a hexadecimal string that doesn't correspond to an Emoji.
     * Example: "0041" -> 'A'
     */
    @Test
    fun testToEmoji_nonEmojiCharacter() {
        val input = "0041" // Unicode for 'A'
        val expected = "A"
        val result = input.toEmoji()
        assertEquals("Non-Emoji Unicode code should return the corresponding character.", expected, result)
    }

    /**
     * Tests converting the highest valid Unicode code point.
     * Example: "10FFFF" -> (valid max code point)
     */
    @Test
    fun testToEmoji_maxUnicode() {
        val input = "10FFFF" // Maximum valid Unicode code point
        val expected = "\uDBFF\uDFFF" // Surrogate pair for U+10FFFF
        val result = input.toEmoji()
        assertEquals("Maximum Unicode code point should return the correct character.", expected, result)
    }

    /**
     * Tests converting a code point beyond the Unicode range.
     * Example: "110000" -> Invalid, should return an empty string.
     */
    @Test
    fun testToEmoji_codePointTooHigh() {
        val input = "110000" // Beyond Unicode range
        val expected = ""
        val result = input.toEmoji()
        assertEquals("Code point beyond Unicode range should return an empty string.", expected, result)
    }

    /**
     * Tests converting a string with leading and trailing spaces.
     * Example: " 1F601 " -> 😁
     */
    @Test
    fun testToEmoji_withSpaces() {
        val input = " 1F601 "
        val expected = "😁"
        val result = input.toEmoji()
        assertEquals("String with spaces should trim and return the correct Emoji.", expected, result)
    }

    /**
     * Tests converting a lowercase hexadecimal string representing an Emoji.
     * Example: "1f602" -> 😂
     */
    @Test
    fun testToEmoji_validEmojiLowercaseFull() {
        val input = "1f602" // Unicode for 😂
        val expected = "😂"
        val result = input.toEmoji()
        assertEquals("Valid lowercase full emoji code should return the correct Emoji.", expected, result)
    }











}