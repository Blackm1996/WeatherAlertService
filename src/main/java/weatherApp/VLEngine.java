package weatherApp;

import data.users.UsersAccess;
import org.apache.velocity.app.VelocityEngine;
import spark.ModelAndView;
import spark.Request;
import spark.template.velocity.VelocityTemplateEngine;

import static logging.AppLogger.*;

import java.util.Map;

public class VLEngine
{
    public static String render(Request request, Map<String, Object> model, String templatePath)
    {
        var userId = request.session().attribute("loggedIn");
        if(userId != null)
            model.put("user", UsersAccess.getUserById((int)userId));
        return strictVelocityEngine().render(new ModelAndView(model, templatePath));
    }
    private static VelocityTemplateEngine strictVelocityEngine() {
        VelocityEngine configuredEngine = new VelocityEngine();
        configuredEngine.setProperty("runtime.references.strict", true);
        configuredEngine.setProperty("resource.loader", "class");
        configuredEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityTemplateEngine(configuredEngine);
    }
}
