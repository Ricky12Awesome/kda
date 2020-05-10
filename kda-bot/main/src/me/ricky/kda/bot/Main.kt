package me.ricky.kda.bot

import club.minnced.jda.reactor.ReactiveEventManager
import club.minnced.jda.reactor.asMono
import club.minnced.jda.reactor.on
import me.ricky.kda.core.KDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.requests.restaction.MessageAction

fun main() {
  val token = System.getenv("BOT_TOKEN")
  val manager = ReactiveEventManager()
  val kda = KDA(token) {
    setEventManager(manager)
  }

  kda.on<GuildMessageReceivedEvent>()
    .map(GuildMessageReceivedEvent::getMessage)
    .filter { it.contentRaw == "!ping" }
    .map { it.channel.sendMessage("Pong") }
    .flatMap(MessageAction::asMono)
    .subscribe()
}