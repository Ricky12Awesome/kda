package me.ricky.kda.bot.test

import me.ricky.kda.core.lang.getMessage
import me.ricky.kda.core.lang.initTranslations
import java.util.*

fun main() {
  initTranslations()

  println(Locale.US.getMessage("test", "{oof}" to "123"))
  println(Locale.ENGLISH.getMessage("test", "{oof}" to "123"))
  println(Locale.JAPAN.getMessage("test", "{oof}" to "123"))
}