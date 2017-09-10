package actors;

import actors.BookingActorProtocol.*;
import akka.actor.*;
import akka.actor.dsl.Creators;
import akka.japi.pf.ReceiveBuilder;
import java.util.*;

public class BookingActor extends UntypedAbstractActor {

    private final String DEFAULT_FROM_LOCATION = "X";
    private final String DEFAULT_TO_LOCATION = "Y";

    private final String[] PATH1 = new String[]{"CA001"};
    private final String[] PATH2 = new String[]{"AA001", "BA001"};
    private final String[] PATH3 = new String[]{"AA001", "CA002", "AA002"};
    private final String[] NOPATH = null;

    private ActorRef aaActor;
    private ActorRef baActor;
    private ActorRef caActor;
    private HashMap<String, ActorRef> flightToAirlineMap;

    private FlightsTableConnector flightsTable;

    public static Props getProps(ActorRef aaActor, ActorRef baActor, ActorRef caActor) {
        return Props.create(BookingActor.class, () -> new BookingActor(aaActor, baActor, caActor));
    }

    public BookingActor(ActorRef aaActor, ActorRef baActor, ActorRef caActor) {
        this.aaActor = aaActor;
        this.baActor = baActor;
        this.caActor = caActor;
        this.flightsTable = new FlightsTableConnector();
        this.flightToAirlineMap = new HashMap<>();
        initFlightToAirlineMap();
    }

    private void initFlightToAirlineMap() {
        // Get flights for aaActor
        ArrayList<String> aaFlights = flightsTable.getAllFlightsForOperator("AA");
        for (String flight: aaFlights) {
            this.flightToAirlineMap.put(flight, aaActor);
        }
        // Get flights for baActor
        ArrayList<String> baFlights = flightsTable.getAllFlightsForOperator("BA");
        for (String flight: baFlights) {
            this.flightToAirlineMap.put(flight, baActor);
        }
        // Get flights for caActor
        ArrayList<String> caFlights = flightsTable.getAllFlightsForOperator("CA");
        for (String flight: caFlights) {
            this.flightToAirlineMap.put(flight, caActor);
        }
        System.out.println("Initialized flightToAirlineMap successfully");
    }


    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof GetAllBookedTrips)
            reactionToGetAllBookedTrips((GetAllBookedTrips) msg);
        else if (msg instanceof GetTripSegments)
            reactionToGetTripSegments((GetTripSegments) msg);
        else if (msg instanceof GetAllAirlineOperators)
            reactionToGetAllAirlineOperators((GetAllAirlineOperators) msg);
        else if (msg instanceof  OperatorDoesNotExist)
            sender().tell("", self());
        else if (msg instanceof BookATrip)
            reactionToBookATrip((BookATrip) msg);
        else
            unhandled(msg);
    }

    private void reactionToGetAllBookedTrips(GetAllBookedTrips msg) {
        sender().tell(msg.getAllBookedTrips(), self());
    }

    private void reactionToGetTripSegments(GetTripSegments msg) {
        sender().tell(msg.getTripSegments(), self());
    }

    private void reactionToGetAllAirlineOperators(GetAllAirlineOperators msg) {
        sender().tell(msg.getAllAirlineOperators(), self());
    }

    private void reactionToBookATrip(BookATrip msg) {
        String from = msg.from;
        String to = msg.to;

        if (!isDestinationValid(from, to)) {
            sender().tell(BookATripStatus.INVALID_LOCATIONS.getStatus(), self());
        } else {
            String[] path = determineShortestPath();
            if (path.equals(NOPATH)) {
                sender().tell(BookATripStatus.NO_PATHS_AVAILABLE.getStatus(), self());
            } else {
                System.out.println("Path: " + path.toString());
                String output = "";
                for (String segment: path) {
                    output += segment + ",";
                }
                sender().tell("Path: " + output, self());
            }
        }

    }

    private boolean isDestinationValid(String from, String to) {
        return from.equals(DEFAULT_FROM_LOCATION) && to.equals(DEFAULT_TO_LOCATION);
    }

    private String[] determineShortestPath() {
        HashMap<String, Integer> availableSeats = new HashMap<>();
        for (String flight : this.flightToAirlineMap.keySet()) {
            availableSeats.put(flight, flightsTable.getAvailableSeatsForFlight(flight));
        }

        if (availableSeats.get("CA001") > 0)
            return PATH1;
        else if (availableSeats.get("AA001") > 0 && availableSeats.get("BA001") > 0)
            return PATH2;
        else if (availableSeats.get("AA001") > 0 && availableSeats.get("CA002") > 0 && availableSeats.get("AA002") > 0)
            return PATH3;
        else
            return NOPATH;
    }
}
