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
            .cancelOn { it.player.name == "cancelled name" } // will be kicked for "Event cancelled" if the name equals to "cancelled name"
            .cancelWithMessage("your name contains illegal characters") {
                // will kick the player for "your name contains illegal characters" if the player's name contains "N"
                it.player.name.contains("N")
            }
            .on {
                // this message will only be sent if the player has allowFlight.
                // this on block will still be called even if the player's name is "cancelled name".
                it.player.sendMessage("You are able to fly!")
            }

        Events
            .listenTo<BlockBreakEvent>()
            .cancelOn {
                // this will cancel the event if the player does not have the permission "events.break"
                !it.player.hasPermission("events.break")
            }
            .cancelWithMessage("you may not break blocks at Y level 50") {
                // will cancel the event if the Y coordinate is 50, and will
                // send the player "you may not break blocks at Y level 50"
                it.block.location.y == 50.0
            }

        listOf(
            BlockPlaceEvent::class.java,
            BlockBreakEvent::class.java
        ).forEach { type ->
            Events
                .listenTo(type)
                .filter { it.block.type == Material.ACACIA_DOOR }
                .on {
                    // this will change the block type on BlockBreakEvent or BlockPlaceEvent if the type is ACACIA_DOOR
                    it.block.type = Material.TRAPPED_CHEST
                }
        }
    }
}