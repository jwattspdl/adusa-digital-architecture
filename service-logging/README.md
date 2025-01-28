# Service Logging Configuration Guide

This is a simple Spring project that provides an example of how to configure logging to a JSON format. 
The JSON format for logs is required by the SRE team to facilitate log aggregation and analysis within the Datadog
tool. For the most current set of instructions on JSON log configuration for Java and Datadog, 
see [these instructions at the Datadog site](https://docs.datadoghq.com/logs/log_collection/java/?tab=logback).

Spring Boot [by default uses the Logback logging framework](https://docs.spring.io/spring-boot/docs/3.2.x/reference/htmlsingle/#features.logging),
and this example project provides a simple configuration for Logback to output logs in JSON format, according to the 
Datadog instructions page [here](https://docs.datadoghq.com/logs/log_collection/java/?tab=logback)

## Configuration

### Files

```text
{root}/
   |- src/
       |- main/
           |- resources/
               |- logback-spring.xml - contains logback configuration, references logging values from Spring config
               |- application.yml - contains Spring Boot configuration values that can reference externalized config values, e.g., environment variables
               
   |- Dockerfile - a docker file that can be used to build a container for the application and demonstrate use of Spring profiles in Logback configuration
```

#### Spring Configuration 

The above sets various Spring configuration values, some of which are part of the logging configuration that can be used
without a `logback-spring.xml` file, but all are referenced from the `logback-spring.xml` file. 

`application.yml`

```yaml
logging.file: ${LOGGING_FILE}
logging.file.name: ${LOGGING_FILE_NAME}
logging.file.max-history: ${LOGGING_FILE_MAX_HISTORY}
logging.file.max-size: ${LOGGING_FILE_MAX_SIZE}
logging.level.root: ${LOGGING_LEVEL_ROOT}
logging.pattern.file: ${LOGGING_PATTERN_FILE}
logging.pattern.console: ${LOGGING_PATTERN_CONSOLE}
```

#### Logback Configuration

For consistency, a `logback-spring.xml` configuration file must be included in the `src/main/resources` directory. Using
the `logback-spring.xml` file allows developers to take advantage of Spring Boot's Logback extensions (see [here](https://docs.spring.io/spring-boot/docs/3.2.x/reference/htmlsingle/#features.logging.logback-extensions),
allows access to [Spring Boot Environment configuration properties](https://docs.spring.io/spring-boot/docs/3.2.x/reference/htmlsingle/#features.logging.logback-extensions)
and allows developers to leverage [Spring profiles](https://docs.spring.io/spring-boot/docs/3.2.x/reference/htmlsingle/#features.logging.logback-extensions) to control whole sections of the logging configuration for details.


```xml
<configuration>
    <springProperty name="LOG_LEVEL" source="logging.level.root" defaultValue="INFO"/>
    <springProperty name="LOG_MAX_HISTORY" source="logging.logback.max-history" defaultValue="30"/>
    <springProperty name="LOG_MAX_FILE_SIZE" source="logging.logback.max-file-size" defaultValue="10MB"/>
    <springProperty name="LOG_TOTAL_SIZE_CAP" source="logging.logback.total-size-cap" defaultValue="100MB"/>
    <springProperty name="LOG_CLEAN_HOSTORY_ON_START" source="logging.logback.clean-history-on-start" defaultValue="true"/>
    <springProperty name="LOG_FILE" source="logging.file.name"/>
    
    <springProfile name="! container">
        <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>${LOG_FILE}</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>application.%d{yyyy-MM-dd-HH:mm:ss.SSS}.log</fileNamePattern>
                <maxHistory>${LOG_MAX_HISTORY}</maxHistory>
                <maxFileSize>${LOG_MAX_FILE_SIZE}</maxFileSize>
                <totalSizeCap>${LOG_TOTAL_SIZE_CAP}</totalSizeCap>
                <cleanHistoryOnStart>${LOG_CLEAN_HOSTORY_ON_START}</cleanHistoryOnStart>
            </rollingPolicy>
            <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
        </appender>
    </springProfile>

    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
    </appender>

   <springProfile name="! container">
       <root level="${LOG_LEVEL}">
           <appender-ref ref="FILE"/>
           <appender-ref ref="CONSOLE"/>
       </root>
   </springProfile>
   <springProfile name="container">
      <root level="${LOG_LEVEL}">
         <appender-ref ref="CONSOLE"/>
      </root>
   </springProfile>
   
</configuration>
```

Pay attention to the following in the example above:

1. Use of `springProperty` value to access values configured in `application.yml` file.
   - [Logback RollingFileAppender](https://logback.qos.ch/manual/appenders.html#RollingFileAppender)
   - [TimeBasedRollingPolicy](https://logback.qos.ch/manual/appenders.html#TimeBasedRollingPolicy) 
2. Providing default values for the `springProperty` values in case they are not present in the environment.
3. Use of `springProfile` to conditionally include the `FILE` appender based on the absence of the `container` profile.

> **All applications and services deployed to CF2 in containers should only log to STDOUT**.
> 
> The above configuration would be backwards compatible with QTS deployments as the `container` profile must be set to 
> leave out the `FILE` appender.
>
 
## Building the Application

### Build with Gradle

```bash
./gradlew clean build
```

### Import in IDE

Import the project into your IDE as a Gradle project.

## Running the Examples

### Run the Application with Gradle

Run with the default profile, which will include the `FILE` appender.

```bash
./gradlew bootRun   
```
After the run, there will be an `application.[date-time].log` file(s) in this project directory. 

To run with the `container` profile, which will not produce a file. use the following command.

```bash
 ./gradlew bootRun --args='--spring.profiles.active=container'
```

## Sources
- [Datadog Java Log Collection](https://docs.datadoghq.com/logs/log_collection/java/?tab=logback)
- [Spring Logging](https://docs.spring.io/spring-boot/docs/3.2.x/reference/htmlsingle/#features.logging)
- [Spring Logback Extensions](https://docs.spring.io/spring-boot/docs/3.2.x/reference/htmlsingle/#features.logging.logback-extensions)  