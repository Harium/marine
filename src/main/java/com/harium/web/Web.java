package com.harium.web;

import com.harium.web.model.WebModule;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.staticFiles;

public class Web {

    private static String host = "";
    public static final String PARAM_PATH = "path";

    static List<WebModule> webModules = new ArrayList<>();
    static List<Class<? extends WebModule>> registers = new ArrayList<>();

    public static void register(Class<? extends WebModule> controller) {
        registers.add(controller);
    }

    public static void init() {
        Web.acceptEndSlash();
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

    /**
     * Method to treat paths with end slash
     */
    public static void acceptEndSlash() {
        Spark.before(new Filter() {
            @Override
            public void handle(Request request, Response response) throws Exception {
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
}