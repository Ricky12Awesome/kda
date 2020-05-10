package me.ricky.kda.core

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent

/**
 * Kotlin Development API based on [JDA]
 */
class KDA private constructor(source: JDA) : JDA by source {
  /**
   * @param token required to start the bot
   * @param intents gateway intents for the bot. default: [GatewayIntent.DEFAULT]
   * @param builder applies to [JDABuilder]
   */
  constructor(
    token: String,
    intents: Collection<GatewayIntent> = GatewayIntent.getIntents(GatewayIntent.DEFAULT),
    builder: JDABuilder.() -> Unit = {}
  ) : this(JDABuilder.createDefault(token, intents).apply(builder).build())

  /**
   * @param builder applies to [JDABuilder], Need to set token and intents yourself or use another [KDA] constructor
   */
  constructor(builder: JDABuilder.() -> Unit) : this(JDABuilder().apply(builder).build())


}