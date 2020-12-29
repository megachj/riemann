plugins {
    id("org.asciidoctor.convert") version "2.4.0"
}

val springDocOpenApiUiVersion: String by ext
//val springAutoRestDocsVersion: String by ext

dependencies {
    implementation(project(":spring:library:jpa"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Rest Docs
    implementation("org.springdoc:springdoc-openapi-ui:${springDocOpenApiUiVersion}")

//    asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor")
//    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}
