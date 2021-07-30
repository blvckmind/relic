package io.github.blvckmind.relic

import io.github.blvckmind.relic.service.TrayIconObject
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class RelicAddressBookApplication

fun main(args: Array<String>) {
	TrayIconObject.initTrayIcon()
	runApplication<RelicAddressBookApplication>(*args)
}
