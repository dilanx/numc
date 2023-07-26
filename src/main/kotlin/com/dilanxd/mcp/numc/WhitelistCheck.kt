package com.dilanxd.mcp.numc

import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.URI

class WhitelistCheck : Listener {

    @EventHandler
    fun onAsyncPlayerPreLoginEvent(event: AsyncPlayerPreLoginEvent) {
        val uuid = event.uniqueId.toString().replace("-", "")
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(
                URI.create("https://api.dilanxd.com/nu-discord/numc/whitelist?id=$uuid")
            )
            .build()

        val code = client
            .send(request, HttpResponse.BodyHandlers.ofString())
            .statusCode()

        if (code == 200) {
            event.allow()
            return
        }

        if (code == 403) {
            val component = LegacyComponentSerializer
                .legacyAmpersand()
                .deserialize(
                    "&5&lN U &f&lM C&r\n\n" +
                    "&cYou are not whitelisted.&7\n\n" +
                    "&7-========-&r\n\n" +
                    "&fTo add yourself to the whitelist, type &b/whitelist&f in any channel on the NU Discord server.&r\n\n" +
                    "&7-========-&r\n\n" +
                    "&7Not on the Discord server? Join at &9nudiscord.org&7."
                )

            event.disallow(Result.KICK_WHITELIST, component)
            return
        }

        event.disallow(
            Result.KICK_OTHER,
            Component.text("An error occurred while verifying your server access. Notify an administrator on Discord.")
        )
    }

}
