val jdaVersion: String by rootProject
val jdaReactorVersion: String by rootProject
val logbackVersion: String by rootProject
val kmongoVersion: String by rootProject
val mongodbVersion: String by rootProject

dependencies {
  api(kotlin("stdlib-jdk8"))
  api("net.dv8tion:JDA:$jdaVersion")
  api("club.minnced:jda-reactor:$jdaReactorVersion")
  api("ch.qos.logback:logback-classic:$logbackVersion")
  api("org.litote.kmongo:kmongo-async:$kmongoVersion")
  api("org.mongodb:mongodb-driver-async:$mongodbVersion")
}