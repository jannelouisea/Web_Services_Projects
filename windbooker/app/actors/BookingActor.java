package actors;

import actors.BookingActorProtocol.*;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.japi.pf.ReceiveBuilder;

public class BookingActor extends UntypedAbstractActor {

    public static Props getProps() {
        return Props.create(BookingActor.class);
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof GetAllBookedTrips)
            reactionToGetAllBookedTrips((GetAllBookedTrips) msg);
        else if (msg instanceof GetBookedTrip)
            reactionToGetBookedTrip((GetBookedTrip) msg);
        else
            // unhandled(msg);
            System.out.println("BookingActor: ERROR: Unable to process message");
    }

    private void reactionToGetAllBookedTrips(GetAllBookedTrips msg) {
        sender().tell(msg.getAllBookedTrips(), self());
    }

    private void reactionToGetBookedTrip(GetBookedTrip msg) {
        sender().tell(msg.getBookedTrip(), self());
    }
}
