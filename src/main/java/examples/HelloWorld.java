package examples;

import com.harium.marine.Marine;

public class HelloWorld {

    public static void main(String[] args) {
        System.out.println("Starting Server");

        String host = "localhost";
        int port = 8081;

        System.out.println(host);

        Marine.port(port);
        Marine.host(host);

        Marine.staticFileLocation("/public");

        // Register Web Modules
        //Marine.register(HealthCheck.class);

        // Init Web Modules
        Marine.init();
    }

}
