package controllers;

import actors.AirlineOperatorActor;
import actors.AirlineOperatorActorProtocol.*;
import actors.BookingActor;
import actors.BookingActorProtocol.*;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import beans.TripBean;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import scala.concurrent.Future;
import scala.concurrent.Await;
import scala.concurrent.Promise;
import scala.concurrent.duration.Duration;
import play.mvc.*;
import beans.TripBean;

import java.lang.reflect.Executable;
import java.util.*;

import static akka.pattern.Patterns.ask;

/* JSON Libraries */
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import javax.inject.Singleton;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Singleton
public class HomeController extends Controller {

    private final String SUCCESS_STATUS = "success";
    private final String ERROR_STATUS = "error";
    private final String[] OPERATORS = new String[]{"AA", "BA", "CA"};

    private ActorSystem windbooker;
    private ActorRef bookingActor;
    private ActorRef aaActor;
    private ActorRef baActor;
    private ActorRef caActor;
    private Timeout defaultTimeOut;
    private Map<String, Integer> flights; // don't need this

    private HashMap<String, Integer> aaDesignatedFlights;
    private HashMap<String, Integer> baDesignatedFlights;
    private HashMap<String, Integer> caDesignatedFlights;
    // Airline operator registry
    // JDBC Connector

    // Constructor
    HomeController() {
        // Create airline operator registry
        // Create JDBC Connector
        createFlightsList();

        // initialize actor system
        windbooker = ActorSystem.create("windbooker");

        // initialize actors
        bookingActor = windbooker.actorOf(BookingActor.getProps(), "booking");
        aaActor = windbooker.actorOf(AirlineOperatorActor.getProps("AA", aaDesignatedFlights), "AmericanAirlines");
        baActor = windbooker.actorOf(AirlineOperatorActor.getProps("BA", baDesignatedFlights), "BritishAirways");
        caActor = windbooker.actorOf(AirlineOperatorActor.getProps("CA", caDesignatedFlights), "AirChina");

        defaultTimeOut = new Timeout(Duration.create(5, "seconds"));

        createFlightsList();
    }

    // I MIGHT NOT NEED THIS
    private void createFlightsList() {
        aaDesignatedFlights = new HashMap<>();
        baDesignatedFlights = new HashMap<>();
        caDesignatedFlights = new HashMap<>();

        aaDesignatedFlights.put("AA001", 3);
        aaDesignatedFlights.put("AA002", 1);

        baDesignatedFlights.put("BA001", 1);

        caDesignatedFlights.put("CA001", 1);
        caDesignatedFlights.put("CA002", 1);
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    /**
     *
     * @return
     */
    public Result getBookedTrips() {
        ObjectNode response = Json.newObject();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<String> result;

        GetAllBookedTrips message = new GetAllBookedTrips();

        Future<Object> future = ask(bookingActor, message, defaultTimeOut);
        try {
            result = (ArrayList<String>) Await.result(future, defaultTimeOut.duration());
        } catch (Exception e) {
            // e.printStackTrace(System.out);
            result = null;
        }

        response.put("status", SUCCESS_STATUS);
        if (result != null) {
            response.putPOJO("trips", result.toArray( new String[result.size()]));
        } else {
            response.putPOJO("trips", new String[]{});
        }

        try {
            return ok(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.out);
            return ok(response);
        }
    }

    /**
     *
     * @param tripID
     * @return
     */
    public Result getTripSegments(String tripID) {
        ObjectNode response = Json.newObject();
        ObjectMapper mapper = new ObjectMapper();
        TripBean result;

        GetBookedTrip message = new GetBookedTrip(tripID);

        Future<Object> future = ask(bookingActor, message, defaultTimeOut);
        try {
            // What happens if the id does not exist in the database?
            result = (TripBean) Await.result(future, defaultTimeOut.duration());
        } catch (Exception e) {
            e.printStackTrace(System.out);
            result = null;
        }

        if (result != null) {
            response.put("status", SUCCESS_STATUS)
                    .putPOJO("segments", result.getSegments());
        } else {
            response.put("status", ERROR_STATUS)
                    .put("message", "Unable to get trip details for " + tripID);
        }

        try {
            return ok(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
        } catch (JsonProcessingException e) {
            // e.printStackTrace(System.out);
            return ok(response);
        }
    }

    /**
     *
     * @return
     */
    public Result getAirlineOperators() {
        ObjectNode response = Json.newObject();
        ObjectMapper mapper = new ObjectMapper();

        response.put("status", SUCCESS_STATUS);
        response.putPOJO("operators", OPERATORS);

        try {
            return ok(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.out);
            return ok(response);
        }
    }

    private ActorRef determineAirlineOperator(String operator) {
        ActorRef airlineOperator;
        switch (operator) {
            case "AA":
                airlineOperator = aaActor;
                break;
            case "BA":
                airlineOperator = baActor;
                break;
            case "CA":
                airlineOperator = caActor;
                break;
            default:
                airlineOperator = null;
                break;
        }
        return airlineOperator;
    }

    /**
     *
     * @param operator
     * @return
     */
    public Result getFlightsAirlineOperates(String operator) {
        ObjectNode response = Json.newObject();
        ObjectMapper mapper = new ObjectMapper();
        Set<String> result;

        ActorRef airlineOperator = determineAirlineOperator(operator);
        if (airlineOperator != null) {
            GetAllFlights message = new GetAllFlights();
            Future<Object> future = ask(airlineOperator, message, defaultTimeOut);
            try {
                result = (Set<String>) Await.result(future, defaultTimeOut.duration());
            } catch (Exception e) {
                // e.printStackTrace(System.out);
                result = null;
            }

            if (result != null) {
                response.put("status", SUCCESS_STATUS)
                        .putPOJO("flights", result.toArray(new String[result.size()]));
            } else {
                response.put("status", ERROR_STATUS)
                        .put("message", "Unable to get flights for operator " + operator);
            }
        } else {
            response.put("status", ERROR_STATUS)
                    .put("message", "Operator " + operator + " does not exists. " +
                            "Use /operators to get a list of available operators.");
        }

        try {
            return ok(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.out);
            return ok(response);
        }
    }

    /**
     *
     * @param operator
     * @param flightCode
     * @return
     */
    public Result getAvailableFlightSeats(String operator, String flightCode) {
        ObjectNode response = Json.newObject();
        ObjectMapper mapper = new ObjectMapper();
        Integer result;

        ActorRef airlineOperator = determineAirlineOperator(operator);

        if (airlineOperator != null) {
            GetAvailableSeatsForFlight message = new GetAvailableSeatsForFlight(flightCode);
            Future<Object> future = ask(airlineOperator, message, defaultTimeOut);
            try {
                result = (Integer) Await.result(future, defaultTimeOut.duration());
            } catch (Exception e) {
                // e.printStackTrace(System.out);
                result = null;
            }

            if (result != null) {
                response.put("status", SUCCESS_STATUS)
                        .putPOJO("seats", result);
            } else {
                response.put("status", ERROR_STATUS)
                        .put("message", "Flight " + flightCode + " does not exist. " +
                                "Use /operators/:operator/flights to get a list of flights the airline operator manages.");
            }
        } else {
            response.put("status", ERROR_STATUS)
                    .put("message", "Operator " + operator + " does not exist. " +
                            "Use /operators to get a list of available operators.");
        }

        try {
            return ok(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.out);
            return ok(response);
        }
    }

    /**
     *
     * @param from
     * @param to
     * @return
     */
    public Result postBookTrip(String from, String to) {
        return ok("from: " + from + " to: " + to);
    }



}
