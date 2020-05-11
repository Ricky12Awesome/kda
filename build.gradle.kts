import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlinx.serialization.gradle.SerializationGradleSubplugin

plugins {
  kotlin("jvm") version "1.4-M1"
  kotlin("plugin.serialization") version "1.4-M1"
  maven
}

allprojects {
  apply<KotlinPluginWrapper>()
  apply<SerializationGradleSubplugin>()
  apply<MavenPlugin>()

  repositories {
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    jcenter()
    mavenCentral()
  }

  kotlin.sourceSets["main"].kotlin.srcDir("./main/src/")
  sourceSets["main"].resources.srcDir("./main/resources/")
  kotlin.sourceSets["test"].kotlin.srcDir("./test/src/")
  sourceSets["test"].resources.srcDir("./test/resources/")

  val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")

    from(sourceSets["main"].allSource)
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
  }

  val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")

    from(tasks.javadoc)
    dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
  }

  artifacts {
    archives(sourcesJar)
    archives(javadocJar)
  }

  tasks {
    compileKotlin {
      kotlinOptions.jvmTarget = "1.8"
    }

    compileTestKotlin {
      kotlinOptions.jvmTarget = "1.8"
    }
  }
}
