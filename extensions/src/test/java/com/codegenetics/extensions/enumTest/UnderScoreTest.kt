package com.codegenetics.extensions.enumTest

import com.codegenetics.extensions.extension.underscore
import org.junit.Assert.assertEquals
import org.junit.Test

enum class EventEnums {
    USER_LOGIN,
    USER_LOGOUT,
    ERROR_OCCURRED
}

enum class Severity {
    LOW,
    MEDIUM,
    HIGH
}

class UnderScoreTest {

    @Test
    fun testEnumReceiver_withEnumParameter() {
        val event1 = EventEnums.USER_LOGIN
        val event2 = EventEnums.USER_LOGOUT
        val result = event1 underscore event2
        assertEquals("USER_LOGIN_USER_LOGOUT", result)
    }

    @Test
    fun testEnumReceiver_withStringParameter() {
        val event = EventEnums.ERROR_OCCURRED
        val status = "critical"
        val result = event underscore status
        assertEquals("ERROR_OCCURRED_critical", result)
    }

    @Test
    fun testStringReceiver_withEnumParameter() {
        val receiver = "system"
        val severity = Severity.HIGH
        val result = receiver underscore severity
        assertEquals("system_HIGH", result)
    }

    @Test
    fun testStringReceiver_withStringParameter() {
        val receiver = "admin"
        val description = "access_granted"
        val result = receiver underscore description
        assertEquals("admin_access_granted", result)
    }

    @Test
    fun testEnumReceiver_withDifferentTypeParameter() {
        val event = Severity.MEDIUM
        val code = 404
        val result = event underscore code
        assertEquals("MEDIUM_404", result)
    }

    @Test
    fun testEnumReceiver_withAnotherEnumParameter() {
        val severity = Severity.LOW
        val event = EventEnums.USER_LOGIN
        val result = severity underscore event
        assertEquals("LOW_USER_LOGIN", result)
    }

    @Test
    fun testStringReceiver_withNonEnumNonStringParameter() {
        val receiver = "process"
        val number = 12345
        val result = receiver underscore number
        assertEquals("process_12345", result)
    }


}