plugins {
    id("application")
}

application {
    mainClass.set("com.sunset.Main")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.sunset.Main"
    }
}

sourceSets {
    main {
        resources {
            srcDirs("src/main/data")
        }
    }
}

dependencies {

    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
}
