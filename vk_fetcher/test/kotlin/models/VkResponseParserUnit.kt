package utils

import kotlinx.serialization.SerializationException
import models.VkResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal object VkResponseParserUnit {

    @Test
    fun testParses() {
        assertDoesNotThrow {
            val response = VkResponse.parse("{\"response\":{\"total_count\":10}}")
            assertEquals(response.postsCount, 10)
        }
    }

    @Test
    fun testUnknownKeys() {
        assertDoesNotThrow {
            val response = VkResponse.parse("{\"response\":{\"xx\":\"yy\",\"total_count\":20}}")
            assertEquals(response.postsCount, 20)
        }
    }

    @Test
    fun testInvalid() {
        assertThrows(SerializationException::class.java) {
            VkResponse.parse("{\"response\":{\"total_count\":\"abc\"}}")
        }
    }

}