package com.codegenetics.extensions.parsing

import com.codegenetics.extensions.extension.safeParseToInt
import org.junit.Assert.assertEquals

import org.junit.Test

class SafeParseToIntTest {
    @Test
    fun testNormalInteger() {
        // Test with a standard integer string
        val input = "1234"
        val expected = 1234
        val result = input.safeParseToInt()
        assertEquals("Parsing a standard integer failed.", expected, result)
    }

    @Test
    fun testWithExtraCharacters() {
        // Test with extra non-numeric characters surrounding the number
        val input = "abc1234xyz"
        val expected = 1234
        val result = input.safeParseToInt()
        assertEquals("Parsing with extra characters failed.", expected, result)
    }

    @Test
    fun testNoDigits() {
        // Test with a string that contains no digits
        val input = "NoNumberHere"
        val expected = 0
        val result = input.safeParseToInt()
        assertEquals("Parsing a string with no digits should return 0.", expected, result)
    }

    @Test
    fun testLeadingZeros() {
        // Test with leading zeros in the number
        val input = "001234"
        val expected = 1234
        val result = input.safeParseToInt()
        assertEquals("Parsing a string with leading zeros failed.", expected, result)
    }

    @Test
    fun testNegativeNumber() {
        // Test with a negative number
        val input = "-1234"
        // Since the '-' is removed, it becomes "1234"
        val expected = -1234
        val result = input.safeParseToInt()
        assertEquals("Parsing a negative number should ignore the '-' and return positive value.", expected, result)
    }

    @Test
    fun testNumberWithSpaces() {
        // Test with spaces within the number
        val input = "12 34"
        // Spaces are removed, resulting in "1234"
        val expected = 1234
        val result = input.safeParseToInt()
        assertEquals("Parsing a number with spaces failed.", expected, result)
    }

    @Test
    fun testMultipleNonDigits() {
        // Test with multiple non-digit characters interspersed with digits
        val input = "1a2b3c4"
        val expected = 1234
        val result = input.safeParseToInt()
        assertEquals("Parsing a string with multiple non-digits failed.", expected, result)
    }

    @Test
    fun testEmptyString() {
        // Test with an empty string
        val input = ""
        val expected = 0
        val result = input.safeParseToInt()
        assertEquals("Parsing an empty string should return 0.", expected, result)
    }

    @Test
    fun testOnlyNonDigits() {
        // Test with a string that contains only non-digit characters
        val input = "!@#$%^&*()"
        val expected = 0
        val result = input.safeParseToInt()
        assertEquals("Parsing a string with only non-digits should return 0.", expected, result)
    }

    @Test
    fun testNumbersWithSymbols() {
        // Test with numbers embedded within symbols
        val input = "#1!2@3#4$"
        val expected = 1234
        val result = input.safeParseToInt()
        assertEquals("Parsing a string with numbers and symbols failed.", expected, result)
    }

    @Test
    fun testZero() {
        // Test with a string representing zero
        val input = "0"
        val expected = 0
        val result = input.safeParseToInt()
        assertEquals("Parsing '0' should return 0.", expected, result)
    }

    @Test
    fun testMultipleZeros() {
        // Test with multiple zeros
        val input = "0000"
        val expected = 0
        val result = input.safeParseToInt()
        assertEquals("Parsing multiple zeros should return 0.", expected, result)
    }

    @Test
    fun testLargeNumber() {
        // Test with a very large number
        val input = "123456789012345"
        val expected = 0 // Adjusted to fit into Int
        val result = input.safeParseToInt()
        assertEquals("Parsing a very large number failed.", expected, result)
    }
    @Test
    fun testMultipleNegativeSigns() {
        val input = "--1234"
        val expected = -1234 // Only the first '-' is preserved
        val result = input.safeParseToInt()
        assertEquals("Parsing multiple negative signs should preserve only the first one.", expected, result)
    }

    @Test
    fun testNegativeWithExtraSymbols() {
        val input = "-a-b-c1234"
        val expected = -1234
        val result = input.safeParseToInt()
        assertEquals("Parsing a negative number with multiple '-' and extra characters should preserve only the first '-'.", expected, result)
    }

    @Test
    fun `safeParseInt should correctly parse Arabic-Indic digits`() {
        val arabicIndicDigits = "١٢٣٤٥٦٧٨٩٠" // Unicode: U+0661 to U+0660
        val expected = 1234567890
        val actual = arabicIndicDigits.safeParseToInt()
        assertEquals( "Arabic-Indic digits should parse to $expected",expected, actual)
    }

    @Test
    fun `safeParseInt should correctly parse Eastern Arabic numerals`() {
        val easternArabicDigits = "۱۲۳۴۵۶۷۸۹۰" // Unicode: U+06F1 to U+06F0
        val expected = 1234567890
        val actual = easternArabicDigits.safeParseToInt()
        assertEquals("Eastern Arabic numerals should parse to $expected",expected, actual)
    }

    @Test
    fun `safeParseInt should return default value for mixed digits`() {
        val mixedDigits = "١٢٣ABC٤٥٦" // Mix of Arabic-Indic digits and Latin letters
        val expected = 123456
        val actual = mixedDigits.safeParseToInt()
        assertEquals( "Mixed digits should parse to $expected by ignoring non-digits",expected, actual,)
    }

    @Test
    fun `safeParseInt should return 0 for invalid input`() {
        val invalidInput = "ABCDEF"
        val expected = 0
        val actual = invalidInput.safeParseToInt()
        assertEquals("Invalid input should return default value $expected",expected, actual, )
    }

}