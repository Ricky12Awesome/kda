package me.ricky.kda.core.lang

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import me.ricky.kda.core.util.name
import me.ricky.kda.core.util.replaceAll
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.streams.asSequence

lateinit var lang: LanguageMap
  private set

fun initTranslations(
  directory: Path = Paths.get("./lang"),
  default: Locale = Locale.US
) {
  val json = Json(JsonConfiguration.Stable)
  val initLang = mutableMapOf<Locale, Map<String, String>>()
  val serializer = MapSerializer(String.serializer(), String.serializer())
  val locales = Locale
    .getAvailableLocales()
    .associateBy {
      "${it.toLanguageTag()}.json"
    }

  Files.list(directory)
    .asSequence()
    .filter { locales[it.name] != null }
    .forEach { path ->
      val locale = locales.getValue(path.name)

      Files.newBufferedReader(path).use {
        initLang[locale] = json.parse(serializer, it.readText())
      }
    }

  lang = LanguageMap(initLang, default)
}

class LanguageMap(
  private val source: Map<Locale, Map<String, String>>,
  private val default: Locale = Locale.US
) : Map<Locale, Map<String, String>> by source {
  override operator fun get(key: Locale): Map<String, String> {
    return source[key] ?: source[default] ?: emptyMap()
  }
}

fun Locale.getMessage(
  key: String,
  args: Map<String, String>,
  ignoreCase: Boolean = false
): String {
  return lang[this][key]?.replaceAll(args, ignoreCase) ?: key
}

fun Locale.getMessage(
  key: String,
  args: List<Pair<String, String>>,
  ignoreCase: Boolean = false
): String {
  return lang[this][key]?.replaceAll(args, ignoreCase) ?: key
}

fun Locale.getMessage(
  key: String,
  vararg args: Pair<String, String>,
  ignoreCase: Boolean = false
): String {
  return lang[this][key]?.replaceAll(*args, ignoreCase = ignoreCase) ?: key
}
