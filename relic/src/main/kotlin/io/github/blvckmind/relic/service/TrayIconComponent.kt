package io.github.blvckmind.relic.service

import io.github.blvckmind.relic.config.ApplicationProperties
import io.github.blvckmind.relic.util.notNull
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Component
import java.awt.*
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.net.URI
import javax.annotation.PostConstruct
import kotlin.system.exitProcess

@Component
class TrayIconComponent(
    val applicationProperties: ApplicationProperties,
    val serverProperties: ServerProperties,
    val configurableApplicationContext: ConfigurableApplicationContext
) {

    @PostConstruct
    fun buildTrayMenu() {
        TrayIconObject.get().notNull {
            fillTrayIcon(it)
        }
    }

    fun fillTrayIcon(trayIcon: TrayIcon) {
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
            val linksMenu = Menu("Links")

            /* Help item */
            val helpItem = MenuItem("Help")
            helpItem.addActionListener {
                Desktop.getDesktop().browse(URI("https://github.com/blvckmind/relic/wiki"))
            }
            linksMenu.add(helpItem)

            /* Issues item */
            val issuesItem = MenuItem("Issues")
            issuesItem.addActionListener {
                Desktop.getDesktop().browse(URI("https://github.com/blvckmind/relic/issues"))
            }
            linksMenu.add(issuesItem)

            /* Contribute item */
            val contributeItem = MenuItem("Contribute")
            contributeItem.addActionListener {
                Desktop.getDesktop().browse(URI("https://github.com/blvckmind/relic"))
            }
            linksMenu.add(contributeItem)

            trayMenu.add(linksMenu)
        } else {
            /* Github item */
            val githubItem = MenuItem("github.com/blvckmind")
            githubItem.isEnabled = false
            trayMenu.add(githubItem)
        }

        /* ShowPassword item */
        val showPasswordItem = Menu("Password")
        trayMenu.add(showPasswordItem)

        /* ShowPassword: Password item */
        val password = applicationProperties.authorization.password
        val passwordItem = MenuItem("Copy to clipboard")
        passwordItem.addActionListener {
            val defaultToolkit = Toolkit.getDefaultToolkit()
            val clipboard: Clipboard = defaultToolkit.systemClipboard

            try {
                clipboard.setContents(StringSelection(password), null)
            } catch (e: Exception) {
                e.printStackTrace()
                passwordItem.isEnabled = false
                passwordItem.label = "'$password'";
            }
        }
        showPasswordItem.add(passwordItem)

        /* Separator */
        trayMenu.addSeparator()

        /* Shutdown item */
        val shutdownItem = MenuItem("Shutdown")
        shutdownItem.addActionListener {
            trayMenu.removeAll()

            shutdownItem.isEnabled = false
            shutdownItem.label = "Shutting down..."
            trayMenu.add(shutdownItem)

            SpringApplication.exit(configurableApplicationContext, ExitCodeGenerator { 0 })
            exitProcess(0)
        }
        trayMenu.add(shutdownItem)

        trayIcon.popupMenu = trayMenu
    }
}