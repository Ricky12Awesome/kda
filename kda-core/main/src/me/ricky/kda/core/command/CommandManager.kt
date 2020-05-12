package me.ricky.kda.core.command

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

interface Command

class CommandManager(
  val serverDataManager: ServerDataManager = ConstantServerDataManager()
) : MutableMap<String, Command> by mutableMapOf() {
  fun execute(event: GuildMessageReceivedEvent) {
    val prefix = serverDataManager.getPrefix(event.guild)
    val args = event.message.contentRaw.split(" ")


  }
}