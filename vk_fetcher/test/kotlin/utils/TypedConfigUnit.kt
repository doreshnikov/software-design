package utils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.FileNotFoundException

internal object TypedConfigUnit {

    @Test
    fun testReal() {
        assertDoesNotThrow {
            val props = TypedConfig("test/resources/test.properties")

            assertEquals(props.get<String>("testProp1"), "testValue1")
            assertEquals(props.get<String>("empty_prop"), "")
            assertEquals(props.get<String>("multi.path.prop"), "testValue2")

            assertEquals(props.get<Int>("int_prop"), 120)
            assertEquals(props.get<Double>("double_prop"), 177.013);
        }
    }

    @Test
    fun testRealFails() {
        lateinit var props: TypedConfig
        assertDoesNotThrow {
            props = TypedConfig("test/resources/test.properties")
        }
        assertThrows(IllegalStateException::class.java) {
            props.get("no such prop")
        }
        assertThrows(ClassCastException::class.java) {
            props.get<Exception>("empty_prop")
        }
        assertThrows(NumberFormatException::class.java) {
            props.get<Int>("double_prop")
        }
    }

    @Test
    fun testFake() {
        lateinit var props: TypedConfig
        assertThrows(FileNotFoundException::class.java) {
            props = TypedConfig("test/resources/no.properties")
        }
        assertThrows(UninitializedPropertyAccessException::class.java) {
            props.get("any_prop")
        }
    }

}