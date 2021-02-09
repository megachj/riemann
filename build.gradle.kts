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
        set("springAutoRestDocsVersion", "2.0.7")
        set("springDocOpenApiUiVersion", "1.5.2") // Swagger

        set("hibernateValidatorVersion", "7.0.0.Final")
        set("resilience4jVersion", "1.6.1")
    }
}

configure(subprojects.filter { it.path.contains("java") }) {
    apply(plugin = "java")
    apply(plugin = "java-library") // dependency api 사용

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.16")
        testCompileOnly("org.projectlombok:lombok:1.18.16")
        annotationProcessor("org.projectlombok:lombok:1.18.16")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.16")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0") // 이상하게 안됨.
        testImplementation("junit:junit:4.13.1")
        testImplementation("org.assertj:assertj-core:3.19.0")
        testImplementation("org.hamcrest:hamcrest-core:2.2")
    }
}

configure(subprojects.filter { it.path.contains("spring") }) {
    apply(plugin = "java")
    apply(plugin = "java-library") // dependency api 사용
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    tasks.bootJar { enabled = true }
    tasks.jar { enabled = false }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.16")
        testCompileOnly("org.projectlombok:lombok:1.18.16")
        annotationProcessor("org.projectlombok:lombok:1.18.16")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.16")

        implementation("org.springframework.boot:spring-boot-starter")

        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        testAnnotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

configure(subprojects.filter { it.path.contains("spring") and it.path.contains("library") }) {
    tasks.bootJar { enabled = false }
    tasks.jar { enabled = true }
}
