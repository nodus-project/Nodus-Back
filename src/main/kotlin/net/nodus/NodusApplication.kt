package net.nodus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class NodusApplication

fun main(args: Array<String>) {
	runApplication<NodusApplication>(*args)
}
