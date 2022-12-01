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
To run this project, launch the main classes separately [Client.java](quickfixj%2Fclient%2Fsrc%2Fmain%2Fjava%2Forg%2Fgershaw%2Fquickfixj%2Fssl%2Fclient%2FClient.java) and [Server.java](quickfixj%2Fserver%2Fsrc%2Fmain%2Fjava%2Forg%2Fgershaw%2Fquickfixj%2Fserver%2FServer.java) from your favorite IDE. Use `-Dspring.profiles.active=ssl` on both Client and Server to enable SSL