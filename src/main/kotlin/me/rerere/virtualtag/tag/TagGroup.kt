package me.rerere.virtualtag.tag

import net.kyori.adventure.text.Component

class TagGroup(
    val name: String,
    val permission: String,
    val priority: Int,
    val prefix: Component,
    val suffix: Component
)