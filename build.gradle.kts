plugins {
    java
    `java-library`
    id("org.springframework.boot") version "2.3.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }

    group = "com.sunset"

    ext {
        set("hibernateValidatorVersion", "6.1.2.Final")
        set("springAutoRestDocsVersion", "2.0.7")
        set("springDocOpenApiUiVersion", "1.4.3")
    }
}

configure(subprojects.filter { it.path.contains("java") or it.path.contains("spring") }) {
    apply(plugin = "java")
    apply(plugin = "java-library")

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.16")
        testCompileOnly("org.projectlombok:lombok:1.18.16")
        annotationProcessor("org.projectlombok:lombok:1.18.16")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.16")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
        testImplementation("org.hamcrest:hamcrest-core:2.2")
    }
}

configure(subprojects.filter { it.path.contains("spring") }) {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    tasks.bootJar { enabled = true }
    tasks.jar { enabled = false }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")

        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        testAnnotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}
