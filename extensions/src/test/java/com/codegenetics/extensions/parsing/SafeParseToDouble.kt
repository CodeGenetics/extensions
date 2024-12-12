package com.codegenetics.extensions.parsing


import com.codegenetics.extensions.extension.safeParseToDouble
import org.junit.Assert.assertEquals
import org.junit.Test


class SafeParseToDouble {
    @Test
    fun testNormalDouble() {
        val input = "123.45"
        val expected = 123.45
        val result = input.safeParseToDouble()
        assertEquals("Parsing a standard double failed.", expected, result, 0.0001)
    }

    @Test
    fun testWithExtraCharacters() {
        val input = "abc123.45xyz"
        val expected = 123.45
        val result = input.safeParseToDouble()
        assertEquals("Parsing with extra characters failed.", expected, result, 0.0001)
    }

    @Test
    fun testNoDigits() {
        val input = "NoNumberHere"
        val expected = 0.0
        val result = input.safeParseToDouble()
        assertEquals("Parsing a string with no digits should return 0.0.", expected, result, 0.0001)
    }

    @Test
    fun testMultipleDotsInvalidFormat() {
        val input = "12.34.56"
        val expected = 0.0
        val result = input.safeParseToDouble()
        assertEquals(
            "Parsing a string with multiple dots should return 0.0.",
            expected,
            result,
            0.0001
        )
    }

    @Test
    fun testLeadingZeros() {
        val input = "00123.45"
        val expected = 123.45
        val result = input.safeParseToDouble()
        assertEquals("Parsing a string with leading zeros failed.", expected, result, 0.0001)
    }

    @Test
    fun testWithCommaAsDecimal() {
        val input = "12,35"
        val expected = 12.35 // Adjust based on implementation
        val result = input.safeParseToDouble()
        assertEquals(
            "Parsing a string with comma as decimal separator failed.",
            expected,
            result,
            0.0001
        )
    }

    @Test
    fun testEmptyString() {
        val input = ""
        val expected = 0.0
        val result = input.safeParseToDouble()
        assertEquals("Parsing an empty string should return 0.0.", expected, result, 0.0001)
    }

    @Test
    fun testOnlyDecimalSeparator() {
        val input = "."
        val expected = 0.0
        val result = input.safeParseToDouble()
        assertEquals(
            "Parsing a string with only a decimal separator should return 0.0.",
            expected,
            result,
            0.0001
        )
    }

    @Test
    fun testNegativeNumber() {
        val input = "-123.45"
        val expected = -123.45
        val result = input.safeParseToDouble()
        assertEquals(
            "Parsing a negative number should return its negative value.",
            expected,
            result,
            0.0001
        )
    }

    @Test
    fun testNumberWithSpaces() {
        val input = "12 3.45"
        val expected = 123.45
        val result = input.safeParseToDouble()
        assertEquals("Parsing a number with spaces failed.", expected, result, 0.0001)
    }

    @Test
    fun testNumberWithMultipleCommasAndDots() {
        val input = "1,234.56.78"
        val expected = 0.0 // Invalid format
        val result = input.safeParseToDouble()
        assertEquals(
            "Parsing a string with multiple commas and dots should return 0.0.",
            expected,
            result,
            0.0001
        )
    }

    @Test
    fun testLargeNumber() {
        val input = "123456789012345"
        val expected = 123456789012345.0
        val result = input.safeParseToDouble()
        assertEquals("Parsing a very large number failed.", expected, result, 0.0001)
    }

    @Test
    fun `safeParseDouble should correctly parse Arabic-Indic digits with decimal`() {
        val arabicIndicDouble = "١٢٣٤٫٥٦٧" // "1234.567" with Arabic decimal separator (U+066B)
        val expected = 1234.567
        val actual = arabicIndicDouble.safeParseToDouble()
        assertEquals(
            "Arabic-Indic digits with decimal should parse to $expected",
            expected,
            actual,
            0.0001
        )
    }

    @Test
    fun `safeParseDouble should correctly parse Eastern Arabic numerals with decimal`() {
        val easternArabicDouble =
            "۱۲۳۴٫۵۶۷" // "1234.567" with Eastern Arabic decimal separator (U+066B)
        val expected = 1234.567
        val actual = easternArabicDouble.safeParseToDouble()
        assertEquals(
            "Eastern Arabic numerals with decimal should parse to $expected",
            expected,
            actual,0.0001
        )
    }

    @Test
    fun `safeParseDouble should return default value for mixed digits and decimal`() {
        val mixedDouble = "١٢٣ABC٤٫٥٦٧DEF" // Mix of Arabic-Indic digits, letters, and decimal
        val expected = 1234.567 // Assuming non-digit characters are removed
        val actual = mixedDouble.safeParseToDouble()
        assertEquals(
            "Mixed digits and characters should parse to $expected by ignoring non-digits",
            expected,
            actual,
            0.0001
        )
    }


    @Test
    fun `safeParseDouble should handle negative Arabic-Indic numbers`() {
        val negativeArabicIndic = "-١٢٣٫٤٥٦" // "-123.456"
        val expected = -123.456
        val actual = negativeArabicIndic.safeParseToDouble()
        assertEquals(
            "Negative Arabic-Indic number should parse to $expected",
            expected,
            actual,
            0.0001
        )
    }

    @Test
    fun `safeParseDouble should handle negative Eastern Arabic numerals`() {
        val negativeEasternArabic = "-۱۲۳۴٫۵۶۷" // "-1234.567"
        val expected = -1234.567
        val actual = negativeEasternArabic.safeParseToDouble()
        assertEquals(
            "Negative Eastern Arabic numerals should parse to $expected",
            expected,
            actual,
            0.0001
        )
    }


    @Test
    fun `safeParseDouble should ignore non-digit characters and parse correctly`() {
        val input = "١٢٣ABC٤٫٥٦٧DEF"
        val expected = 1234.567
        val actual = input.safeParseToDouble()
        assertEquals(
            "Input with non-digit characters should parse to $expected by ignoring letters",
            expected,
            actual,
            0.0001
        )
    }


    @Test
    fun `safeParseDouble should handle leading and trailing spaces`() {
        val input = "  ١٢٣٤٫٥٦٧  "
        val expected = 1234.567
        val actual = input.safeParseToDouble()
        assertEquals(
            "Input with leading and trailing spaces should parse to $expected", expected,
            actual, 0.0001
        )
    }

    @Test
    fun `safeParseDouble should return zero for multiple decimal separators`() {
        val input = "١٢٣٫٤٥٫٦٧"
        val expected = 0.0
        val actual = input.safeParseToDouble()
        assertEquals(
            "Input with multiple decimal separators should return $expected", expected,
            actual,0.0001
        )
    }

}