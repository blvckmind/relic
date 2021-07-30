package io.github.blvckmind.relic.service

import io.github.blvckmind.relic.util.notNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Component
import java.awt.*
import javax.annotation.PostConstruct
import kotlin.system.exitProcess
import java.net.URI
import org.springframework.boot.SpringApplication

@Component
class TrayIconComponent(
    val serverProperties: ServerProperties,
    val configurableApplicationContext: ConfigurableApplicationContext,
    @Value("\${spring.security.user.password}") private val password: String
) {

    @PostConstruct
    fun buildTrayMenu() {

        TrayIconObject.get().notNull {
            fillTrayIcon(it)
            it.displayMessage("Relic", "Application started", TrayIcon.MessageType.NONE)
        }

    }

    fun fillTrayIcon(trayIcon: TrayIcon){
        val trayMenu = trayIcon.popupMenu
        trayMenu.removeAll()

        /* Open item */
        val port = serverProperties.port
        val openItem: MenuItem
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            openItem = MenuItem("Open")
            openItem.addActionListener {
                Desktop.getDesktop().browse(URI("http://localhost:$port"))
            }
        } else {
            openItem = MenuItem("URL: localhost:$port")
            openItem.isEnabled = false
        }
        trayMenu.add(openItem)

        /* Separator */
        trayMenu.addSeparator()

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            /* Help item */
            val helpItem = MenuItem("Help")
            helpItem.addActionListener {
                Desktop.getDesktop().browse(URI("https://github.com/blvckmind/relic/wiki"))
            }
            trayMenu.add(helpItem)

            /* Issues item */
            val issuesItem = MenuItem("Issues")
            issuesItem.addActionListener {
                Desktop.getDesktop().browse(URI("https://github.com/blvckmind/relic/issues"))
            }
            trayMenu.add(issuesItem)

            /* Contribute item */
            val contributeItem = MenuItem("Contribute")
            contributeItem.addActionListener {
                Desktop.getDesktop().browse(URI("https://github.com/blvckmind/relic"))
            }
            trayMenu.add(contributeItem)
        } else {
            /* Github item */
            val githubItem = MenuItem("github.com/blvckmind")
            githubItem.isEnabled = false
            trayMenu.add(githubItem)
        }

        /* Separator */
        trayMenu.addSeparator()

        /* ShowPassword item */
        val showPasswordItem = Menu("Show Password")
        trayMenu.add(showPasswordItem)

        /* ShowPassword: Password item */
        val passwordItem = MenuItem("'$password'")
        passwordItem.isEnabled = false
        showPasswordItem.add(passwordItem)

        /* Separator */
        trayMenu.addSeparator()

        /* Shutdown item */
        val shutdownItem = MenuItem("Shutdown")
        shutdownItem.addActionListener {
            shutdownItem.isEnabled = false
            shutdownItem.label = "Shutting down..."
            SpringApplication.exit(configurableApplicationContext, ExitCodeGenerator { 0 })
            exitProcess(0)
        }
        trayMenu.add(shutdownItem)

        trayIcon.popupMenu = trayMenu
    }
}