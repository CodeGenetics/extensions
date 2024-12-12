package com.codegenetics.extensions.stringext

import com.codegenetics.extensions.extension.beautifyJson
import org.junit.Assert.assertEquals
import org.junit.Test

class JsonTest {
    /**
     * Tests beautifying a valid JSON object with default indentation.
     */
    @Test
    fun testBeautifyJson_validJsonObject_defaultIndent() {
        val input = """{"name":"John","age":30,"city":"New York"}"""
        val expected = """
            {
              "name": "John",
              "age": 30,
              "city": "New York"
            }
        """.trimIndent()

        val result = input.beautifyJson()
        assertEquals("Valid JSON object should be beautified correctly with default indentation.", expected, result)
    }

    /**
     * Tests beautifying a valid JSON array with default indentation.
     */
    @Test
    fun testBeautifyJson_validJsonArray_defaultIndent() {
        val input = """["apple", "banana", "cherry"]"""
        val expected = """
            [
              "apple",
              "banana",
              "cherry"
            ]
        """.trimIndent()

        val result = input.beautifyJson()
        assertEquals("Valid JSON array should be beautified correctly with default indentation.", expected, result)
    }


    /**
     * Tests beautifying an invalid JSON string.
     * Should return the original string unchanged.
     */
    @Test
    fun testBeautifyJson_invalidJson() {
        val input = """{name: John, age: 30, city: New York}""" // Missing quotes around keys and string values
        val expected = input

        val result = input.beautifyJson()
        assertEquals("Invalid JSON should return the original string unchanged.", expected, result)
    }

    /**
     * Tests beautifying an empty string.
     * Should return the original empty string.
     */
    @Test
    fun testBeautifyJson_emptyString() {
        val input = ""
        val expected = ""

        val result = input.beautifyJson()
        assertEquals("Empty string should return an empty string.", expected, result)
    }

    /**
     * Tests beautifying a blank string (only whitespace).
     * Should return the original blank string.
     */
    @Test
    fun testBeautifyJson_blankString() {
        val input = "    "
        val expected = "    "

        val result = input.beautifyJson()
        assertEquals("Blank string should return the original blank string.", expected, result)
    }

    /**
     * Tests beautifying a JSON string with nested objects.
     */
    @Test
    fun testBeautifyJson_nestedJsonObject() {
        val input = """{"person":{"name":"John","age":30},"city":"New York"}"""
        val expected = """
            {
              "person": {
                "name": "John",
                "age": 30
              },
              "city": "New York"
            }
        """.trimIndent()

        val result = input.beautifyJson()
        assertEquals("Nested JSON object should be beautified correctly.", expected, result)
    }

    /**
     * Tests beautifying a JSON string with Unicode characters.
     */
    @Test
    fun testBeautifyJson_unicodeCharacters() {
        val input = """{"greeting":"こんにちは","farewell":"さようなら"}"""
        val expected = """
            {
              "greeting": "こんにちは",
              "farewell": "さようなら"
            }
        """.trimIndent()

        val result = input.beautifyJson()
        assertEquals("JSON with Unicode characters should be beautified correctly.", expected, result)
    }

    /**
     * Tests beautifying a JSON string with array of objects.
     */
    @Test
    fun testBeautifyJson_jsonArrayOfObjects() {
        val input = """[{"name":"John"}, {"name":"Jane"}, {"name":"Doe"}]"""
        val expected = """
            [
              {
                "name": "John"
              },
              {
                "name": "Jane"
              },
              {
                "name": "Doe"
              }
            ]
        """.trimIndent()

        val result = input.beautifyJson()
        assertEquals("JSON array of objects should be beautified correctly.", expected, result)
    }

    /**
     * Tests beautifying a JSON string with numeric values.
     */
    @Test
    fun testBeautifyJson_numericValues() {
        val input = """{"integer":123,"float":123.456,"scientific":1.23e4}"""
        val expected = """
            {
              "integer": 123,
              "float": 123.456,
              "scientific": 1.23e4
            }
        """.trimIndent()

        val result = input.beautifyJson()
        assertEquals("JSON with numeric values should be beautified correctly.", expected, result)
    }
}