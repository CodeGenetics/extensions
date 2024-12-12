package com.codegenetics.extensions.parsing

import com.codegenetics.extensions.extension.toColor
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ColorParsing {
    /**
     * Tests parsing a valid 6-digit hexadecimal color code.
     */
    @Test
    fun testToColor_valid6Digit() {
        val input = "#FF5733"
        val expected = 0xFFFF5733.toInt() // Equivalent to -435143 in signed Int
        val result = input.toColor()
        assertEquals("Parsing a valid 6-digit color code failed.", expected, result)
    }

    /**
     * Tests parsing a valid 3-digit hexadecimal color code.
     * The validateColor() function should convert "#F00" to "#FF0000".
     */
    @Test
    fun testToColor_valid3Digit() {
        val input = "#F00"
        val expected = 0xFFFF0000.toInt() // Corrected expansion to include full alpha
        val result = input.toColor()
        assertEquals("Parsing a valid 3-digit color code failed.", expected, result)
    }

    /**
     * Tests parsing a valid 4-digit hexadecimal color code with alpha.
     * The validateColor() function should convert "#F00A" to "#FF0000AA".
     */
    @Test
    fun testToColor_valid4Digit() {
        val input = "#F00A"
        val expected = 0xFF0000AA.toInt() // Corrected expansion to include full alpha
        val result = input.toColor()
        assertEquals("Parsing a valid 4-digit color code failed.", expected, result)
    }

    /**
     * Tests parsing a valid 8-digit hexadecimal color code with alpha.
     */
    @Test
    fun testToColor_valid8Digit() {
        val input = "#80FF5733"
        val expected = 0x80FF5733.toInt() // Equivalent to -2130771715 in signed Int
        val result = input.toColor()
        assertEquals("Parsing a valid 8-digit color code failed.", expected, result)
    }

    /**
     * Tests parsing a color code missing the '#' prefix.
     * Expected to return 0 due to invalid format.
     */
    @Test
    fun testToColor_invalidMissingHash() {
        val input = "FF5733" // Missing '#'
        val expected = 0 // Expected to return 0 due to invalid format
        val result = input.toColor()
        assertEquals("Parsing a color code missing '#' should return 0.", expected, result)
    }

    /**
     * Tests parsing a color code with invalid length.
     * Expected to return 0 due to invalid format.
     */
    @Test
    fun testToColor_invalidLength() {
        val input = "#FFFFF" // Invalid length (5 characters)
        val expected = 0 // Expected to return 0 due to invalid format
        val result = input.toColor()
        assertEquals("Parsing a color code with invalid length should return 0.", expected, result)
    }

    /**
     * Tests parsing a color code with invalid hexadecimal characters.
     * Expected to return 0 due to invalid hex.
     */
    @Test
    fun testToColor_invalidCharacters() {
        val input = "#GGGGGG" // Invalid hex characters
        val expected = 0 // Expected to return 0 due to invalid hex
        val result = input.toColor()
        assertEquals("Parsing a color code with invalid characters should return 0.", expected, result)
    }

    /**
     * Tests parsing an empty string.
     * Expected to return 0.
     */
    @Test
    fun testToColor_emptyString() {
        val input = ""
        val expected = 0 // Expected to return 0 due to empty string
        val result = input.toColor()
        assertEquals("Parsing an empty string should return 0.", expected, result)
    }

    /**
     * Tests parsing a color code with extra characters beyond valid length.
     * Expected to return 0 due to invalid format.
     */
    @Test
    fun testToColor_extraCharacters() {
        val input = "#FF5733ABC" // Extra characters after valid color code
        val expected = 0 // Expected to return 0 due to invalid format
        val result = input.toColor()
        assertEquals("Parsing a color code with extra characters should return 0.", expected, result)
    }

    /**
     * Tests parsing a color code with only the '#' character.
     * Expected to return 0 due to invalid format.
     */
    @Test
    fun testToColor_onlyHash() {
        val input = "#"
        val expected = 0 // Expected to return 0 due to invalid format
        val result = input.toColor()
        assertEquals("Parsing a string with only '#' should return 0.", expected, result)
    }

    /**
     * Tests parsing a color code with non-hexadecimal symbols.
     * Expected to return 0 due to invalid characters.
     */
    @Test
    fun testToColor_nonHexSymbols() {
        val input = "#@F!2#3"
        val expected = 0 // Expected to return 0 due to invalid characters
        val result = input.toColor()
        assertEquals("Parsing a color code with non-hexadecimal symbols should return 0.", expected, result)
    }
}