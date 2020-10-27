package utils

import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.*

class TypedConfig(filename: String) {

    val properties = Properties()

    init {
        val fileInputStream = FileInputStream(filename)
        properties.load(fileInputStream)
    }

    inline fun <reified T : Any> get(name: String): T {
        val property = properties.getProperty(name) ?: error("Property '$name' not found")
        return when (T::class) {
            String::class -> property
            Int::class -> property.toInt()
            Double::class -> property.toDouble()
            else -> throw ClassCastException("Could not cast value '$property' to class '${T::class.simpleName}'")
        } as T
    }

}