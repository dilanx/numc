package com.dilanxd.mcp.numc

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.command.CommandSender
import com.dilanxd.mcp.numc.WhitelistCheck
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.command.Command
import org.bukkit.entity.Player
import com.dilanxd.mcp.numc.Utility.Companion.sendMessage

class NUMC : JavaPlugin() {
    override fun onEnable() {
        this.server.pluginManager.registerEvents(WhitelistCheck(), this)
        this.saveDefaultConfig()
    }

    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String>
    ): Boolean {

        if (cmd.name.equals("numc", true)) {
            if (args.isEmpty()) {
                sendMessage(sender, listOf(
                    " ",
                    "&5&lThe Northwestern Minecraft Server",
                    "&7Plugin version: &f${pluginMeta.version}",
                    "&7Author: &fDilan N (dilanxd.com)",
                    " "
                ))
                return true
            }

            if (args[0].equals("reload", true)
                || args[0].equals("r", true)) {
                if (!sender.hasPermission("numc.reload")) {
                    sendMessage(sender, "&cYou do not have permission to use this command.")
                    return true
                }

                this.reloadConfig()
                sendMessage(sender, "&7The NUMC configuration file has been reloaded successfully.")
                return true
            }

            sendMessage(sender, "&7Unknown arguments for &f/numc&7.")
            return true
        }

        if (cmd.name.equals("admins", true)) {
            val admins = config.getMapList("admins")
            if (admins.isEmpty()) {
                sendMessage(sender, "&7This server does not have any administrators listed.")
                return true
            }

            sendMessage(sender, listOf(
                " ",
                "&e&lAdministrators",
                "&5The Northwestern Minecraft Server",
                " ",
                *admins.map { admin ->
                    val minecraft = admin["minecraft"] as String
                    val online = server.getPlayer(minecraft) != null
                    val minecraftText = if (online) "&a${minecraft}&7" else "&c${minecraft}&7"
                    val discordText = "&9${admin["discord"]}&7"

                    "&e${admin["name"]}&7 (MC: $minecraftText | Discord: $discordText)"
                }.toTypedArray(),
                " "
            ))
            return true
        }

        return true
    }
}
