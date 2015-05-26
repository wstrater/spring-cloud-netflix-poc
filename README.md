# spring-cloud-netflix-poc
## Overview
This project is a proof of concept to explore Spring, Netflix, Gradle and Docker as a future architecture. 

Spring
* [RESTful Web Services](https://spring.io/guides/gs/rest-service/)
* [Spring-Boot](http://projects.spring.io/spring-boot/)
* [Spring-Cloud](http://projects.spring.io/spring-cloud/)
* [Spring-Cloud-Netflix](http://cloud.spring.io/spring-cloud-netflix/)
* [Spring-Cloud-Config](http://cloud.spring.io/spring-cloud-config/)
* [Spring-Security](http://projects.spring.io/spring-security/)

Netflix
* [Eureka](https://github.com/Netflix/eureka) to publish and locate micro-services.
* [Ribbon](https://github.com/Netflix/ribbon) to provide a load-balanced access to the micro-services.
* [Feign](https://github.com/Netflix/feign) to provide a generated wrapper to Ribbon.
* [Hystrix](https://github.com/Netflix/Hystrix) to provide a circuit-breaker and monitoring for our micro-service calls.
* [Zuul](https://github.com/Netflix/zuul) to provide an edge server that can distribute browser requests.

Gradle
* [Gradle](https://gradle.org/) as a build tool. We have always used Ant and would like to move to a build tool that allows us to better manage our dependencies and efficiently work with sub-projects.

JCache
* [JCache](https://jcp.org/en/jsr/detail?id=107) for caching. We would like to look at a way of making heavy objects available to different micro-services without pre-fetching or fetching them multiple times. We have used EHCache in the past but our implementation was rather intrusive.

Docker
* [Docker](https://www.docker.com/) for containers for deployments. We would like to explore the container concept to make deployments more easy.
* [Machine](https://docs.docker.com/machine/) for abstracting out target machines for deployments.
* [Swarm](https://docs.docker.com/swarm/) for group in target machines for deployment.
* [Compose](https://docs.docker.com/compose/) for defining groups of containers for a deployment.

This POC is based on a presentation that Josh Long gave. He checked the code into GitHub, [bootiful-microservices](https://github.com/joshlong/bootiful-microservices), but it appears the project is still available but has been re-purposed for a different presentation. 
## Config-Server Project
The config-server project is a simple Spring-Config server that is used by other servers.
## Config-Data Project
The config-data project is used to contain the configuration served up by the config-server. We do not use Git and will have to explore how to integrate Spring-Config with our process. `PropertyLocator` or `PropertySource` may be an approach.
## Eureka-Server Project
The eureka-server project is a simple Spring-Cloud implementation of a Netflix Eureka server.
## Commons Project
This project contains many simple utility classes that get shared by other projects. One of the more notable set of classes is the AbstractMessage and it's implementations of formatted messages.

Production support has been a big concern at work and we use the formatted messages to get a quick searchable overview of what a user did. Each request generates a `transactionId` that is used in all messages for the request. Other values can be pulled from a Log4J `ThreadLocal` implementation, MDC. This allows us to capture the information such as `userId` when it is available and use it whenever we generate a message. This POC takes it a step farther and tries to tie the requests across all micro-services using a `RequestInterceptor` and `LoggingFilter`.
##Contact-Service Project
This project is a sample micro-services project and is used to show a pattern for implementing micro-services. It contains three sub-projects and generates two artifacts. The sub-projects are commons, client and server. The two artifacts is a client jar and a server implementation.
###Contact-Service-Commons Project
This project defines code common to the client jar and the server implementation. It contains shared data structures and REST paths.
###Contact-Service-Client Project
The client project generates a jar that is shared with the client and defines the contract between the client and server.

Others including Josh Long have talked about not providing a client jar be we feel it is important.
###Contact-Service-Server Project
The server project is the implementation of the services. It exposes the services using Spring RESTful services and Eureka to publish the instance.

This project currently uses two data-stores, JPA and a `Map`. The use of a `Map` based on initial unsuccessful attempts to get the JPA implementation properly injected while using `MockMvc` for testing.
##Passport-Service Project
This project is patterned after the Contact-Service Project but uses other micro-services such as Contact-Service as the data-store to provide aggregation instead it's own data-store.
#Building/Running
###Config-Server
This needs to be running before any other server is started. Before it can be started, it must be configured to find the configuration in the Config-Data project. This is currently a file based reference in `src/main/resources/application.yml`. Once the configuration is updated, it should be a just a simple `bootRun`
```
./gradlew bootRun
```
Once this is up and running you should be able to access the configuration. Here is a link to default configuration for Contact-Service project, http://localhost:8888/contact-service/default/mater
###Eureka-Server
Once the Config-Server is up and running you can start the Eureka-Server the same way.
```
./gradlew bootRun
```
You will notice that there are some errors in the log file during start up. Eureka is designed to run in a peer-to-peer based configuration and none of the peers are defined. This can be ignored until I finish the configuration.

Once the Eureka-Server is up and running you should be able to view registered instances, http://localhost:8761. It may take a minute or so but the first instance to show up is itself as it builds out it's peer-to-peer network.
###Commons
This project generates a jar that other projects depend on so it needs to be compiled and uploaded to the local Maven repository on your machine.
```
./gradlew uploadArchives
```
###Contact-Service
This project contains a client jar that needs to be uploaded to the local Maven repository and a server that needs to be run.
```
./gradlew uploadArchives bootRun
```
Once the Contact-Service has started and had time to register itself, it will appear on the Eureka-Server page. The timing is based on the Ribbon configuration that defaults to 30 seconds and it may take a couple cycles to appear.

Once the instance appears on the Eureka-Server page, you should be able to open the link under the `Status` header in a new tab. You will notice the port is never the same since it is randomly chosen when the server starts. This along with the randomly generated instance name allow multiple instances of micro-services run on the same machine without collision. You can test the Contact-Service with a similar link, http://localhost:#####/contacts/wstrater
###Passport-Service
This project is similar to the Contact-Service project.
```
./gradlew uploadArchives
```