package me.rerere.virtualtag.tag

import me.rerere.virtualtag.api.Tag
import me.rerere.virtualtag.tag.team.teamPacketSender
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class VirtualTeam(
    val name: String,
    val prefix: Component,
    val suffix: Component,
    val color: ChatColor,
) {
    val players: MutableSet<String> = hashSetOf()

    val tag: Tag
        get() = Tag(prefix, suffix, color)

    fun recreate() {
        destroy()
        create()
    }

    fun addPlayer(player: String) {
        players += player
        teamPacketSender.addPlayer(this, setOf(player))
    }

    fun removePlayer(player: String) {
        players -= player
        teamPacketSender.removePlayer(this, setOf(player))
    }

    private fun create() {
        teamPacketSender.createTeam(this)
    }

    fun createForPlayer(player: Player) {
        teamPacketSender.createForPlayer(this, player)
    }

    fun destroy() {
        teamPacketSender.destroyTeam(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VirtualTeam

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}