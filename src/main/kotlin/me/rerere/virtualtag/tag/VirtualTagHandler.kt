package me.rerere.virtualtag.tag

import me.rerere.virtualtag.api.Tag
import org.bukkit.entity.Player

class VirtualTagHandler {
    val virtualTeams = hashSetOf<VirtualTeam>()
    private var nextTeamId = 0L

    // Generate a unique team name
    private fun generateTeamName(): String {
        return "virtualteam_${nextTeamId++}"
    }

    fun sendCurrentNameTags(player: Player) {
        virtualTeams.forEach {
            it.createForPlayer(player)
        }
    }

    fun setPlayerTag(player: Player, tag: Tag) {
        player.playerListName(null)

        val oldTeam = this.getPlayerCurrentTeam(player)
        oldTeam?.takeIf { it.tag != tag }?.removePlayer(player.name)
        val team = this.getVirtualTeamByTag(tag) ?: VirtualTeam(
            name = this.generateTeamName(),
            prefix = tag.prefix,
            suffix = tag.suffix,
            color = tag.color
        )
        team.recreate()
        team.addPlayer(player.name)
        virtualTeams += team
        this.cleanVirtualTeams()
    }

    fun removePlayerTag(player: Player) {
        this.getPlayerCurrentTeam(player)?.removePlayer(player.name)
        cleanVirtualTeams()
    }

    private fun getPlayerCurrentTeam(player: Player): VirtualTeam? = virtualTeams.find {
        it.players.contains(player.name)
    }

    fun getPlayerCurrentTag(player: Player): Tag? = virtualTeams.find {
        it.players.contains(player.name)
    }?.let {
        Tag(
            it.prefix,
            it.suffix,
            it.color,
        )
    }

    private fun cleanVirtualTeams() {
        virtualTeams.removeIf { team ->
            team.players.isEmpty().also {
                if (it) {
                    team.destroy()
                }
            }
        }
    }

    private fun getVirtualTeamByTag(tag: Tag): VirtualTeam? = virtualTeams.find {
        it.tag == tag
    }
}