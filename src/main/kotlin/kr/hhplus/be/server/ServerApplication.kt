package kr.hhplus.be.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableRetry
@EnableAsync
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}