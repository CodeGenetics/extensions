package com.codegenetics.extensions.extension

import org.junit.Test
import org.junit.Assert.*


class LongExtKtTest {



    @Test
    fun `Zero bytes input`() {
        // Test the function's behavior when the input is zero bytes. 
        //Should return '0.00 B'.
        assertEquals("0.00 B", 0L.toReadableByteCount())
    }

    @Test
    fun `Small byte value`() {
        // Test with a small number of bytes (e.g., less than 1 KB). 
        //Should return value in bytes with 'B' unit.
        assertEquals("512.00 B", 512L.toReadableByteCount())
        assertEquals("1.00 B", 1L.toReadableByteCount())
    }

    @Test
    fun `Kilobyte boundary`() {
        // Test at the boundary between bytes and kilobytes (e.g., 1024 bytes). 
        //Should return '1.00 KB'.
        assertEquals("1.00 KB", 1024L.toReadableByteCount())
    }

    @Test
    fun `Megabyte boundary`() {
        // Test at the boundary between kilobytes and megabytes (e.g., 1024 * 1024 bytes). 
        //Should return '1.00 MB'.
        assertEquals("1.00 MB", (1024L * 1024L).toReadableByteCount())
    }

    @Test
    fun `Gigabyte boundary`() {
        // Test at the boundary between megabytes and gigabytes (e.g., 1024 * 1024 * 1024 bytes). 
        //Should return '1.00 GB'.
        assertEquals("1.00 GB", (1024L * 1024L * 1024L).toReadableByteCount())
    }

    @Test
    fun `Terabyte boundary`() {
        // Test at the boundary between gigabytes and terabytes (e.g., 1024 * 1024 * 1024 * 1024 bytes).
        //Should return '1.00 TB'.
        assertEquals("1.00 TB", (1024L * 1024L * 1024L * 1024L).toReadableByteCount())
    }

    @Test
    fun `Petabyte boundary`() {
        // Test at the boundary between terabytes and petabytes (e.g., 1024^5 bytes). 
        //Should return '1.00 PB'.
        assertEquals("1.00 PB", (1024L * 1024L * 1024L * 1024L * 1024L).toReadableByteCount())
    }

    @Test
    fun `Exabyte boundary`() {
        // Test at the boundary between petabytes and exabytes (e.g., 1024^6 bytes).
        // Should return '1.00 EB'.
        assertEquals("1.00 EB", (1024L * 1024L * 1024L * 1024L * 1024L * 1024L).toReadableByteCount())
    }

    @Test
    fun `Large byte value`() {
        // Test with a very large number of bytes (e.g., close to the maximum long value). 
        //Should return the EB unit
        assertEquals("1023.00 EB", (1023L * 1024L * 1024L * 1024L * 1024L * 1024L).toReadableByteCount())
    }

    @Test
    fun `Values between units`() {
        // Test with values that fall between the unit boundaries (e.g., 512 bytes, 1536 bytes, 2.5 MB). 
        //Check for correct unit and 2 decimals
        assertEquals("512.00 B", 512L.toReadableByteCount())
        assertEquals("1.50 KB", 1536L.toReadableByteCount())
        assertEquals("2.50 MB", (2.5 * 1024 * 1024).toLong().toReadableByteCount())
        assertEquals("1.01 KB", 1034L.toReadableByteCount())
    }

    @Test
    fun `Negative bytes input`() {
        // Test the function's behavior when the input is negative. 
        //The function should not be allowed to be used with negative inputs
        assertThrows(IllegalArgumentException::class.java) { (-1L).toReadableByteCount() }
    }

    @Test
    fun `Max Long`() {
        // Test with the max value of a long. 
        //Should return value with EB unit.
        assertEquals("8.00 EB", Long.MAX_VALUE.toReadableByteCount())
    }

}