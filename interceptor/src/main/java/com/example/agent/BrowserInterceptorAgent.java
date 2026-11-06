package com.example.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import java.lang.instrument.Instrumentation;
import java.net.URI;
import java.awt.Desktop;
import java.io.IOException;

public class BrowserInterceptorAgent {

    // The entry point for the Java Agent
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("[Agent] Infiltrating JVM to block browser launches...");

        // check if retransformation is supported by this JVM
        if (!inst.isRetransformClassesSupported()) {
            System.out.println("[AGENT] Waarschuwing: Retransformation wordt niet ondersteund!");
        }

        new AgentBuilder.Default()

            // Overwrite ignore-rules (otherwise system classes will be ignored)
            .ignore(ElementMatchers.none())

            // Tell Byte Buddy to allow modifications on core JDK classes
            .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
            .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)

            // Output everything ByteBuddy does (for debugging)
            //.with(AgentBuilder.Listener.StreamWriting.toSystemOut())

            // Explicitly target java.awt.Desktop
            .type(ElementMatchers.named("java.awt.Desktop"))
            // Weave the advice into the browse(URI) method
            .transform((builder, typeDescription, classLoader, module, protectionDomain) -> 
                builder.visit(Advice.to(DesktopBrowseInterceptor.class)
                       .on(ElementMatchers.named("browse")
                           .and(ElementMatchers.takesArgument(0, URI.class))))
            ).installOn(inst);
    }

    // embedded advice class code
    public static class DesktopBrowseInterceptor {
        @Advice.OnMethodEnter(skipOn = Advice.OnDefaultValue.class)
        public static boolean enter(@Advice.Argument(0) URI uri) {

        // Specific URI do intercept
        String targetHost = "nu.nl"; 
        
        if (uri.getHost() != null && uri.getHost().contains(targetHost)) {
            String execString = "google-chrome " + uri; // replace with your own browser cmd string
            System.out.println("\nGoogle Chrome called for: " + uri);
            try {
                Runtime.getRuntime().exec(execString);
            }  catch (IOException e) {          
                System.err.println("IOException: " + e.getMessage());
                e.printStackTrace();
            }
            return false; // Original browse() is skipped
        }
        
        // Let pass for every other website
        System.out.println("\nDefault browser for: " + uri);
        return true; // Original browse() executed

        }
    }
}
