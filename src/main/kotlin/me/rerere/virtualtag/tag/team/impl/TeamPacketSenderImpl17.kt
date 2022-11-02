package me.rerere.virtualtag.tag.team.impl

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.wrappers.WrappedChatComponent
import io.papermc.paper.adventure.PaperAdventure
import me.rerere.virtualtag.tag.VirtualTeam
import me.rerere.virtualtag.tag.team.TeamPacketSender
import me.rerere.virtualtag.util.*
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.util.*

class TeamPacketSenderImpl17 : TeamPacketSender {
    override fun createTeam(virtualTeam: VirtualTeam) {
        with(virtualTeam) {
            createPacket(PacketType.Play.Server.SCOREBOARD_TEAM) {
                // team name
                strings.writeSafely(0, name)

                // team mode
                integers.writeSafely(0, 0)

                // players
                getSpecificModifier(Collection::class.java).writeSafely(0, players)

                // team info
                optionalStructures.readSafely(0).get().apply {
                    chatComponents.apply {
                        // DisplayName
                        writeSafely(0, WrappedChatComponent.fromText(name))
                        // Prefix
                        writeSafely(1, prefix.toWrappedChatComponent())
                        // Suffix
                        writeSafely(2, suffix.toWrappedChatComponent())
                    }
                    strings.apply {
                        writeSafely(0, "always")
                        writeSafely(1, "never")
                    }
                    // color
                    getSpecificModifier(ChatFormatConverter.chatFormatClass).writeSafely(
                        0,
                        color.toNmsChatFormat()
                    )
                    // friendly tags
                    integers.writeSafely(0, 0x0)
                }
            }.broadcast()
        }
    }

    override fun createForPlayer(virtualTeam: VirtualTeam, player: Player) {
        with(virtualTeam){
            createPacket(PacketType.Play.Server.SCOREBOARD_TEAM) {
                // team name
                strings.writeSafely(0, name)

                // team mode
                integers.writeSafely(0, 0)

                // players
                getSpecificModifier(Collection::class.java).writeSafely(0, players)

                // team info
                optionalStructures.readSafely(0).get().apply {
                    chatComponents.apply {
                        // DisplayName
                        writeSafely(0, WrappedChatComponent.fromChatMessage(name)[0])
                        // Prefix
                        writeSafely(1, prefix.toWrappedChatComponent())
                        // Suffix
                        writeSafely(2, suffix.toWrappedChatComponent())
                    }
                    strings.apply {
                        writeSafely(0, "always")
                        writeSafely(1, "never")
                    }
                    // color
                    getSpecificModifier(ChatFormatConverter.chatFormatClass).writeSafely(
                        0,
                        color.toNmsChatFormat()
                    )
                    // friendly tags
                    integers.writeSafely(0, 0x0)
                }
            }.send(player)
        }
    }

    override fun destroyTeam(virtualTeam: VirtualTeam) {
        with(virtualTeam){
            createPacket(PacketType.Play.Server.SCOREBOARD_TEAM) {
                strings.writeSafely(0, name)
                integers.writeSafely(0, 1)
                optionalStructures.writeSafely(0, Optional.empty())
            }.broadcast()
        }
    }

    override fun addPlayer(virtualTeam: VirtualTeam, entities: Set<String>) {
        with(virtualTeam){
            createPacket(PacketType.Play.Server.SCOREBOARD_TEAM) {
                strings.writeSafely(0, name)
                integers.writeSafely(0, 3)
                getSpecificModifier(Collection::class.java).writeSafely(0, entities)
                optionalStructures.writeSafely(0, Optional.empty())
            }.broadcast()
        }
    }

    override fun removePlayer(virtualTeam: VirtualTeam, entities: Set<String>) {
        with(virtualTeam){
            createPacket(PacketType.Play.Server.SCOREBOARD_TEAM) {
                strings.writeSafely(0, name)
                integers.writeSafely(0, 4)
                getSpecificModifier(Collection::class.java).writeSafely(0, entities)
                optionalStructures.writeSafely(0, Optional.empty())
            }.broadcast()
        }
    }

    fun Component.toWrappedChatComponent(): WrappedChatComponent {
        val vanilla = PaperAdventure.asVanilla(this)
        return WrappedChatComponent.fromHandle(vanilla)
    }
}