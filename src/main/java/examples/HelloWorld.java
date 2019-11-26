package examples;

import com.harium.marine.Web;

public class HelloWorld {

    public static void main(String[] args) {
        System.out.println("Starting Server");

        String host = "localhost";
        int port = 8081;

        System.out.println(host);

        Web.port(port);
        Web.host(host);

        Web.staticFileLocation("/public");

        // Register Web Modules
        //Marine.register(HealthCheck.class);

        // Init Web Modules
        Web.init();
    }

}
