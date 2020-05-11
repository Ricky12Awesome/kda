package me.ricky.kda.core.util

import java.nio.file.Path

val Path.name inline get() = fileName.toString()