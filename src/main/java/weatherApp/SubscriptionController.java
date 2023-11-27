package weatherApp;

import data.cities.CityAccess;
import data.subscriptions.Subscription;
import data.subscriptions.SubscriptionsAccess;
import data.users.UsersAccess;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

import static logging.AppLogger.*;

public class SubscriptionController
{
    public static Route renderNewSubscriptionPage = (Request request, Response response) ->
    {
        Map<String, Object> model = new HashMap<>();
        Integer userId = request.session().attribute("loggedIn");
        logDebug("Attempt rendering new subscription page");
        if(userId !=null && SubscriptionsAccess.getUserSubscription(userId)==null)
        {
            model.put("cities", CityAccess.getCities().values());
        }
        else {
            logDebug("No User is logged in or user is already subscribed, redirecting to index");
            response.redirect("/index/");
        }
        return VLEngine.render(request, model, "/velocity/subscribe.vm");
    };

    public static Route renderAllSubscriptionsPage = (Request request, Response response) ->
    {
        Map<String, Object> model = new HashMap<>();
        logDebug("Attempt rendering subscriptions page");
        if(request.session().attribute("loggedIn") !=null)
        {
            model.put("subscriptions",SubscriptionsAccess.getSubscriptions());
        }
        else {
            logDebug("No User is logged in, redirecting to index");
            response.redirect("/index/");
        }
        return VLEngine.render(request, model, "/velocity/subscriptions.vm");
    };
    public static Route removeSubscription = (Request request, Response response) ->
    {
        if(request.session().attribute("loggedIn") !=null)
        {
            SubscriptionsAccess.unsubscribe(request.session().attribute("loggedIn"));
            logDebug("Unsubscribe successfully");
        }
        else
            logDebug("No User is logged in");
        logDebug("redirecting to index");
        response.redirect("/index/");
        return null;
    };

    public static Route subscribe = (Request request, Response response) ->
    {
        Map<String ,Object> model = new HashMap<>();
        logDebug("Attempting subscription");
        try {
            if(request.session().attribute("loggedIn") !=null)
            {
                int userId = Integer.parseInt(request.queryParams("userId"));
                int cityId = Integer.parseInt(request.queryParams("cityId"));
                int minTemp = Integer.parseInt(request.queryParams("minTemp"));
                int maxTemp = Integer.parseInt(request.queryParams("maxTemp"));

                int result = SubscriptionsAccess.subscribe(userId, cityId, maxTemp,minTemp);

                switch (result)
                {
                    case (SubscriptionsAccess.SUCCESSFUL)-> {
                        logDebug("User subscribed successfully");
                        response.redirect("/index/");
                    }
                    case (SubscriptionsAccess.ERROR)-> {
                        model.put("error", "There was an error, please reload the page and try again");
                        logDebug("Error in userId or cityId does not exists");
                    }
                    case (SubscriptionsAccess.ALREADYSUB)-> {
                        logDebug("User already subscribed");
                        model.put("error", "You are already subscribed to a city, please reload the page");
                    }
                    case (SubscriptionsAccess.TEMPERROR)-> {
                        logDebug("minimum temperature is bigger than maximum temperature");
                        model.put("error", "Minimum temperature is bigger than maximum temperature");
                    }
                }
            }
            else {
                logDebug("No User is logged in");
                model.put("error", "You are not logged in, please login or create a new user before subscribing to a new city");
            }
        }
        catch (NumberFormatException nEx)
        {
            logDebug("Invalid temperature values");
            model.put("error", "A temperature must be a number, please enter a valid value");
        }
        return VLEngine.render(request, model, "/velocity/subscribe.vm");
    };
}
