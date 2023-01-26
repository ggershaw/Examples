spring-boot-starter
=======

<p>
If you are new to FIX, you could have a look at the [industry website](https://www.fixtrading.org/). In particular,
there is a very useful web-based dictionary provided for free that shows each version of FIX and the messages
(fields and components) that make up the version [FIXimate](https://fiximate.fixtrading.org/).
</p>
<p>
In this module, once the client receives a successful Logon, the client sends a MarketDataRequest to the server.
The server responds with a stream of MarketDataIncrementalRefresh me
</p>
This module provides a realistic example of a Marketclient/server interaction using the 
[quickfixj-spring-boot-starter](https://github.com/esanchezros/quickfixj-spring-boot-starter) developed primarily by
[Eduardo Sanchez-Ros](https://www.linkedin.com/in/eduardosanchezros/) who is a member of the [QuickFIX/J LinkedIn Group](https://www.linkedin.com/groups/14156680/). This starter reduces boilerplate code. As an exercise, a dev can compare the total lines of code in
this module to the child modules:
<p>
<strong>quickfixj/client</strong>
<br/>
<strong>quickfixj/server</strong>
</p>


## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
This project consists of a Fix client and Server that are able to run as SSL or Non-SSL 
depending on the spring profile chosen. The Client's SSL profile injects mock FIX credentials, 
which have been encrypted in the client's 
[application.yml](quickfixj/client/src/main/resources/application.yml) using Spring Boot/Jaspyt. 
FYI, the credentials will appear in clear text in the FIX messages. Additionally, the Jaspyt
encryption password is in clear text in the client's 
[application.yml](quickfixj/client/src/main/resources/application.yml).
A production version of this application could store it as an environment variable. 
See the following link for more info 
[Jasypt Security](https://github.com/ulisesbocchio/jasypt-spring-boot#demo-app)




## Technologies
Project is created with:
- Spring Boot version: 3.0
- Quickfixj version: 2.3.1


## Setup
 
### IDE
launch the main classes separately: 
- [Client.java](quickfixj/client/src/main/java/org/gershaw/quickfixj/ssl/client/Client.java) 
- [Server.java](quickfixj/server/src/main/java/org/gershaw/quickfixj/server/Server.java)
  - Use `-Dspring.profiles.active=ssl` on both Client and Server to enable SSL

### Maven
- Windows
  - Open 2 cmd prompts.
    - Start Server
      - Non-SSL: `mvn -pl :server spring-boot:run`
      - SSL: `mvn -pl :server spring-boot:run -Dspring-boot.run.profiles=ssl`
    - Start Client 
      - Non-SSL: `mvn -pl :client spring-boot:run`
      - SSL: `mvn -pl :client spring-boot:run -Dspring-boot.run.profiles=ssl`

### Self Signed SSL Cert creation
FYI, this is for testing purposes only. See article [here](https://community.pivotal.io/s/article/Generating-a-self-signed-SSL-certificate-using-the-Java-keytool-command?language=en_US0)