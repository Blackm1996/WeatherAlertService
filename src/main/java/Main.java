import data.cities.CityAccess;
import data.subscriptions.SubscriptionsAccess;
import data.users.UsersAccess;
import service.WeatherService;
import weatherApp.IndexController;
import weatherApp.SubscriptionController;
import weatherApp.UserController;
import weatherApp.VLEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import static spark.Spark.*;

import static logging.AppLogger.*;
public class Main {

    public static void main(String[] args) {

        CityAccess.generateDataSet();
        int us= UsersAccess.generateInitial();
        if(us > 0)
            SubscriptionsAccess.subscribe(us,32,10,6);
        initializeBackgroundService();
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        port(4567);

        before("*", ((request, response) -> {
            logDebug("Request received: "+request.pathInfo()+" methode: "+request.requestMethod());
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
        //Subscription
        get("subscription/", SubscriptionController.renderNewSubscriptionPage);
        post("deleteSubscription/", SubscriptionController.removeSubscription);
        post("subscription/", SubscriptionController.subscribe);
        get("subscriptions/", SubscriptionController.renderAllSubscriptionsPage);

        get("*", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return VLEngine.render(request, model, "/velocity/NotFound.vm");});
    }
    private static void initializeBackgroundService() {
        WeatherService service = new WeatherService();
        // Create a scheduled executor service
        logDebug("starting weather service");
        try{
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            // Schedule a task to run periodically
            // Run every 10 minutes since openweathermap's data is updated every 10 minutes
            scheduler.scheduleWithFixedDelay(service::ProcessWeather, 0, 10, TimeUnit.MINUTES);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logDebug("Shutting down Weather Service...");
                scheduler.shutdown();
                service.getExecutorService().shutdown();
                service.getEmailService().getEmailExecutor().shutdown();
            }));
        }
        catch (Exception e)
        {
            logError("Exception starting service: "+e);
        }
    }
}