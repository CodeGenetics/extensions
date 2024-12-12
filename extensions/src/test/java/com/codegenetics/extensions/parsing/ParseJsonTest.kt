package com.codegenetics.extensions.parsing

import com.codegenetics.extensions.extension.parseJson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ParseJsonTest {

    // ----------------------------
    // Tests for Successful Parsing
    // ----------------------------

    @Test
    fun testParseValidJsonObjectToPerson() {
        val json = """{"name":"John", "age":30, "city":"New York"}"""
        val expected = Person(name = "John", age = 30, city = "New York")
        val result = json.parseJson<Person>()
        assertEquals(
            "Parsing valid JSON object to Person should return the correct object.",
            expected,
            result
        )
    }

    @Test
    fun testParseValidJsonArrayToFruitList() {
        val json = """{"fruits":["apple", "banana", "cherry"]}"""
        val expected = FruitList(fruits = listOf("apple", "banana", "cherry"))
        val result = json.parseJson<FruitList>()
        assertEquals(
            "Parsing valid JSON array to FruitList should return the correct object.",
            expected,
            result
        )
    }

    @Test
    fun testParseEmptyJsonObjectToPerson() {
        val json = "{}"
        // Assuming default values in Person data class if fields are missing
        val expected = Person(name = "", age = 0, city = "")
        val result = json.parseJson<Person>()
        assertEquals(
            "Parsing empty JSON object to Person should return object with default values.",
            expected,
            result
        )
    }

    @Test
    fun testParseEmptyJsonArrayToFruitList() {
        val json = """{"fruits":[]}"""
        val expected = FruitList(fruits = emptyList())
        val result = json.parseJson<FruitList>()
        assertEquals(
            "Parsing empty JSON array to FruitList should return object with empty list.",
            expected,
            result
        )
    }

    // ----------------------------
    // Tests for Failed Parsing
    // ----------------------------

    @Test
    fun testParseInvalidJsonMissingQuotes() {
        val json = """{name:"John", last:cena, city:"New York"}"""
        val expected = Person(name = "John", age = 0, city = "New York")
        val result = json.parseJson<Person>()

        assertEquals("Parsing JSON with missing quotes should return null.", result, expected)
    }

    @Test
    fun testParseInvalidJsonExtraComma() {
        val json = """{"name":"John", "age":30, "city":"New York",}"""
        val result = json.parseJson<Person>()
        assertNull("Parsing JSON with an extra comma should return null.", result)
    }

    @Test
    fun testParseInvalidJsonUnclosedBrace() {
        val json = """{"name":"John", "age":30, "city":"New York"""
        val result = json.parseJson<Person>()
        assertNull("Parsing JSON with an unclosed brace should return null.", result)
    }

    @Test
    fun testParseInvalidJsonUnclosedBracket() {
        val json = """["apple", "banana", "cherry""""
        val result = json.parseJson<List<String>>()
        assertNull("Parsing JSON with an unclosed bracket should return null.", result)
    }

    @Test
    fun testParseRandomString() {
        val json = """Just a random string, not JSON."""
        val result = json.parseJson<Person>()
        assertNull("Parsing a random non-JSON string should return null.", result)
    }

    @Test
    fun testParseNumberOnly() {
        val json = "12345"
        val result = json.parseJson<Int>()
        assertNull("Parsing a string containing only a number should return null.", result)
    }

    @Test
    fun testParseBooleanOnly() {
        val json = "true"
        val result = json.parseJson<Boolean>()
        assertNull("Parsing a string containing only 'true' should return null.", result)
    }

    @Test
    fun testParseNullString() {
        val json = "null"
        val result = json.parseJson<Person>()
        assertNull("Parsing the string 'null' should return null.", result)
    }

    // --------------------------------
    // Tests for Edge Cases and Null Inputs
    // --------------------------------

    @Test
    fun testParseEmptyString() {
        val json = ""
        val result = json.parseJson<Person>()
        assertNull("Parsing an empty string should return null.", result)
    }

    @Test
    fun testParseWhitespaceOnlyString() {
        val json = "   "
        val result = json.parseJson<Person>()
        assertNull("Parsing a whitespace-only string should return null.", result)
    }

    @Test
    fun testParseNullInput() {
        val json: String? = null
        // Since `parseJson` is an extension on `String`, handle nulls outside the function
        val result = json?.parseJson<Person>()
        assertNull("Parsing a null string should return null.", result)
    }

    // --------------------------------
    // Tests for Type Mismatches
    // --------------------------------

    @Test
    fun testParseJsonToIncorrectType() {
        val json = """{"fruits":["apple", "banana", "cherry"]}"""
        val expected =  Person(name = "", age = 0, city = "")
        val result = json.parseJson<Person>() // Attempting to parse FruitList JSON into Person
        assertEquals("Parsing JSON into an incorrect type should return null.", result, expected)
    }

    @Test
    fun testParseJsonWithMissingFields() {
        val json = """{"name":"Bob"}""" // Missing 'age' and 'city'
        val expected = Person(name = "Bob", age = 0, city = "")
        val result = json.parseJson<Person>()
        assertEquals(
            "Parsing JSON with missing fields should assign default values.",
            expected,
            result
        )
    }

    @Test
    fun testParseJsonWithExtraFields() {
        val json = """{"name":"Charlie", "age":28, "city":"Chicago", "country":"USA"}"""
        val expected = Person(name = "Charlie", age = 28, city = "Chicago")
        val result = json.parseJson<Person>()
        assertEquals(
            "Parsing JSON with extra fields should ignore them and parse known fields.",
            expected,
            result
        )
    }

    @Test
    fun testParseJsonWithIncorrectFieldTypes() {
        val json = """{"name":"Diana", "age":"twenty-five", "city":12345}"""
        val expected = Person(name = "Diana", age = 0, city = "12345")
        val result = json.parseJson<Person>()

        assertNull("Parsing JSON with incorrect field types ", result)
    }
}

data class Person(
    val name: String = "",
    val age: Int = 0,
    val city: String = ""
)

data class FruitList(
    val fruits: List<String>
)