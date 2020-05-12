package me.ricky.kda.core.command

import net.dv8tion.jda.api.entities.Guild
import java.util.*

/**
 * Manages data for [CommandManager] or anything else that depends on this
 *
 * - Prefixes
 * - Translation [Locale]
 */
interface ServerDataManager {
  /**
   * @param guild acts as the key to get the prefix
   */
  fun getPrefix(guild: Guild): String

  /**
   * @param guild acts as the key to get the [Locale]
   */
  fun getLocale(guild: Guild): Locale

  /**
   * @param guild acts as the key for the [prefix]
   * @param prefix acts as the value
   *
   * @return true if prefix was set successfully
   */
  fun setPrefix(guild: Guild, prefix: String): Boolean

  /**
   * @param guild acts as the key for the [locale]
   * @param locale acts as the value
   *
   * @return true if locale was set successfully
   */
  fun setLocale(guild: Guild, locale: Locale): Boolean
}

/**
 * Data will always be the same across all [Guild]s and cannot be changed.
 *
 * @param prefix for all [Guild]s
 * @param locale for all [Guild]s
 */
class ConstantServerDataManager(
  private val prefix: String = "!",
  private val locale: Locale = Locale.US
) : ServerDataManager {
  override fun getPrefix(guild: Guild): String = prefix
  override fun getLocale(guild: Guild): Locale = locale
  override fun setPrefix(guild: Guild, prefix: String): Boolean = false
  override fun setLocale(guild: Guild, locale: Locale): Boolean = false
}