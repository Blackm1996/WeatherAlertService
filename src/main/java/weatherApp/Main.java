package weatherApp;

import data.cities.CityAccess;
import data.subscriptions.SubscriptionsAccess;
import data.users.UsersAccess;
import service.WeatherService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import static spark.Spark.*;

import static logging.AppLogger.*;
public class Main {
    public static void main(String[] args) {

        //System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "logs/config/logback.xml");
        CityAccess.generateDataSet();
        int us= UsersAccess.generateInitial();
        if(us > 0)
            SubscriptionsAccess.subscribe(us,32,10,6);
        initializeBackgroundService();
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        port(4567);

        before("*", ((request, response) -> {
            logDebug("Request received "+request.pathInfo());
            if (!request.pathInfo().endsWith("/")) {
                response.redirect(request.pathInfo() + "/");
            }}));

        before("/",(request, response) -> {response.redirect("/index/");});
        //index
        get("index/", IndexController.index);
        //login / logout
        get("login/", UserController.renderLoginPage);
        post("login/", UserController.login);
        post("logout/", UserController.logout);
        //register new user
        get("register/", UserController.renderRegisterPage);
        post("register/", UserController.registerUser);

        get("*", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return VLEngine.render(request, model, "/velocity/NotFound.vm");});
    }
    private static void initializeBackgroundService() {
        WeatherService service = new WeatherService();
        // Create a scheduled executor service
        logDebug("starting weather service");
        try{
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            // Schedule a task to run periodically
            // Fetch weather information for subscribed cities and notify users
            scheduler.scheduleWithFixedDelay(service::ProcessWeather, 0, 10, TimeUnit.MINUTES); // Run every 10 minutes
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logDebug("Shutting down Weather Service...");
                scheduler.shutdown();
            }));
        }
        catch (Exception e)
        {
            logError("Exception starting service: "+e);
        }
    }
}