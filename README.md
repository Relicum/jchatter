JChatter - For sending JSON Chat messages
============================================

'''WORK IN PROGRESS'''

JChatter is a custom implementation of various other Libs by @dorkrepublic @Spoonyloony @bobacadodl 

<blockquote>
The difference being this version does not use any reflection/NMS code to create OR send the messages.
</blockquote>

This plugin uses a fluentAPI to chain the creation of messages quickly. The messages are then sent using the Console and the TellRaw command.

Included is a simple method to send to a single player or send to all players.

There is also an abstract save and load method which can be used to save and load messages that are used often. This makes it more efficient due to not having to build the JSON message each time.

Maven Dependency
----

```XML
    <dependency>
        <groupId>org.codemine</groupId>
        <artifactId>jchat</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
```

Maven Repo
----

```XML
    <repository>
        <id>snapshot</id>
        <url>http://repository-relicum.forge.cloudbees.com/snapshot/</url>
    </repository>
```

JavaDocs
---
[JAVA DOCS](https://relicum.ci.cloudbees.com/job/JChatter/javadoc/)

Jenkins
---
[JENKINS CI](https://relicum.ci.cloudbees.com/job/JChatter/)

**Pull requests gladly accepted**

  
