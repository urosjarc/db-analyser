plugins {
    java
    application
    kotlin("jvm") version "1.9.22"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
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

    //Drivers
    runtimeOnly("org.xerial:sqlite-jdbc:3.44.1.0") // Lets continue with sqlite driver...
    runtimeOnly("com.ibm.db2:jcc:11.5.9.0")
    runtimeOnly("com.h2database:h2:2.2.224")
    runtimeOnly("org.apache.derby:derby:10.17.1.0")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.3.2")
    runtimeOnly("com.mysql:mysql-connector-j:8.2.0")
    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc:12.4.2.jre11")
    runtimeOnly("org.postgresql:postgresql:42.7.1")
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
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.2.0")

    // koin
    implementation("io.insert-koin:koin-logger-slf4j:3.3.0")
    implementation("io.insert-koin:koin-core:3.3.0")
    implementation("org.jetbrains.kotlin:kotlin-test")

    /**
     * TESTING
     */
    testImplementation("com.urosjarc:db-messiah:0.0.1")                     // Required
    testImplementation("com.urosjarc:db-messiah-extra:0.0.1")               // Optional extra utils
    testImplementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0") // Optional logging
}
