package actors;

import beans.TripBean;

import java.util.ArrayList;

/**
 * This class will hold the different types of messages the
 * BookingActor can receive.
 */
public class BookingActorProtocol {

    /**
     *
     */
    public static class GetAllBookedTrips {

        public GetAllBookedTrips() { }

        public ArrayList<String> getAllBookedTrips() {
            // Real version would connect
            // to the database to get the list of booked trips
            ArrayList<String> bookedTrips = new ArrayList<>();
            bookedTrips.add("trip1");
            bookedTrips.add("trip2");
            return bookedTrips;
        }

    }

    /**
     *
     */
    public static class GetBookedTrip {
        private String tripID;

        public GetBookedTrip(String tripID) {
            this.tripID = tripID;
        }

        public TripBean getBookedTrip() {
            // Real version would connect
            // to the database to get the list of booked trips
            return new TripBean(this.tripID, new String[]{"AA001", "CA002"});
        }
    }

}
