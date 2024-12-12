package com.codegenetics.extensions.parsing

import com.codegenetics.extensions.extension.isValidJson
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsValidJsonTest {

    // -----------------------------
    // Tests for Valid JSON Objects
    // -----------------------------

    @Test
    fun testValidJsonObject() {
        val input = """{"name":"John", "age":30, "city":"New York"}"""
        assertTrue("Parsing a valid JSON object should return true.", input.isValidJson())
    }

    @Test
    fun testValidJsonArray() {
        val input = """["apple", "banana", "cherry"]"""
        assertTrue("Parsing a valid JSON array should return true.", input.isValidJson())
    }

    @Test
    fun testValidNestedJson() {
        val input = """{
            "person": {
                "name": "Alice",
                "age": 25
            },
            "hobbies": ["reading", "cycling"]
        }"""
        assertTrue("Parsing a valid nested JSON structure should return true.", input.isValidJson())
    }

    @Test
    fun testEmptyJsonObject() {
        val input = "{}"
        assertTrue("Parsing an empty JSON object should return true.", input.isValidJson())
    }

    @Test
    fun testEmptyJsonArray() {
        val input = "[]"
        assertTrue("Parsing an empty JSON array should return true.", input.isValidJson())
    }

    // -------------------------------
    // Tests for Invalid JSON Structures
    // -------------------------------

//    @Test
//    fun testInvalidJsonMissingQuotes() {
//        val input = """{name:"John", last:cena, city:"New York"}"""
//        assertFalse("Parsing JSON with missing quotes should return false.", input.isValidJson())
//    }

    @Test
    fun testInvalidJsonExtraComma() {
        val input = """{"name":"John", "age":30, "city":"New York",}"""
        assertFalse("Parsing JSON with an extra comma should return false.", input.isValidJson())
    }

    @Test
    fun testInvalidJsonUnclosedBrace() {
        val input = """{"name":"John", "age":30, "city":"New York"""
        assertFalse("Parsing JSON with an unclosed brace should return false.", input.isValidJson())
    }

    @Test
    fun testInvalidJsonUnclosedBracket() {
        val input = """["apple", "banana", "cherry""""
        assertFalse("Parsing JSON with an unclosed bracket should return false.", input.isValidJson())
    }

    @Test
    fun testInvalidJsonRandomString() {
        val input = """Just a random string, not JSON."""
        assertFalse("Parsing a random non-JSON string should return false.", input.isValidJson())
    }

    @Test
    fun testInvalidJsonNumberOnly() {
        val input = "12345"
        assertFalse("Parsing a string containing only a number should return false.", input.isValidJson())
    }

    @Test
    fun testInvalidJsonBooleanOnly() {
        val input = "true"
        assertFalse("Parsing a string containing only 'true' should return false.", input.isValidJson())
    }

    @Test
    fun testInvalidJsonNull() {
        val input = "null"
        assertFalse("Parsing the string 'null' should return false.", input.isValidJson())
    }

    // --------------------------------
    // Tests for Edge Cases and Null Inputs
    // --------------------------------

    @Test
    fun testEmptyString() {
        val input = ""
        assertFalse("Parsing an empty string should return false.", input.isValidJson())
    }

    @Test
    fun testWhitespaceOnlyString() {
        val input = "   "
        assertFalse("Parsing a whitespace-only string should return false.", input.isValidJson())
    }

}