val jdaVersion: String by rootProject
val jdaReactorVersion: String by rootProject
val logbackVersion: String by rootProject
val kmongoVersion: String by rootProject
val mongodbVersion: String by rootProject
val serializationVersion: String by rootProject
val coroutineVersion: String by rootProject

dependencies {
  // Kotlin
  api(kotlin("stdlib-jdk8"))
  api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion") {
    exclude("org.jetbrains.kotlin", "kotlin-stdlib")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-common")
  }

  api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion") {
    exclude("org.jetbrains.kotlin", "kotlin-stdlib")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-common")
  }

  // Other
  api("ch.qos.logback:logback-classic:$logbackVersion")

  // JDA
  api("club.minnced:jda-reactor:$jdaReactorVersion")
  api("net.dv8tion:JDA:$jdaVersion") {
    exclude("org.slf4j", "slf4j-api")
  }

  // Mongo
  api("org.mongodb:mongodb-driver-async:$mongodbVersion")
  api("org.litote.kmongo:kmongo-async:$kmongoVersion") {
    exclude("org.jetbrains.kotlin", "kotlin-stdlib")
  }
}