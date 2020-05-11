package me.ricky.kda.core.util

/**
 * @param with from-to values to replace
 * @param ignoreCase ignore case
 *
 * @return new [String] with everything replaced by [with]
 */
fun String.replaceAll(with: Map<String, String>, ignoreCase: Boolean = false): String {
  return replaceAll(with.toList(), ignoreCase)
}

/**
 * @param with from-to values to replace
 * @param ignoreCase ignore case
 *
 * @return new [String] with everything replaced by [with]
 */
fun String.replaceAll(with: List<Pair<String, String>>, ignoreCase: Boolean = false): String {
  return with.fold(this) { str, (from, to) ->
    str.replace(from, to, ignoreCase)
  }
}

/**
 * @param with from-to values to replace
 * @param ignoreCase ignore case
 *
 * @return new [String] with everything replaced by [with]
 */
fun String.replaceAll(vararg with: Pair<String, String>, ignoreCase: Boolean = false): String {
  return with.fold(this) { str, (from, to) ->
    str.replace(from, to, ignoreCase)
  }
}