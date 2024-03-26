plugins {
    java
    application
    kotlin("jvm") version "1.9.22"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.22"
    id("org.beryx.runtime") version "1.13.1"
}

group = "com.urosjarc"
version = "0.0.1"

kotlin {
    jvmToolchain(19)
}

repositories {
    mavenCentral()
}

application {
    mainClass = "com.urosjarc.dbanalyser.MainKt"
}

javafx {
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    // Dark style
    implementation("org.jfxtras:jmetro:11.6.14")

    // Db clients
    runtimeOnly("org.xerial:sqlite-jdbc:3.44.1.0")
    runtimeOnly("com.mysql:mysql-connector-j:8.2.0")
    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc:12.4.2.jre11")
    runtimeOnly("org.postgresql:postgresql:42.7.1")
    runtimeOnly("com.ibm.db2:jcc:11.5.9.0")
    runtimeOnly("com.h2database:h2:2.2.224")
    runtimeOnly("org.apache.derby:derby:10.17.1.0")
    runtimeOnly("org.apache.derby:derbytools:10.15.2.0")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.3.2")
    runtimeOnly("com.oracle.database.jdbc:ojdbc11:23.3.0.23.09")

    //Fuzzy search
    implementation("me.xdrop:fuzzywuzzy:1.4.0")

    //Table prrity pring
    implementation("com.jakewharton.fliptables:fliptables:1.1.1")

    //Time
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    //Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.6.0")

    // Logging
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")
    implementation("org.apache.logging.log4j:log4j-api:2.17.2")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.2.0")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")
    implementation("org.apache.logging.log4j:log4j-layout-template-json:2.17.1")

    // koin
    implementation("io.insert-koin:koin-logger-slf4j:3.3.0")
    implementation("io.insert-koin:koin-core:3.3.0")
    implementation("org.jetbrains.kotlin:kotlin-test")
}
runtime {
    imageZip.set(project.file("${project.buildDir}/image-zip/db-analyzer.zip"))
    options = listOf("--compress", "2", "--no-header-files", "--no-man-pages")
    modules = listOf("java.desktop", "jdk.unsupported", "java.scripting", "java.logging", "java.xml", "java.management")
    launcher { noConsole = true }
}
