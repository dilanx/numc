package com.dilanxd.mcp.numc

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Utility {
    companion object {
        fun sendMessage(player: Player, message: String) {
            player.sendMessage(
                LegacyComponentSerializer
                    .legacyAmpersand()
                    .deserialize(message)
            )
        }

        fun sendMessage(sender: CommandSender, message: String) {
            sender.sendMessage(
                LegacyComponentSerializer
                    .legacyAmpersand()
                    .deserialize(message)
            )
        }

        fun sendMessage(player: Player, message: List<String>) {
            for (msg in message) {
                player.sendMessage(
                    LegacyComponentSerializer
                        .legacyAmpersand()
                        .deserialize(msg)
                )
            }
        }

        fun sendMessage(sender: CommandSender, message: List<String>) {
            for (msg in message) {
                sender.sendMessage(
                    LegacyComponentSerializer
                        .legacyAmpersand()
                        .deserialize(msg)
                )
            }
        }
    }
}
