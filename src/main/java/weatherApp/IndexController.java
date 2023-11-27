package weatherApp;

import data.subscriptions.Subscription;
import data.subscriptions.SubscriptionsAccess;
import static logging.AppLogger.*;

import data.users.UsersAccess;
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
            logDebug("User "+ UsersAccess.getUserById(request.session().attribute("loggedIn")).getName() + " is logged In, loading index page");

            Subscription subscription = SubscriptionsAccess.getUserSubscription(request.session().attribute("loggedIn"));
            model.put("subscription", subscription);
            return VLEngine.render(request, model, "/velocity/index.vm");
        }
        logDebug("No user is logged in, redirecting to the register page");
        response.redirect("/register/");
        return null;
    };
}
