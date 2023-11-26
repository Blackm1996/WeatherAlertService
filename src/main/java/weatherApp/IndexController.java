package weatherApp;

import data.cities.City;
import data.cities.CityAccess;
import data.subscriptions.Subscription;
import data.subscriptions.SubscriptionsAccess;
import logging.AppLogger;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class IndexController
{
    public static Route index = (Request request, Response response) ->
    {
        Map<String, Object> model = new HashMap<>();
        if(request.session().attribute("loggedIn") != null) {
            Subscription subscription = SubscriptionsAccess.getUserSubscription(request.session().attribute("loggedIn"));
            if(subscription != null)
            {
                model.put("subscription", subscription);
            }
            return VLEngine.render(request, model, "/velocity/index.vm");
        }
        response.redirect("/register/");
        return null;
    };
}
