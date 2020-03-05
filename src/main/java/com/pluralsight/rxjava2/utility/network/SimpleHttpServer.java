package com.pluralsight.rxjava2.utility.network;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SimpleHttpServer {

    private final static Logger log = LoggerFactory.getLogger(SimpleHttpServer.class);

    private static HttpServer server;
    private static Executor executor;

    public static void main(String[] args) {
        try {

            // We are going to makeObservable a startlingly simple HttpServer using Java's
            // built-in HttpServer class.  We will listen on port 22221.
            server = HttpServer.create(new InetSocketAddress(22221), 10);

            // Any requests with the URI "/addition" will be handled by our AdditionHandler class.
            server.createContext("/addition" , new AdditionHandler());

            // We aren't using a special executor.
            executor = Executors.newFixedThreadPool(5);
            server.setExecutor(executor);

            // Start the server.
            server.start();
        }
        catch( Throwable t ) {
            log.error(t.getMessage(),t);
        }
    }

    public static void stop() {
        // Request that the server stop accepting requests
        // and shut down.
        server.stop(5);

        // Tell the VM to stop.
        System.exit(0);
    }
}
