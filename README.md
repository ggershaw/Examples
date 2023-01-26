Examples using the new QuickFIX/J Spring Boot Starter
=======
This module develops a QuickFIX/J client & server applications using the spring boot starter for QuickFIX/J and sends MarketDataIncrementalRefreshes(MDIR) from the
[server](spring-boot-starter/src/main/java/org/gershaw/quickfixj/springboot/server/BootifiedServer.java) in response to the MarketDataRequest(MDR) from the [client](spring-boot-starter/src/main/java/org/gershaw/quickfixj/springboot/client/BootifiedClient.java).
The frequency of the MDIRs is configurable in the yml file.

Please launch the [client](spring-boot-starter/src/main/java/org/gershaw/quickfixj/springboot/client/BootifiedClient.java) in its own process and the
[server](spring-boot-starter/src/main/java/org/gershaw/quickfixj/springboot/server/BootifiedServer.java) in another.

<b>Note:</b> This is example code and not fit for production. For instance, if you bring down the client after its sent the MDR, the server will continue to send MDIRs to a disconnected session, which is suboptimal and needs to be investigated.


The code for the boot starter is located [here](https://github.com/esanchezros/quickfixj-spring-boot-starter) and has excellent documentations and examples [here](https://github.com/esanchezros/quickfixj-spring-boot-starter-examples)
The starter was developed and is maintained by [Eduardo Sanchez-Ros](https://github.com/esanchezros)
<\p>

Please compare this module to the modules [client](quickfixj/client) and [server](quickfixj/server).

Having coded all three of these modules, using the spring boot starter has some clear advantages:
- Like all starters, this does a great job of setting up the dependency mgt.
- Setting up a client and server requires little code.
- Finally, I like the use of Spring's event publishing to publish low frequency events.

Look forward to the next commit on this starter :)
