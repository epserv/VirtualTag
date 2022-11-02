package me.rerere.virtualtag.util

import com.comphenix.protocol.utility.MinecraftReflection
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.ChatColor

object ChatFormatConverter {
    val chatFormatClass = MinecraftReflection.getMinecraftClass("EnumChatFormat") as Class<Any>
    val chatFormatResetFields: Array<Any> = chatFormatClass.enumConstants
}

fun ChatColor.toNmsChatFormat() = ChatFormatConverter.chatFormatResetFields[this.ordinal]

fun lastChatColor(text: String) : ChatColor {
    for(index in text.indices.reversed()){
        if(text[index] != ChatColor.COLOR_CHAR){
            val color = ChatColor.getByChar(text[index])
            color?.let {
                if(it.isColor || it == ChatColor.RESET){
                    return it
                }
            }
        }
    }
    return ChatColor.RESET
}

fun ChatColor.toNamedTextColor(): NamedTextColor? = NamedTextColor.NAMES.value(this.name)

fun NamedTextColor?.toChatColor(): ChatColor = if (this == null) ChatColor.RESET else ChatColor.valueOf(this.toString().uppercase())

fun Component.toLegacyString(): String = LegacyComponentSerializer.legacySection().serialize(this)