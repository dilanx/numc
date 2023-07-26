package com.dilanxd.mcp.numc

import org.bukkit.plugin.java.JavaPlugin
import com.dilanxd.mcp.numc.WhitelistCheck

class NUMC : JavaPlugin() {
    override fun onEnable() {
        this.server.pluginManager.registerEvents(WhitelistCheck(), this)
    }
}
