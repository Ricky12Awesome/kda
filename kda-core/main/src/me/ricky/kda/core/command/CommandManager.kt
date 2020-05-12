package me.ricky.kda.core.command

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonElement
import me.ricky.kda.core.lang.Text
import me.ricky.kda.core.lang.Translations
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

interface CommandManagerTranslation {
  val noCommand: Text

  val commands: Map<String, JsonElement>
}

open class CommandContext<T>(
  from: GuildMessageReceivedEvent,
  val translation: T,
) : GuildMessageReceivedEvent(from.jda, from.responseNumber, from.message)

interface Command<T> {
  val name: String
  val translationSerializer: KSerializer<T>

  fun executeWith(context: CommandContext<T>) = context.execute()
  fun CommandContext<T>.execute()
}

class CommandManager<T : CommandManagerTranslation>(
  private val translation: Translations<T>,
  private val serverDataManager: ServerDataManager = ConstantServerDataManager(),
  private val json: Json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
) : MutableMap<String, Command<*>> by mutableMapOf() {
  fun execute(event: GuildMessageReceivedEvent) {
    val prefix = serverDataManager.getPrefix(event.guild)
    val locale = serverDataManager.getLocale(event.guild)
    val args = event.message.contentRaw.split(" ")
    val name = args[0].removePrefix(prefix)
    val command = this[name] ?: return
    val commandTranslations = translation[locale].commands
    val rawTranslation = commandTranslations[command.name]
      ?: error("No Translation for ${command.name}")

    event.execute(command, rawTranslation)
  }

  private fun <T> GuildMessageReceivedEvent.execute(command: Command<T>, rawTranslation: JsonElement) {
    val translation = json.fromJson(command.translationSerializer, rawTranslation)
    val context = CommandContext(this, translation)

    command.executeWith(context)
  }

  operator fun plusAssign(command: Command<*>) {
    this[command.name] = command
  }
}