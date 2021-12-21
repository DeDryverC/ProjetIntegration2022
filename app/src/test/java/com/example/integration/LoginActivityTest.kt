package com.example.integration




import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import io.mockk.mockk

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test




    class LoginActivityTest{
        @Test
        fun LoginTest2() {
            var loginMocked = mockk<LoginActivity>()
            every { loginMocked.login() } returns true
            every { loginMocked.showUserProfile() } returns "OK"
            assertEquals( loginMocked.login(),true)
            assertEquals( loginMocked.showUserProfile(),"OK")
        }
        @Test
        fun LoginTest() {
            var loginMocked = mockk<LoginActivity>(relaxed = true)
            val result = loginMocked.login()
            assertEquals( result,false)

        }
    }
