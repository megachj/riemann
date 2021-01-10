//plugins {
//    id("org.asciidoctor.convert") version "2.4.0" // spring-rest-docs
//}

val springDocOpenApiUiVersion: String by ext

//tasks {
//    asciidoctor {
//        dependsOn(test)
//    }
//    bootJar {
//        dependsOn(asciidoctor)
//    }
//}

dependencies {
    implementation(project(":spring:library:jpa"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.springdoc:springdoc-openapi-ui:${springDocOpenApiUiVersion}") // swagger
}
