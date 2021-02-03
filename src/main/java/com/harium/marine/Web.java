package com.harium.marine;

import com.harium.marine.model.WebModule;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.before;
import static spark.Spark.options;
import static spark.Spark.staticFiles;

public class Web {

    private static int port;
    private static String host = "";
    public static final String PARAM_PATH = "path";

    static List<WebModule> webModules = new ArrayList<>();
    static List<Class<? extends WebModule>> registers = new ArrayList<>();

    public static void init() {
        Web.acceptEndSlash();
        for (WebModule webModule : webModules) {
            webModule.init();
        }
        for (Class<?> controller : registers) {
            try {
                Constructor<?> constructor = controller.getConstructor();
                WebModule webModule = (WebModule) constructor.newInstance();
                webModule.init();
                webModules.add(webModule);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        registers.clear();
    }

    public static void register(WebModule webModule) {
        webModules.add(webModule);
    }

    public static void register(Class<? extends WebModule> controller) {
        registers.add(controller);
    }

    /**
     * Method to treat paths with end slash
     */
    public static void acceptEndSlash() {
        before(new Filter() {
            @Override
            public void handle(Request request, Response response) {
                String path = request.pathInfo();
                if (!"/".equals(path) && path.endsWith("/")) {
                    response.redirect(path.substring(0, path.length() - 1));
                }
            }
        });
    }

    public static Map<String, Object> buildModel(Request request) {
        Map<String, Object> model = new HashMap<>();
        model.put(PARAM_PATH, host);

        for (WebModule controller : webModules) {
            controller.buildModel(request, model);
        }

        return model;
    }

    public static void staticFileLocation(String folder) {
        if (isLocalHost()) {
            String projectDir = System.getProperty("user.dir");
            String staticDir = "/src/main/resources" + folder;
            staticFiles.externalLocation(projectDir + staticDir);
        } else {
            staticFiles.location(folder);
        }
    }

    private static boolean isLocalHost() {
        String noPrefix = host.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)", "");
        return noPrefix.startsWith("localhost");
    }

    public static void host(String host) {
        Web.host = host;
    }

    public static void port(int port) {
        Web.port = port;
        Spark.port(port);
    }

    public static void enableCORS(final String origin, final String methods, final String headers) {
        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }

    public static String host() {
        return host;
    }

    public static int port() {
        return port;
    }
}