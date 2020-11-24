val resilience4jVersion: String by ext

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    implementation("io.github.resilience4j:resilience4j-spring-boot2:${resilience4jVersion}")
    implementation("io.github.resilience4j:resilience4j-reactor:${resilience4jVersion}")
    // implementation("io.github.resilience4j:resilience4j-all:${resilience4jVersion}") // Optional, only required when you want to use the Decorators class
    // implementation("io.micrometer:micrometer-registry-prometheus")

    testImplementation("io.projectreactor:reactor-test")
}
