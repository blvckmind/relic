package io.github.blvckmind.relic.service

import io.github.blvckmind.relic.RelicAddressBookApplication
import java.awt.*
import javax.imageio.ImageIO
import java.awt.MenuItem


/* https://stackoverflow.com/questions/40571199/creating-tray-icon-using-javafx */
object TrayIconObject {

    private var globalTrayIcon: TrayIcon? = null
    private var inited = false
    private const val APPLICATION_NAME = "Relic"

    fun initTrayIcon() {
        if (!SystemTray.isSupported() || inited) {
            return
        }
        inited = true
        val trayMenu = PopupMenu()

        val bootingUpItem = MenuItem("Booting up...")
        bootingUpItem.isEnabled = false
        trayMenu.add(bootingUpItem)

        trayMenu.addSeparator()

        val pic: Image = ImageIO.read(RelicAddressBookApplication::class.java.classLoader.getResourceAsStream("images/relic.png"))
        val trayIcon = TrayIcon(pic, APPLICATION_NAME, trayMenu)
        trayIcon.isImageAutoSize = true
        val tray = SystemTray.getSystemTray()
        try {
            tray.add(trayIcon)
            globalTrayIcon = trayIcon
        } catch (e: AWTException) {
            e.printStackTrace()
        }
    }

    fun get() = globalTrayIcon
}