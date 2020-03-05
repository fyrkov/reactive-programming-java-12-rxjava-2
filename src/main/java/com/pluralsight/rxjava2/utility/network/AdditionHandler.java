package com.pluralsight.rxjava2.utility.network;

import com.pluralsight.rxjava2.utility.ThreadHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AdditionHandler implements HttpHandler {

    private final static Logger log = LoggerFactory.getLogger(AdditionHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Map the incoming parameters from the URL request.
        Map<String,String> parameters = queryToMap(exchange.getRequestURI().getQuery());

        int a = 0;
        int b = 0;
        int delay = 0;

        // Parse the "a" parameter if provided.
        if( parameters.containsKey("a")) {
            a = Integer.parseInt(parameters.get("a"));
        }

        // Parse the "b" parameter if provided.
        if(parameters.containsKey("b")) {
            b = Integer.parseInt(parameters.get("b"));
        }

        // Parse the delay if provided.
        if( parameters.containsKey("delay")) {
            delay = Integer.parseInt(parameters.get("delay"));
        }

        // Sleep for as long as the delay specifies.
        ThreadHelper.sleep(delay, TimeUnit.MILLISECONDS);

        // Create a string response after adding a plus b.
        String response = Integer.toString(a+b);

        // Set the response header indicating success (200) and how long
        // the response is going to be in bytes.
        exchange.sendResponseHeaders(200, response.length());

        // Write out the response byte for byte.
        exchange.getResponseBody().write(response.getBytes());

        // Close the exchange.
        exchange.close();

        // If the two numbers add up to -1, we are going to
        // request that the server shut down.
        if( (a+b) == -1) {
            SimpleHttpServer.stop();
        }
    }

    private static Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
