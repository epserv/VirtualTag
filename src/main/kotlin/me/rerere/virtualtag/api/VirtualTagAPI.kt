package me.rerere.virtualtag.api

import me.rerere.virtualtag.util.toChatColor
import me.rerere.virtualtag.virtualTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.ChatColor
import org.bukkit.entity.Player

@Suppress("unused")
object VirtualTagAPI {
    @JvmStatic
    fun setPlayerTag(player: Player, prefix: Component, suffix: Component, color: ChatColor) {
        virtualTag().tagHandler.setPlayerTag(
            player, Tag(
                prefix = prefix,
                suffix = suffix,
                color = color,
            )
        )
    }
    @JvmStatic
    fun setPlayerTag(player: Player, prefix: Component, suffix: Component, color: NamedTextColor?) =
        setPlayerTag(player, prefix, suffix, color.toChatColor())

    @JvmStatic
    fun removePlayerTag(player: Player) {
        virtualTag().tagHandler.removePlayerTag(player)
    }

    @JvmStatic
    fun getPlayerCurrentTag(player: Player): Tag? = virtualTag().tagHandler.getPlayerCurrentTag(player)
}