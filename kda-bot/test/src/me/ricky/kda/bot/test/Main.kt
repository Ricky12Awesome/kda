package me.ricky.kda.bot.test

import kotlinx.serialization.Serializable
import me.ricky.kda.core.lang.Message
import me.ricky.kda.core.lang.loadTranslations

@Serializable
data class Translation(
  val test: Message,
  val commands: CommandTranslation
)

@Serializable
data class CommandTranslation(
  val test: TestCommandTranslation
)

@Serializable
data class TestCommandTranslation(
  val message: Message
)

fun main() {
  val translations = loadTranslations(Translation.serializer())

  translations.values.forEach {
    println(it.test("{text}" to "123 ABC"))
    println(it.commands.test.message)
  }
}