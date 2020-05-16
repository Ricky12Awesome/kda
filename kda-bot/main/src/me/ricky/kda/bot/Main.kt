package me.ricky.kda.bot

import club.minnced.jda.reactor.ReactiveEventManager
import club.minnced.jda.reactor.asMono
import club.minnced.jda.reactor.on
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import me.ricky.kda.core.KDA
import me.ricky.kda.core.command.CommandManager
import me.ricky.kda.core.command.CommandManagerTranslation
import me.ricky.kda.core.lang.Text
import me.ricky.kda.core.lang.loadTranslations
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.requests.restaction.MessageAction

@Serializable
data class Translations(
  override val noCommand: Text = Text(),
  override val commands: Map<String, JsonElement>,
) : CommandManagerTranslation

fun main() {
  val token = System.getenv("BOT_TOKEN")
    ?: error("Cannot start bot because no token was provided.")

  KDA(token) {
    val translations = loadTranslations(Translations.serializer())
    val commandManager = CommandManager(translations)
    val eventManager = ReactiveEventManager()

    with(eventManager) {

      on<GuildMessageReceivedEvent>()
        .subscribe(commandManager::execute)

      on<GuildMessageReceivedEvent>()
        .map(GuildMessageReceivedEvent::getMessage)
        .filter { it.contentRaw == "!ping" }
        .map { it.channel.sendMessage("Pong") }
        .flatMap(MessageAction::asMono)
        .subscribe()

      setEventManager(this)
    }
  }
}