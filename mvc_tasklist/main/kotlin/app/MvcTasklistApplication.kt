package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MvcTasklistApplication

fun main(args: Array<String>) {
	runApplication<MvcTasklistApplication>(*args)
}
