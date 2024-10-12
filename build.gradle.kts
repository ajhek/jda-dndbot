plugins {
    application
    id("com.gradleup.shadow") version "8.3.1"
    id("java")
}

application.mainClass = "com.example.dndbot.Bot"
group = "org.example"
version = "1.0"

val jdaVersion = "5.1.2"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("ch.qos.logback:logback-classic:1.5.6")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true

    sourceCompatibility = "1.8"
}

tasks.test {
    useJUnitPlatform()
}