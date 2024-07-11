*date: 2023-11-30*

## 概述

Spring 官网：https://spring.io/

Spring Boot 官网：https://spring.io/projects/spring-boot

**`总述`：**

Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".

We take an opinionated view of the Spring platform and third-party libraries so you can get started with minimum fuss. Most Spring Boot applications need minimal Spring configuration.

If you’re looking for information about a specific version, or instructions about how to upgrade from an earlier release, check out [the project release notes section](https://github.com/spring-projects/spring-boot/wiki#release-notes) on our wiki.

> 关键字：stand-alone（独立的），production-grade（生产级的），Spring based Applications（基于 Spring 的应用）。

**`特性`：**

- Create stand-alone Spring applications.
- Embed Tomcat, Jetty or Undertow directly (no need to deploy WAR files).
- Provide opinionated 'starter' dependencies to simplify your build configuration.
- Automatically configure Spring and 3rd party libraries whenever possible.
- Provide production-ready features such as metrics, health checks, and externalized configuration.
- Absolutely no code generation and no requirement for XML configuration.

> 关键字：
>
> - 创建独立的 Spring 应用。
> - 内嵌 Tomcat，Jetty 和 Undertow 服务器，无需部署 WAR 包。
> - 提供 starter 启动器。
> - 自动配置。
> - 提供各种特性，如指标、健康检查和外部化配置等。
> - 无需代码生成和 XML 配置。

## Getting Started

官网：https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html#getting-started

### 系统要求

Spring Boot 3.2.0 requires [Java 17](https://www.java.com/) and is compatible up to and including Java 21. [Spring Framework 6.1.1](https://docs.spring.io/spring-framework/reference/6.1/) or above is also required.

Explicit build support is provided for the following build tools:

| Build Tool | Version                    |
| :--------- | :------------------------- |
| Maven      | 3.6.3 or later             |
| Gradle     | 7.x (7.5 or later) and 8.x |

**Servlet Containers**

Spring Boot supports the following embedded servlet containers:

| Name         | Servlet Version |
| :----------- | :-------------- |
| Tomcat 10.1  | 6.0             |
| Jetty 12.0   | 6.0             |
| Undertow 2.3 | 6.0             |

You can also deploy Spring Boot applications to any servlet 5.0+ compatible container.

**GraalVM Native Images**

Spring Boot applications can be [converted into a Native Image](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.introducing-graalvm-native-images) using GraalVM 22.3 or above.

Images can be created using the [native build tools](https://github.com/graalvm/native-build-tools) Gradle/Maven plugins or `native-image` tool provided by GraalVM. You can also create native images using the [native-image Paketo buildpack](https://github.com/paketo-buildpacks/native-image).

The following versions are supported:

| Name               | Version |
| :----------------- | :------ |
| GraalVM Community  | 22.3    |
| Native Build Tools | 0.9.28  |

### 构建应用

**第一步：`Maven 创建 pom.xml`。**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>myproject</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>

    <!-- Additional lines to be added here... -->

</project>
```

> Spring Boot 项目，均需要添加 parent 依赖 spring-boot-starter-parent。

**第二步：`按需添加 starter`。**

Spring Boot provides a number of “Starters” that let you add jars to your classpath. “Starters” provide dependencies that you are likely to need when developing a specific type of application. Now add the `spring-boot-starter-web` dependency immediately below the `parent` section:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

> Spring Boot 提供了很多 starter（启动器），根据业务需求添加对应的 starter，Maven 会自动加载对应的依赖。通过`mvn dependency:tree`命令，可以查看依赖树。此处添加的是 Web 开发对应的 starter：spring-boot-starter-web。

**第三步：`编写代码`。**

```java
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MyApplication {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}
```

> 此处，我们编写了一个最简单的 Spring Boot 应用程序，使用了 @RestController 和 @SpringBootApplication 注解。**@RestController 是 Spring MVC 的注解，`@SpringBootApplication`是 Spring Boot 的核心注解，它是一个复合注解，由 @SpringBootConfiguration，@EnableAutoConfiguration 和 @ComponentScan 三个注解组成。**
>
> 在实际生产时，会将 @RestController 和 @SpringBootApplication 注解分开使用，此处仅仅为演示。

**第四步：`启动程序`。**

```shell
$ mvn spring-boot:run

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::  (v3.2.0)
....... . . .
....... . . . (log output here)
....... . . .
........ Started MyApplication in 0.906 seconds (process running for 6.514)
```

此时，在浏览器输入 localhost:8080，可以看到以下响应：

```
Hello World!
```

**第五步：`创建可执行 Jar 包`。**

To create an executable jar, we need to add the `spring-boot-maven-plugin` to our `pom.xml`. To do so, insert the following lines just below the `dependencies` section:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

Save your `pom.xml` and run `mvn package` from the command line, as follows:

```shell
$ mvn package

[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building myproject 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] .... ..
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ myproject ---
[INFO] Building jar: /Users/developer/example/spring-boot-example/target/myproject-0.0.1-SNAPSHOT.jar
[INFO]
[INFO] --- spring-boot-maven-plugin:3.2.0:repackage (default) @ myproject ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

If you look in the `target` directory, you should see `myproject-0.0.1-SNAPSHOT.jar`. The file should be around 18 MB in size. If you want to peek inside, you can use `jar tvf`, as follows:

```shell
$ jar tvf target/myproject-0.0.1-SNAPSHOT.jar
```

> `jar tvf`命令，可以查看 jar 包的内容。

You should also see a much smaller file named `myproject-0.0.1-SNAPSHOT.jar.original` in the `target` directory. This is the original jar file that Maven created before it was repackaged by Spring Boot.

To run that application, use the `java -jar` command, as follows:

```shell
$ java -jar target/myproject-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::  (v3.2.0)
....... . . .
....... . . . (log output here)
....... . . .
........ Started MyApplication in 0.999 seconds (process running for 1.253)
```

## Using Spring Boot

官网：https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using

It is strongly recommended that you choose a build system that supports [*dependency management*](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.build-systems.dependency-management) and that can consume artifacts published to the “Maven Central” repository. We would recommend that you choose Maven or Gradle. It is possible to get Spring Boot to work with other build systems (Ant, for example), but they are not particularly well supported.

> Spring Boot 强烈推荐使用支持依赖管理功能的构建系统，`建议使用 Maven 或者 Gradle`，本文基于 Maven 进行说明。

Each release of Spring Boot provides a curated list of dependencies that it supports. In practice, you do not need to provide a version for any of these dependencies in your build configuration, as Spring Boot manages that for you. When you upgrade Spring Boot itself, these dependencies are upgraded as well in a consistent way.

The curated list contains all the Spring modules that you can use with Spring Boot as well as a refined list of third party libraries. The list is available as a standard Bills of Materials (`spring-boot-dependencies`) that can be used with both [Maven](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.build-systems.maven) and [Gradle](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.build-systems.gradle).

Although you can still specify a version and override Spring Boot’s recommendations if you need to do so, but each release of Spring Boot is associated with a base version of the Spring Framework. We **highly** recommend that you do not specify its version.

> Spring Boot 的每一个版本，都提供了一个所需的依赖列表，包括版本，虽然使用者也可以自定义其中某些依赖的版本（尤其是 Spring Framework 的基础版本），但是 Spring Boot 不推荐这样做。

### Starters

Starters are a set of convenient dependency descriptors that you can include in your application. You get a one-stop shop for all the Spring and related technologies that you need without having to hunt through sample code and copy-paste loads of dependency descriptors. For example, if you want to get started using Spring and JPA for database access, include the `spring-boot-starter-data-jpa` dependency in your project.

The starters contain a lot of the dependencies that you need to get a project up and running quickly and with a consistent, supported set of managed transitive dependencies.

> **`Starter，启动器。`**每一个启动器，都包含了适配该场景开发所需的大量的依赖项，这些依赖项可以让项目快速启动和运行，并提供一套一致的、受支持的受管传递依赖项。

All **official** starters follow a similar naming pattern; `spring-boot-starter-*`, where `*` is a particular type of application. As explained in the “[Creating Your Own Starter](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.custom-starter)” section, third party starters should not start with `spring-boot`, as it is reserved for official Spring Boot artifacts. Rather, a third-party starter typically starts with the name of the project. For example, a third-party starter project called `thirdpartyproject` would typically be named `thirdpartyproject-spring-boot-starter`.

> 一般而言，Spring Boot 官方的启动器，会以`spring-boot-starter-*`表示，而第三方的启动器，会以`thirdpartyproject-spring-boot-starter`表示。

The following application starters are provided by Spring Boot under the `org.springframework.boot` group:

*Table 1. Spring Boot application starters*

| Name                                              | Description                                                  |
| :------------------------------------------------ | :----------------------------------------------------------- |
| `spring-boot-starter`                             | Core starter, including auto-configuration support, logging and YAML |
| `spring-boot-starter-activemq`                    | Starter for JMS messaging using Apache ActiveMQ              |
| `spring-boot-starter-amqp`                        | Starter for using Spring AMQP and Rabbit MQ                  |
| `spring-boot-starter-aop`                         | Starter for aspect-oriented programming with Spring AOP and AspectJ |
| `spring-boot-starter-artemis`                     | Starter for JMS messaging using Apache Artemis               |
| `spring-boot-starter-batch`                       | Starter for using Spring Batch                               |
| `spring-boot-starter-cache`                       | Starter for using Spring Framework’s caching support         |
| `spring-boot-starter-data-cassandra`              | Starter for using Cassandra distributed database and Spring Data Cassandra |
| `spring-boot-starter-data-cassandra-reactive`     | Starter for using Cassandra distributed database and Spring Data Cassandra Reactive |
| `spring-boot-starter-data-couchbase`              | Starter for using Couchbase document-oriented database and Spring Data Couchbase |
| `spring-boot-starter-data-couchbase-reactive`     | Starter for using Couchbase document-oriented database and Spring Data Couchbase Reactive |
| `spring-boot-starter-data-elasticsearch`          | Starter for using Elasticsearch search and analytics engine and Spring Data Elasticsearch |
| `spring-boot-starter-data-jdbc`                   | Starter for using Spring Data JDBC                           |
| `spring-boot-starter-data-jpa`                    | Starter for using Spring Data JPA with Hibernate             |
| `spring-boot-starter-data-ldap`                   | Starter for using Spring Data LDAP                           |
| `spring-boot-starter-data-mongodb`                | Starter for using MongoDB document-oriented database and Spring Data MongoDB |
| `spring-boot-starter-data-mongodb-reactive`       | Starter for using MongoDB document-oriented database and Spring Data MongoDB Reactive |
| `spring-boot-starter-data-neo4j`                  | Starter for using Neo4j graph database and Spring Data Neo4j |
| `spring-boot-starter-data-r2dbc`                  | Starter for using Spring Data R2DBC                          |
| `spring-boot-starter-data-redis`                  | Starter for using Redis key-value data store with Spring Data Redis and the Lettuce client |
| `spring-boot-starter-data-redis-reactive`         | Starter for using Redis key-value data store with Spring Data Redis reactive and the Lettuce client |
| `spring-boot-starter-data-rest`                   | Starter for exposing Spring Data repositories over REST using Spring Data REST and Spring MVC |
| `spring-boot-starter-freemarker`                  | Starter for building MVC web applications using FreeMarker views |
| `spring-boot-starter-graphql`                     | Starter for building GraphQL applications with Spring GraphQL |
| `spring-boot-starter-groovy-templates`            | Starter for building MVC web applications using Groovy Templates views |
| `spring-boot-starter-hateoas`                     | Starter for building hypermedia-based RESTful web application with Spring MVC and Spring HATEOAS |
| `spring-boot-starter-integration`                 | Starter for using Spring Integration                         |
| `spring-boot-starter-jdbc`                        | Starter for using JDBC with the HikariCP connection pool     |
| `spring-boot-starter-jersey`                      | Starter for building RESTful web applications using JAX-RS and Jersey. An alternative to [`spring-boot-starter-web`](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#spring-boot-starter-web) |
| `spring-boot-starter-jooq`                        | Starter for using jOOQ to access SQL databases with JDBC. An alternative to [`spring-boot-starter-data-jpa`](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#spring-boot-starter-data-jpa) or [`spring-boot-starter-jdbc`](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#spring-boot-starter-jdbc) |
| `spring-boot-starter-json`                        | Starter for reading and writing json                         |
| `spring-boot-starter-mail`                        | Starter for using Java Mail and Spring Framework’s email sending support |
| `spring-boot-starter-mustache`                    | Starter for building web applications using Mustache views   |
| `spring-boot-starter-oauth2-authorization-server` | Starter for using Spring Authorization Server features       |
| `spring-boot-starter-oauth2-client`               | Starter for using Spring Security’s OAuth2/OpenID Connect client features |
| `spring-boot-starter-oauth2-resource-server`      | Starter for using Spring Security’s OAuth2 resource server features |
| `spring-boot-starter-pulsar`                      | Starter for using Spring for Apache Pulsar                   |
| `spring-boot-starter-pulsar-reactive`             | Starter for using Spring for Apache Pulsar Reactive          |
| `spring-boot-starter-quartz`                      | Starter for using the Quartz scheduler                       |
| `spring-boot-starter-rsocket`                     | Starter for building RSocket clients and servers             |
| `spring-boot-starter-security`                    | Starter for using Spring Security                            |
| `spring-boot-starter-test`                        | Starter for testing Spring Boot applications with libraries including JUnit Jupiter, Hamcrest and Mockito |
| `spring-boot-starter-thymeleaf`                   | Starter for building MVC web applications using Thymeleaf views |
| `spring-boot-starter-validation`                  | Starter for using Java Bean Validation with Hibernate Validator |
| `spring-boot-starter-web`                         | Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container |
| `spring-boot-starter-web-services`                | Starter for using Spring Web Services                        |
| `spring-boot-starter-webflux`                     | Starter for building WebFlux applications using Spring Framework’s Reactive Web support |
| `spring-boot-starter-websocket`                   | Starter for building WebSocket applications using Spring Framework’s MVC WebSocket support |

In addition to the application starters, the following starters can be used to add *[production ready](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator)* features:

*Table 2. Spring Boot production starters*

| Name                           | Description                                                  |
| :----------------------------- | :----------------------------------------------------------- |
| `spring-boot-starter-actuator` | Starter for using Spring Boot’s Actuator which provides production ready features to help you monitor and manage your application |

Finally, Spring Boot also includes the following starters that can be used if you want to exclude or swap specific technical facets:

*Table 3. Spring Boot technical starters*

| Name                                | Description                                                  |
| :---------------------------------- | :----------------------------------------------------------- |
| `spring-boot-starter-jetty`         | Starter for using Jetty as the embedded servlet container. An alternative to [`spring-boot-starter-tomcat`](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#spring-boot-starter-tomcat) |
| `spring-boot-starter-log4j2`        | Starter for using Log4j2 for logging. An alternative to [`spring-boot-starter-logging`](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#spring-boot-starter-logging) |
| `spring-boot-starter-logging`       | Starter for logging using Logback. Default logging starter   |
| `spring-boot-starter-reactor-netty` | Starter for using Reactor Netty as the embedded reactive HTTP server. |
| `spring-boot-starter-tomcat`        | Starter for using Tomcat as the embedded servlet container. Default servlet container starter used by [`spring-boot-starter-web`](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#spring-boot-starter-web) |
| `spring-boot-starter-undertow`      | Starter for using Undertow as the embedded servlet container. An alternative to [`spring-boot-starter-tomcat`](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#spring-boot-starter-tomcat) |

> Spring Boot 的启动器，分为三个部分：
>
> 1. application starters：应用启动器，涵盖了大部分开发中的场景。
> 2. production starters：生产启动器，提供了一些生产上的特性，例如系统监控等。
> 3. technical starters：技术启动器，对现使用的一些技术的替代方案。

To learn how to swap technical facets, please see the how-to documentation for [swapping web server](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.webserver.use-another) and [logging system](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.logging.log4j).

For a list of additional community contributed starters, see the [README file](https://github.com/spring-projects/spring-boot/tree/main/spring-boot-project/spring-boot-starters/README.adoc) in the `spring-boot-starters` module on GitHub.

### 构建代码

When a class does not include a `package` declaration, it is considered to be in the “default package”. The use of the “default package” is generally discouraged and should be avoided. It can cause particular problems for Spring Boot applications that use the `@ComponentScan`, `@ConfigurationPropertiesScan`, `@EntityScan`, or `@SpringBootApplication` annotations, since every class from every jar is read. We recommend that you follow Java’s recommended package naming conventions and use a reversed domain name (for example, `com.example.project`).

> 不要使用默认包，给项目定义一个对应的域名结构。

We generally recommend that you locate your main application class in a root package above other classes. The [`@SpringBootApplication` annotation](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.using-the-springbootapplication-annotation) is often placed on your main class, and it implicitly defines a base “search package” for certain items. For example, if you are writing a JPA application, the package of the `@SpringBootApplication` annotated class is used to search for `@Entity` items. Using a root package also allows component scan to apply only on your project.

> 将主程序类放在根包中，高于其他的类，并标注**`@SpringBootApplication`**注解。

### 添加配置类

Spring Boot favors Java-based configuration. Although it is possible to use `SpringApplication` with XML sources, we generally recommend that your primary source be a single `@Configuration` class.

You need not put all your `@Configuration` into a single class. The `@Import` annotation can be used to import additional configuration classes. Alternatively, you can use `@ComponentScan` to automatically pick up all Spring components, including `@Configuration` classes.

If you absolutely must use XML based configuration, we recommend that you still start with a `@Configuration` class. You can then use an `@ImportResource` annotation to load XML configuration files.

> Spring Boot 建议使用**`@Configuration`**注解来标识一个配置类，再使用`@ComponentScan`注解，可以扫描所有的 Spring 组件（包括 @Configuration 注解标识的类），并将组件注入容器。另外，对于其他的配置类，可以使用`@Import`注解导入，如果是 XML 配置文件，则可以使用`@ImportResource`注解导入。

### 自动配置

Auto-configuration packages are the packages that various auto-configured features look in by default when scanning for things such as entities and Spring Data repositories. The `@EnableAutoConfiguration` annotation (either directly or through its presence on `@SpringBootApplication`) determines the default auto-configuration package. Additional packages can be configured using the `@AutoConfigurationPackage` annotation.

>**`@EnableAutoConfiguration`**注解（可直接使用，也可在 @SpringBootApplication 中使用）决定了默认的自动配置包，也可使用`@AutoConfigurationPackage`注解配置其他软件包。

### Spring Beans 和依赖注入

You are free to use any of the standard Spring Framework techniques to define your beans and their injected dependencies. We generally recommend using constructor injection to wire up dependencies and `@ComponentScan` to find beans.

If you structure your code as suggested above (locating your application class in a top package), you can add `@ComponentScan` without any arguments or use the `@SpringBootApplication` annotation which implicitly includes it. All of your application components (`@Component`, `@Service`, `@Repository`, `@Controller`, and others) are automatically registered as Spring Beans.

> Spring Boot 使用`@ComponentScan`注解扫描定义的 Spring Beans，也可以使用 @SpringBootApplication 注解来隐式地包含它。

### @SpringBootApplication 注解

Many Spring Boot developers like their apps to use auto-configuration, component scan and be able to define extra configuration on their "application class". A single `@SpringBootApplication` annotation can be used to enable those three features, that is:

- `@EnableAutoConfiguration`: enable [Spring Boot’s auto-configuration mechanism](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.auto-configuration).
- `@ComponentScan`: enable `@Component` scan on the package where the application is located (see [the best practices](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.structuring-your-code)).
- `@SpringBootConfiguration`: enable registration of extra beans in the context or the import of additional configuration classes. An alternative to Spring’s standard `@Configuration` that aids [configuration detection](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing.spring-boot-applications.detecting-configuration) in your integration tests.

eg:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Same as @SpringBootConfiguration @EnableAutoConfiguration @ComponentScan
@SpringBootApplication
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}
```

None of these features are mandatory and you may choose to replace this single annotation by any of the features that it enables. For instance, you may not want to use component scan or configuration properties scan in your application:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration(proxyBeanMethods = false)
@EnableAutoConfiguration
@Import({ SomeConfiguration.class, AnotherConfiguration.class })
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}
```

In this example, `MyApplication` is just like any other Spring Boot application except that `@Component`-annotated classes and `@ConfigurationProperties`-annotated classes are not detected automatically and the user-defined beans are imported explicitly (see `@Import`).

### 开发人员工具

Spring Boot includes an additional set of tools that can make the application development experience a little more pleasant. The `spring-boot-devtools` module can be included in any project to provide additional development-time features. To include devtools support, add the module dependency to your build, as shown in the following listings for Maven and Gradle:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

Developer tools are automatically disabled when running a fully packaged application. If your application is launched from `java -jar` or if it is started from a special classloader, then it is considered a “production application”. You can control this behavior by using the `spring.devtools.restart.enabled` system property. To enable devtools, irrespective of the classloader used to launch your application, set the `-Dspring.devtools.restart.enabled=true` system property. This must not be done in a production environment where running devtools is a security risk. To disable devtools, exclude the dependency or set the `-Dspring.devtools.restart.enabled=false` system property.

> 打包应用的时候，Devtools 默认会被排除。

Devtools might cause classloading issues, in particular in multi-module projects. [Diagnosing Classloading Issues](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools.diagnosing-classloading-issues) explains how to diagnose and solve them.

>Devtools 可能会造成会导致类加载问题，尤其是在多模块项目中。

## Core Features

// TODO
