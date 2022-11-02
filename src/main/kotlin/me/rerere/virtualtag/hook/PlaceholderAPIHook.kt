package me.rerere.virtualtag.hook

import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.replacer.CharsReplacer
import me.clip.placeholderapi.replacer.Replacer
import me.rerere.virtualtag.api.Tag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TranslatableComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object PlaceholderAPIHook {
    val enable = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null

    val replacerPercent: Replacer = CharsReplacer(Replacer.Closure.PERCENT)

    fun applyPlaceholder(player: Player, text: Component): Component {
        return when (text) {
            is TextComponent -> text.content(PlaceholderAPI.setPlaceholders(player, text.content()))
            is TranslatableComponent -> text.args(text.args().map { applyPlaceholder(player, it) })
            else -> text
        }.let { component -> component.children(component.children().map { applyPlaceholder(player, it) }) }
    }

}

fun Tag.applyPlaceholderAPI(player: Player) = if (PlaceholderAPIHook.enable) {
    this.apply {
        prefix = PlaceholderAPIHook.applyPlaceholder(player, prefix)
        suffix = PlaceholderAPIHook.applyPlaceholder(player, suffix)
    }
} else this