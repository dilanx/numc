package com.dilanxd.mcp.numc

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

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

        val res = client
            .send(request, HttpResponse.BodyHandlers.ofString())
        val code = res.statusCode()
        val body: JSONObject = JSONParser().parse(res.body()) as JSONObject

        if (code == 200) {
            event.allow()
            return
        }

        if (code == 403 || code == 404) {
            val component: TextComponent
            if (body["state"] != "open") {
                component = LegacyComponentSerializer
                    .legacyAmpersand()
                    .deserialize(
                        listOf(
                            "&5&lNU&f&lMC&r",
                            "&cThe server is currently ${body["state"]}.",
                            "&7-========-&r",
                            "&f${body["reason"]}",
                            "&7-========-&r",
                            "&7Not on the Discord server? Join at &9nudiscord.org&7."
                        ).joinToString("\n\n")
                    )
            } else {
                component = LegacyComponentSerializer
                    .legacyAmpersand()
                    .deserialize(
                        listOf(
                            "&5&lNU&f&lMC&r",
                            "&cYou are not whitelisted.&r",
                            "&7-========-&r",
                            "&fTo add yourself to the whitelist, type &b/whitelist&f in any channel on the NU Discord server.&r",
                            "&7-========-&r",
                            "&7Not on the Discord server? Join at &9nudiscord.org&7."
                        ).joinToString("\n\n")
                    )
            }
            event.disallow(Result.KICK_WHITELIST, component)
            return
        }

        event.disallow(
            Result.KICK_OTHER,
            Component.text("An error occurred while verifying your server access. Notify an administrator on Discord.")
        )
    }

}
