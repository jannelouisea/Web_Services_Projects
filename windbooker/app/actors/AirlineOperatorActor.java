package actors;

import actors.AirlineOperatorActorProtocol.*;
import akka.actor.UntypedAbstractActor;
import akka.actor.Props;

import java.util.*;

public class AirlineOperatorActor extends UntypedAbstractActor {

    private String opCode;
    private FlightsTableConnector flightsTable;

    public AirlineOperatorActor(String opCode) {
        flightsTable = new FlightsTableConnector();
        this.opCode = opCode;
    }

    public static Props getProps(String opCode) {
        return Props.create(AirlineOperatorActor.class, () -> new AirlineOperatorActor(opCode));
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof GetAllFlights)
            reactionToGetAllFlights();
        else if (msg instanceof  GetAvailableSeatsForFlight)
            reactionToGetAvailableSeatsForFlight((GetAvailableSeatsForFlight) msg);
        else
            // unhandled(msg);
            System.out.println("AirlineActor: ERROR: Unable to process message");
    }

    private void reactionToGetAllFlights() {
        ArrayList<String> flights = flightsTable.getAllFlightsForOperator(this.opCode);
        sender().tell(flights.toArray(new String[flights.size()]), self());
    }

    private void reactionToGetAvailableSeatsForFlight(GetAvailableSeatsForFlight msg) {
        Integer seats = flightsTable.getAvailableSeatsForFlight(msg.getFlightCode());
        sender().tell(seats, self());
    }
}
