
## Spring Boot 3.4.0 available now (November 21, 2024)

https://spring.io/blog/2024/11/21/spring-boot-3-4-0-available-now

  - Structured logging
  - Support for defining additional beans of the same type as those that are auto-configured
  - Expanded virtual thread support
  - Improved support for Docker Compose and Testcontainers
  - Numerous Actuator enhancements, including info and health information for SSL certificates
  - Improved image building capabilities, including out-of-the-box support for ARM
  - Auto-configuration for MockMvcTester, an AssertJ-based alternative to MockMvc

https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.4-Release-Notes

https://github.com/spring-projects/spring-boot/releases/tag/v3.4.0

  - WebJars Locator Integration: https://github.com/spring-projects/spring-framework/issues/27619


## Structured logging in Spring Boot 3.4

https://spring.io/blog/2024/08/23/structured-logging-in-spring-boot-3-4

https://docs.spring.io/spring-boot/3.4-SNAPSHOT/reference/features/logging.html#features.logging.structured

Logback JSON encoder and appenders: https://github.com/logfellow/logstash-logback-encoder?tab=readme-ov-file#standard-fields

## Spring Batch 5.2.0 goes GA! (November 20, 2024)

https://spring.io/blog/2024/11/20/spring-batch-5-2-0-goes-ga

    - MongoDB job repository support
    - New resourceless job repository
    - Composite item reader implementation
    - New adapters for java.util.function APIs
    - Concurrent steps with blocking queue item reader and writer



export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock
export DOCKER_HOST=unix://${HOME}/.colima/docker.sock
sudo ln -s $HOME/.colima/docker.sock /var/run/docker.sock


https://spring.academy/courses/building-a-batch-application-with-spring-batch/lessons/spring-batch-jobs-creating-jobs-workshop


https://stackoverflow.com/questions/67642620/docker-credential-desktop-not-installed-or-not-available-in-path/72888813#72888813

https://github.com/abiosoft/colima

mkdir -p ~/.docker/cli-plugins
ln -sfn $HOMEBREW_PREFIX/opt/docker-compose/bin/docker-compose ~/.docker/cli-plugins/docker-compose

brew install colima docker docker-compose

To start colima now and restart at login:

brew services restart colima

Or, if you don't want/need a background service you can just run:
/opt/homebrew/opt/colima/bin/colima start -f
==> docker-compose
Compose is a Docker plugin. For Docker to find the plugin, add "cliPluginsExtraDirs" to ~/.docker/config.json:
"cliPluginsExtraDirs": [
"/opt/homebrew/lib/docker/cli-plugins"
]


{
"credsStore": "desktop"
}
