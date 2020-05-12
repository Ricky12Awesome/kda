package me.ricky.kda.bot.test

import club.minnced.jda.reactor.ReactiveEventManager
import club.minnced.jda.reactor.asMono
import club.minnced.jda.reactor.on
import club.minnced.jda.reactor.toMono
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import me.ricky.kda.core.KDA
import me.ricky.kda.core.command.Command
import me.ricky.kda.core.command.CommandContext
import me.ricky.kda.core.command.CommandManager
import me.ricky.kda.core.command.CommandManagerTranslation
import me.ricky.kda.core.lang.Text
import me.ricky.kda.core.lang.loadTranslations
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

@Serializable
data class Translation(
  val test: Text = Text(),
  override val noCommand: Text = Text(),
  override val commands: Map<String, JsonElement>,
) : CommandManagerTranslation

object TestCommand : Command<TestCommand.Translation> {
  override val name: String = "test"
  override val translationSerializer: KSerializer<Translation> = Translation.serializer()

  override fun CommandContext<Translation>.execute() {
    channel.sendMessage(translation.message("{guild}" to guild.name)).queue()
  }

  @Serializable
  data class Translation(
    val message: Text
  )
}

fun main() {
  val translation = loadTranslations(Translation.serializer())
  val token = System.getenv("BOT_TOKEN")
    ?: error("Cannot start bot because no token was provided.")

  KDA(token) {
    val commandManager = CommandManager(translation)
    val eventManager = ReactiveEventManager()

    commandManager += TestCommand

    with(eventManager) {
      on<GuildMessageReceivedEvent>()
        .subscribe(commandManager::execute)
    }

    setEventManager(eventManager)
  }
}