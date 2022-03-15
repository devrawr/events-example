package io.github.devrawr.events

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class EventsExamplePlugin : JavaPlugin()
{
    override fun onEnable()
    {
        Events
            .withPlugin(this)
            .listenTo<PlayerJoinEvent>()
            .filter { it.player.allowFlight }
            .on {
                it.player.sendMessage("You are able to fly!")
            }

        Events
            .listenTo<BlockBreakEvent>()
            .cancelOn {
                !it.player.hasPermission("events.break")
            }

        listOf(
            BlockPlaceEvent::class.java,
            BlockBreakEvent::class.java
        ).forEach { type ->
            Events
                .listenTo(type)
                .filter { it.block.type == Material.ACACIA_DOOR }
                .on {
                    it.block.type = Material.AIR
                }
        }
    }
}