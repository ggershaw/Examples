Examples
=======
## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
This project consists of a Fix client and Server that are able to run as SSL or Non-SSL depending on the spring profile. 

## Technologies
Project is created with:
* Spring Boot version: 3.0
* Quickfixj version: 2.3.1


## Setup
 
### IDE
launch the main classes separately: 
[Client.java](https://github.com/ggershaw/Examples/blob/5ec39a203bd2617de152b62e7e2b5453b4d2d7d2/quickfixj/client/src/main/java/org/gershaw/quickfixj/ssl/client/Client.java) 
[Server.java](https://github.com/ggershaw/Examples/blob/5ec39a203bd2617de152b62e7e2b5453b4d2d7d2/quickfixj/server/src/main/java/org/gershaw/quickfixj/server/Server.java)
Use `-Dspring.profiles.active=ssl` on both Client and Server to enable SSL

### Maven


#### Windows
Open 2 cmd prompts. 1 will be used to start the server. 1 will be used to start the client

##### Start Server

###### Non-SSL
mvn -pl :server spring-boot:run

####### SSL
mvn -pl :server spring-boot:run -Dspring-boot.run.profiles=ssl


##### Start Client
mvn -pl :client spring-boot:run

###### Non-SSL
mvn -pl :client spring-boot:run

####### SSL
mvn -pl :client spring-boot:run -Dspring-boot.run.profiles=ssl

