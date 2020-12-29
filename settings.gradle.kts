rootProject.name = "riemann"

include(":java")
include(":java:etc")
include(":java:etc:excel")
include(":java:generics")

include(":spring")
include(":spring:library")
include(":spring:library:jpa")
include(":spring:application")
include(":spring:application:piece")
include(":spring:application:piece:aop-reflection")
include(":spring:application:piece:async")
include(":spring:application:piece:cache")
include(":spring:application:piece:circuitbreaker")
include(":spring:application:piece:circuitbreaker:resilience4j-springboot2")
include(":spring:application:piece:reactive")
include(":spring:application:web-api")

