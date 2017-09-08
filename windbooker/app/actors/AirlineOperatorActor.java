package actors;

import actors.AirlineOperatorActorProtocol.*;
import akka.actor.UntypedAbstractActor;
import akka.actor.Props;

import java.util.*;

public class AirlineOperatorActor extends UntypedAbstractActor {

    private String opCode;
    private HashMap<String, Integer> designatedFlights;

    public AirlineOperatorActor(String opCode, HashMap<String, Integer> designatedFlights) {
        this.opCode = opCode;
        this.designatedFlights = designatedFlights;
    }

    public static Props getProps(String opCode, HashMap<String, Integer> designatedFlights) {
        return Props.create(AirlineOperatorActor.class, () -> new AirlineOperatorActor(opCode, designatedFlights));
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
        sender().tell(designatedFlights.keySet(), self());
    }

    private void reactionToGetAvailableSeatsForFlight(GetAvailableSeatsForFlight msg) {
        sender().tell(designatedFlights.get(msg.getFlightCode()), self());
    }
}
