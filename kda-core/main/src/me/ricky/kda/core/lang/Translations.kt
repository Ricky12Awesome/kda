package me.ricky.kda.core.lang

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import me.ricky.kda.core.util.name
import me.ricky.kda.core.util.replaceAll
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.streams.asSequence

/**
 * Serializes [Message] as a primitive [String]
 */
@Serializer(Message::class)
object MessageSerializer : KSerializer<Message> {
  override val descriptor: SerialDescriptor = PrimitiveDescriptor(
    serialName = "me.ricky.kda.core.lang.Message",
    kind = PrimitiveKind.STRING
  )

  override fun serialize(encoder: Encoder, value: Message) {
    encoder.encodeString(value.message)
  }

  override fun deserialize(decoder: Decoder): Message {
    return Message(decoder.decodeString())
  }
}

/**
 * @param message Raw message with unfilled arguments to fill arguments use [Message.invoke]
 */
@Serializable(MessageSerializer::class)
class Message(val message: String) {
  /**
   * Fills out arguments for the given [message]
   */
  operator fun invoke(vararg args: Pair<String, String>, ignoreCase: Boolean = false): String {
    return message.replaceAll(*args, ignoreCase = ignoreCase)
  }

  override fun toString(): String {
    return message
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
  val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
  val translations = mutableMapOf<Locale, T>()
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
        translations[locale] = json.parse(serializer, it.readText())
      }
    }

  return Translations(translations, default)
}