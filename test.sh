#!/bin/sh
java -javaagent:interceptor/target/bytebuddy-demo-1.0-SNAPSHOT-jar-with-dependencies.jar -jar browsertest/BrowserTest.jar https://nos.nl
java -javaagent:interceptor/target/bytebuddy-demo-1.0-SNAPSHOT-jar-with-dependencies.jar -jar browsertest/BrowserTest.jar https://nu.nl

