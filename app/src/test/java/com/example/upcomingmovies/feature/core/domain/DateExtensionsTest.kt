package com.example.upcomingmovies.feature.core.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DateExtensionsTest {

    // region formatToDefaultDate

    @Test
    fun `formatToDefaultDate - valid date - returns dd slash MM slash yyyy format`() {
        // Given
        val input = "2023-04-12"

        // When
        val result = input.formatToDefaultDate()

        // Then
        assertEquals("12/04/2023", result)
    }

    @Test
    fun `formatToDefaultDate - first day of year - returns correct format`() {
        // Given
        val input = "1977-05-25"

        // When
        val result = input.formatToDefaultDate()

        // Then
        assertEquals("25/05/1977", result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `formatToDefaultDate - wrong separator format - throws IllegalArgumentException`() {
        // Given — slashes instead of dashes; "2023" matches yyyy but "/" ≠ "-" so parse fails
        val input = "2023/04/12"

        // When
        input.formatToDefaultDate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `formatToDefaultDate - random string - throws IllegalArgumentException`() {
        // Given
        val input = "not-a-date"

        // When
        input.formatToDefaultDate()
    }

    // endregion

    // region daysUntilRelease

    @Test
    fun `daysUntilRelease - far future date - returns positive number`() {
        // Given
        val futureDate = "2099-01-01"

        // When
        val result = futureDate.daysUntilRelease()

        // Then
        assertTrue("Expected positive days for future date, got $result", result > 0)
    }

    @Test
    fun `daysUntilRelease - far past date - returns negative number`() {
        // Given
        val pastDate = "2000-01-01"

        // When
        val result = pastDate.daysUntilRelease()

        // Then
        assertTrue("Expected negative days for past date, got $result", result < 0)
    }

    @Test
    fun `daysUntilRelease - far future date - difference is plausible`() {
        // Given a date guaranteed to be more than 1000 days away
        val futureDate = "2099-01-01"

        // When
        val result = futureDate.daysUntilRelease()

        // Then
        assertTrue(result > 1000)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `daysUntilRelease - invalid format - throws IllegalArgumentException`() {
        // Given
        val input = "01/01/2099"

        // When
        input.daysUntilRelease()
    }

    // endregion
}
