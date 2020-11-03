plugins {
    java
    `java-library`
    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }

    group = "com.sunset"
    version = "1.0.0"

    ext {
        set("commonsLangVersion", "3.9")
        set("googleGuavaVersion", "26.0-android")
        set("hibernateValidatorVersion", "6.1.2.Final")
        set("springAutoRestDocsVersion", "2.0.7")
        set("springDocOpenApiUiVersion", "1.4.3")
    }
}

configure(subprojects.filter { it.parent?.name in listOf("java") }) {
    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.16")
        testCompileOnly("org.projectlombok:lombok:1.18.16")
        annotationProcessor("org.projectlombok:lombok:1.18.16")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.16")

        testImplementation("junit:junit:4.13.1")
        testImplementation("org.hamcrest:hamcrest-core:2.2")
    }
}
