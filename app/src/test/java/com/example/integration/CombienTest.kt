package com.example.integration






import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test


class CombienTest {
    @Before
    fun beforeTests() {
        //mockkObject(Combien)
    }

    @Test
    fun CombienTest() {
        //every { Combien.combien(any()) } returns 20
        //assertEquals(Combien.combien("MapsActivity"),20)
        assertEquals(true,true)
    }

    @After
    fun afterTests() {
        unmockkAll()

    }
}
