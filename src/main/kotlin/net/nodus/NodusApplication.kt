package net.nodus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NodusApplication

fun main(args: Array<String>) {
	runApplication<NodusApplication>(*args)
}
