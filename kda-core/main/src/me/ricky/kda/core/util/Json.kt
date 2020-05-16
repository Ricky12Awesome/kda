package me.ricky.kda.core.util

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

/**
 * Merges [JsonObject] to form a new [JsonObject]
 *
 * Does not merge [JsonArray] together, it replaces them.
 *
 * @receiver Source to merge to
 * @param other Replaces or Adds to the source
 */
fun JsonObject.deepMerge(other: JsonObject): JsonObject {
  val content = (this + other).toMutableMap()

  other.forEach { (key, element) ->
    val sourceElement = this[key] ?: JsonObject(mapOf())

    if (element is JsonObject && sourceElement is JsonObject) {
      content[key] = sourceElement.deepMerge(element)
    }
  }

  return JsonObject(content)
}