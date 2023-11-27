package weatherApp;

import data.users.UsersAccess;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

import static logging.AppLogger.*;
public class UserController
{

    public static Route renderLoginPage = (Request request, Response response) ->
    {
        Map<String, Object> model = new HashMap<>();
        return VLEngine.render(request, model, "/velocity/login.vm");
    };

    public static Route renderRegisterPage = (Request request, Response response) ->
    {
        Map<String, Object> model = new HashMap<>();
        model.put("name","");
        model.put("email","");
        return VLEngine.render(request, model, "/velocity/register.vm");
    };
    public static Route login = (Request request, Response response) ->
    {
        logDebug("Attempting login");
        int id = UsersAccess.authenticate(request.queryParams("email"), request.queryParams("password"));
        logDebug("user Authentication, id = "+id);
        Map<String, Object> model = new HashMap<>();
        if(id >0) {
            request.session().attribute("loggedIn",id);
            logDebug("sending to index with User "+UsersAccess.getUserById(id));
            response.redirect("/index/");
        }
        else
        {
            switch (id){
                case UsersAccess.EMPTYFIELD -> model.put("error","Fill All Fields");
                case UsersAccess.UNAUTHENTICATED, UsersAccess.USERNOTEXIST -> model.put("error", "Wrong Email or Password");
            }
            logDebug("Error logging in, message:" + model.get("error"));
            return VLEngine.render(request, model, "/velocity/login.vm");
        }
        return null;
    };

    public static Route logout = (Request request, Response response) ->
    {
        request.session().removeAttribute("loggedIn");
        logDebug("user Logged out, redirecting to index");
        response.redirect("/index/");
        return null;
    };

    public static Route registerUser = (Request request, Response response) ->
    {
        logDebug("Registering a new User");
        String email = request.queryParams("email");
        String name = request.queryParams("name");
        String password = request.queryParams("password");
        String confirmPass = request.queryParams("confirmPass");
        int id = UsersAccess.addUser(name, email, password, confirmPass);
        logDebug("user Creation, response = "+id);
        Map<String, Object> model = new HashMap<>();
        if(id >0) {
            request.session().attribute("loggedIn",id);
            logDebug("sending to index with User "+UsersAccess.getUserById(id));
            response.redirect("/index/");
        }
        else
        {
            switch (id)
            {
                case UsersAccess.PASSANDCONFIRMNOMATCH -> {
                    model.put("error", "Password and Confirm Password does not match");
                    model.put("name",name);
                    model.put("email",email);
                }
                case UsersAccess.EMPTYFIELD -> model.put("error","Fill All Fields");
                case UsersAccess.USERALREADYREGISTERED -> model.put("error","User already Exists");
            }
            logDebug("Error Creating User, message:" + model.get("error"));
            return VLEngine.render(request, model, "/velocity/register.vm");
        }
        return null;
    };
}
