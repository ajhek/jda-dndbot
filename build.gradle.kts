plugins {
    idea
    application
    id("java")
}

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
    implementation("commons-cli:commons-cli:1.5.0")
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

application {
    // Tell Gradle what the main class is that it should run, this differs per project based on your packages and classes.
    mainClass.set("com.example.dndbot.Main")
}

// We make a task "farJar", this is a task that will create a jar file that contains all the dependencies.
val fatJar = task("fatJar", type = Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Implementation-Title"] = "DnDBot"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "com.example.dndbot.Main" // Same mainclass as the application plugin setting
    }
    from(configurations.runtimeClasspath.get().map{ if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
    destinationDirectory.set(layout.buildDirectory.dir("dist"))
}

tasks {
    "build" {
        // And we tell during the build task, that it DEPENDS on the fatJar task, so it will always run the fatJar task before the build task.
        dependsOn(fatJar)
    }
}