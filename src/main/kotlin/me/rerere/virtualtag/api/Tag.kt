package me.rerere.virtualtag.api

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor

data class Tag(
    var prefix: Component,
    var suffix: Component,
    var color: ChatColor,
)
