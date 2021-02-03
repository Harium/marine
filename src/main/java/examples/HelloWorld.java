package examples;

import com.harium.marine.Web;

import static spark.Spark.get;

// curl -XGET http://localhost:9090/hello
public class HelloWorld {

    public static void main(String[] args) {
        System.out.println("Starting Server");

        String host = "localhost";
        int port = 9090;

        Web.port(port);
        Web.host(host);

        // Register Web Modules
        //Web.register(HealthCheck.class);
        Web.register(() -> get("/hello", (req, res) -> "Hello World"));

        // Init Web Modules
        Web.init();
    }

}
