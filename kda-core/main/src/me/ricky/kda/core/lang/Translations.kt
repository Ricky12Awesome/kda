package me.ricky.kda.core.lang

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonObject
import me.ricky.kda.core.util.deepMerge
import me.ricky.kda.core.util.replaceAll
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * Serializes [Text] as a primitive [String]
 */
@Serializer(Text::class)
object MessageSerializer : KSerializer<Text> {
  override val descriptor: SerialDescriptor = PrimitiveDescriptor(
    serialName = "me.ricky.kda.core.lang.Message",
    kind = PrimitiveKind.STRING
  )

  override fun serialize(encoder: Encoder, value: Text) {
    encoder.encodeString(value.text)
  }

  override fun deserialize(decoder: Decoder): Text {
    return Text(decoder.decodeString())
  }
}

/**
 * @param text Raw text with unfilled arguments to fill arguments use [Text.invoke]
 */
@Serializable(MessageSerializer::class)
class Text(val text: String = "") {
  /**
   * Fills out arguments for the given [text]
   */
  operator fun invoke(vararg args: Pair<String, String>, ignoreCase: Boolean = false): String {
    return text.replaceAll(*args, ignoreCase = ignoreCase)
  }

  override fun toString(): String {
    return text
  }
}

/**
 * @param source Source of the translations
 * @param default Default Locale if no other translations exits
 */
class Translations<T>(
  private val source: Map<Locale, T>,
  private val default: Locale
) : Map<Locale, T> by source {
  override fun get(key: Locale): T {
    return source[key]
      ?: source[default]
      ?: error("No Translations are available.")
  }
}

/**
 * @param serializer Serializer for [T] where the translations will go to
 * @param directory [Path] to load the translations, looks for files like `en-US.json`
 * or anything that matches "${[Locale.toLanguageTag]}.json"
 * @param default Default locale for [Translations]
 */
fun <T> loadTranslations(
  serializer: KSerializer<T>,
  directory: Path = Paths.get("./lang"),
  default: Locale = Locale.US
): Translations<T> {
  fun Locale.getFileName() = "${toLanguageTag()}.json"

  val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
  val translations = mutableMapOf<Locale, T>()
  val defaultPath = directory.resolve(default.getFileName())

  if (Files.notExists(defaultPath)) {
    error("$defaultPath not found.")
  }

  val defaultJson = Files.newBufferedReader(defaultPath).use {
    json.parse(JsonObject.serializer(), it.readText())
  }

  val defaultTranslation = json.fromJson(serializer, defaultJson)

  Locale.getAvailableLocales().forEach { locale ->
    translations[locale] = defaultTranslation

    val path = directory.resolve(locale.getFileName())

    if (Files.exists(path)) {
      val localeJson = Files.newBufferedReader(path).use {
        json.parse(JsonObject.serializer(), it.readText())
      }

      translations[locale] = json.fromJson(serializer, defaultJson.deepMerge(localeJson))
    }
  }

  return Translations(translations, default)
}