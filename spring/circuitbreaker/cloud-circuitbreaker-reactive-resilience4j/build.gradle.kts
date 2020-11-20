val cloudVersion = "1.0.4.RELEASE"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j:${cloudVersion}")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

//    implementation("io.github.resilience4j:resilience4j-micrometer")
//    implementation("io.micrometer:micrometer-registry-prometheus")

    testImplementation("io.projectreactor:reactor-test")
}
