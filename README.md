Examples
=======

<p>
</p>

<strong style="color:red;">Attention</strong> This project has a child module named 
[custom-fix-message-generation](quickfixj/custom-fix-message-generation). It generates classes
from a FIX dictionary. As you know, this is about 1.8k source files. As a result, it takes roughly
5 minutes for this entire project to build. After you build the whole project for the first, 
you can shorten the build by using.

`
mvn -pl !org.gershaw:custom-fix-message-generation install
`
<p>
  <strong>After, you build the whole project the first time, you must mark the folder quickfixj/custom-fix-message-generation/target/generated-sources as a generated source.</strong>
</p>

![See Screenshot](/quickfixj/custom-fix-message-generation/Screenshot.png?raw=true "See Screenshot")

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