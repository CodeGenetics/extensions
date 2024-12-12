package com.codegenetics.extensions.stringext

import com.codegenetics.extensions.extension.unescapeJavaString
import org.junit.Assert.assertEquals
import org.junit.Test

class UnescapeJavaStringTest {

    @Test
    fun testEmptyString() {
        val input = ""
        val expected = ""
        assertEquals(expected, input.unescapeJavaString())
    }

    @Test
    fun testNoEscapes() {
        val input = "Hello World!"
        val expected = "Hello World!"
        assertEquals(expected, input.unescapeJavaString())
    }

    @Test
    fun testStandardEscapes() {
        val input = "Line1\\nLine2\\tTabbed\\\\Backslash\\\"Quote\\'"
        val expected = "Line1\nLine2\tTabbed\\Backslash\"Quote'"
        assertEquals(expected, input.unescapeJavaString())
    }

    @Test
    fun testUnicodeEscapes() {
        val input = "Unicode Test: \\u0041\\u0042\\u0043"
        val expected = "Unicode Test: ABC"
        assertEquals(expected, input.unescapeJavaString())
    }

    @Test
    fun testIncompleteUnicodeEscape() {
        val input = "Incomplete Unicode: \\u123"
        val expected = "Incomplete Unicode: \\u123"
        assertEquals(expected, input.unescapeJavaString())
    }

    @Test
    fun testOctalEscapes() {
        val input = "Octal Test: \\101\\102\\103"
        val expected = "Octal Test: ABC"
        assertEquals(expected, input.unescapeJavaString())
    }

    @Test
    fun testInvalidOctalEscape() {
        val input = "Invalid Octal: \\89\\9"
        val expected = "Invalid Octal: \\89\\9"
        assertEquals(expected, input.unescapeJavaString())
    }

    @Test
    fun testMixedEscapes() {
        val input = "Mix: \\nNewLine, \\tTab, \\u0041A, \\101A"
        val expected = "Mix: \nNewLine, \tTab, AA, AA"
        assertEquals(expected, input.unescapeJavaString())
    }

    @Test
    fun testUnrecognizedEscapes() {
        val input = "Unrecognized: \\x\\y\\z"
        val expected = "Unrecognized: \\x\\y\\z"
        assertEquals(expected, input.unescapeJavaString())
    }
}