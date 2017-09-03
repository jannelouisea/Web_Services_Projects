package controllers;

import play.mvc.*;

/* These are uses for the handleupdates() method */
import com.google.inject.Inject;
import play.data.DynamicForm;
import play.data.FormFactory;

/* JSON Libraries */
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    @Inject
    FormFactory formFactory;
    public Result handleupdates() {
        DynamicForm dynamicForm = formFactory.form().bindFromRequest();

        // Logger.info("Username is: " + dynamicForm.get("username"));
        // Logger.info("Time is: " + dynamicForm.get("timestamp"));
        // Logger.info("Latitude is: " + dynamicForm.get("latitude"));
        // Logger.info("Longitude is: " + dynamicForm.get("longitude"));

        System.out.println("Username is: " + dynamicForm.get("username"));
        System.out.println("Time is: " + dynamicForm.get("timestamp"));
        System.out.println("Latitude is: " + dynamicForm.get("latitude"));
        System.out.println("Longitude is: " + dynamicForm.get("longitude"));

        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setUsername(dynamicForm.get("username"));
        locationInfo.setTimestamp(dynamicForm.get("timestamp"));
        locationInfo.setLatitude(dynamicForm.get("latitude"));
        locationInfo.setLongitude(dynamicForm.get("longitude"));

        // return ok("ok, I received POST data. That's all...\n");
        return ok(Json.toJson(locationInfo));
    }

}
