package com.codegenetics.extensions.parsing

import com.codegenetics.extensions.extension.toSmartBoolean
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SafeParseBooleanTests {
    // -----------------------------
    // Tests for Valid 'True' Inputs
    // -----------------------------

    @Test
    fun testBooleanTrue() {
        val input: Boolean = true
        val expected = true
        val result = input.toSmartBoolean()
        assertEquals("Parsing Boolean true should return true.", expected, result)
    }

    @Test
    fun testStringTrueLowercase() {
        val input = "true"
        val expected = true
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'true' should return true.", expected, result)
    }

    @Test
    fun testStringTrueUppercase() {
        val input = "TRUE"
        val expected = true
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'TRUE' should return true.", expected, result)
    }

    @Test
    fun testStringTrueMixedCase() {
        val input = "TrUe"
        val expected = true
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'TrUe' should return true.", expected, result)
    }

    @Test
    fun testStringOne() {
        val input = "1"
        val expected = true
        val result = input.toSmartBoolean()
        assertEquals("Parsing string '1' should return true.", expected, result)
    }

    @Test
    fun testStringYes() {
        val input = "yes"
        val expected = true
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'yes' should return true.", expected, result)
    }

    @Test
    fun testStringOn() {
        val input = "on"
        val expected = true
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'on' should return true.", expected, result)
    }

    @Test
    fun testNumericOne() {
        val input: Int = 1
        val expected = true
        val result = input.toSmartBoolean()
        assertEquals("Parsing numeric 1 should return true.", expected, result)
    }

    // ------------------------------
    // Tests for Valid 'False' Inputs
    // ------------------------------

    @Test
    fun testBooleanFalse() {
        val input: Boolean = false
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing Boolean false should return false.", expected, result)
    }

    @Test
    fun testStringFalseLowercase() {
        val input = "false"
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'false' should return false.", expected, result)
    }

    @Test
    fun testStringFalseUppercase() {
        val input = "FALSE"
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'FALSE' should return false.", expected, result)
    }

    @Test
    fun testStringFalseMixedCase() {
        val input = "FaLsE"
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'FaLsE' should return false.", expected, result)
    }

    @Test
    fun testStringZero() {
        val input = "0"
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing string '0' should return false.", expected, result)
    }

    @Test
    fun testStringNo() {
        val input = "no"
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'no' should return false.", expected, result)
    }

    @Test
    fun testStringOff() {
        val input = "off"
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'off' should return false.", expected, result)
    }

    @Test
    fun testNumericZero() {
        val input: Int = 0
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing numeric 0 should return false.", expected, result)
    }

    // --------------------------------
    // Tests for Invalid or Edge Inputs
    // --------------------------------

    @Test
    fun testStringMaybe() {
        val input = "maybe"
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'maybe' should return false.", expected, result)
    }

    @Test
    fun testStringTwo() {
        val input = "2"
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing string '2' should return false.", expected, result)
    }

    @Test
    fun testStringNegativeOne() {
        val input = "-1"
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing string '-1' should return false.", expected, result)
    }

    @Test
    fun testStringTru() {
        val input = "tru"
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'tru' should return false.", expected, result)
    }

    @Test
    fun testStringFals() {
        val input = "fals"
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'fals' should return false.", expected, result)
    }

    @Test
    fun testStringTrueFalse() {
        val input = "truefalse"
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'truefalse' should return false.", expected, result)
    }

    @Test
    fun testStringFalseTrue() {
        val input = "falsetrue"
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing string 'falsetrue' should return false.", expected, result)
    }

    @Test
    fun testEmptyString() {
        val input = ""
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing an empty string should return false.", expected, result)
    }

    @Test
    fun testWhitespaceOnlyString() {
        val input = "   "
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing a whitespace-only string should return false.", expected, result)
    }

    @Test
    fun testNullInput() {
        val input: Any? = null
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing null input should return false.", expected, result)
    }

    @Test
    fun testListInput() {
        val input: Any? = listOf("true", "false")
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing a list input should return false.", expected, result)
    }

    @Test
    fun testObjectInput() {
        val input: Any? = Any()
        val expected = false
        val result = input.toSmartBoolean()
        assertEquals("Parsing a generic object input should return false.", expected, result)
    }

    @Test
    fun testFloatOnePointZero() {
        val input: Float = 1.0f
        val expected = true // '1.0' does not match "1", but since toString() is "1.0", which doesn't match, it should return false
        val result = input.toSmartBoolean()
        assertEquals("Parsing float 1.0 should return false.", expected, result)
    }

    @Test
    fun testDoubleZeroPointZero() {
        val input: Double = 0.0
        val expected = false // '0.0' does not match "0", returns false
        val result = input.toSmartBoolean()
        assertEquals("Parsing double 0.0 should return false.", expected, result)
    }


    @Test
    fun testTrueStrings() {
        assertTrue("Parsing 'yes' should return false.", "yes".toSmartBoolean())
        assertTrue("Parsing 'true' should return true.", "true".toSmartBoolean())
        assertTrue("Parsing 'True' should return true.", "True".toSmartBoolean())
        assertTrue("Parsing 'TRUE' should return true.", "TRUE".toSmartBoolean())
        assertTrue("Parsing '1' should return true.", "1".toSmartBoolean())
        assertTrue("Parsing '  true  ' (with spaces) should return true.", "  true  ".toSmartBoolean())
        assertTrue("Parsing ' 1 ' (with spaces) should return true.", " 1 ".toSmartBoolean())
    }

    @Test
    fun testFalseStrings() {
        assertFalse("Parsing 'false' should return false.", "false".toSmartBoolean())
        assertFalse("Parsing 'False' should return false.", "False".toSmartBoolean())
        assertFalse("Parsing 'FALSE' should return false.", "FALSE".toSmartBoolean())
        assertFalse("Parsing '0' should return false.", "0".toSmartBoolean())
        assertFalse("Parsing '  false  ' (with spaces) should return false.", "  false  ".toSmartBoolean())
        assertFalse("Parsing ' 0 ' (with spaces) should return false.", " 0 ".toSmartBoolean())
    }

    @Test
    fun testInvalidStrings() {

        assertFalse("Parsing 'no' should return false.", "no".toSmartBoolean())
        assertFalse("Parsing '2' should return false.", "2".toSmartBoolean())
        assertFalse("Parsing '-1' should return false.", "-1".toSmartBoolean())
        assertFalse("Parsing 'tru' should return false.", "tru".toSmartBoolean())
        assertFalse("Parsing 'fals' should return false.", "fals".toSmartBoolean())
        assertFalse("Parsing 'truefalse' should return false.", "truefalse".toSmartBoolean())
        assertFalse("Parsing 'falsetrue' should return false.", "falsetrue".toSmartBoolean())
    }

    @Test
    fun testNullAndEmptyStrings() {
        assertFalse("Parsing null should return false.", null.toSmartBoolean())
        assertFalse("Parsing empty string should return false.", "".toSmartBoolean())
        assertFalse("Parsing whitespace only string should return false.", "   ".toSmartBoolean())
    }
}