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

        BookingActorJDBCConnector jdbcConnector;

        public GetAllBookedTrips() {
            jdbcConnector = new BookingActorJDBCConnector();
        }

        public String[] getAllBookedTrips() {
            ArrayList<String> result = jdbcConnector.selectAllBookedTrips();
            return result.toArray(new String[result.size()]);
        }

    }

    /**
     *
     */
    public static class GetTripSegments {
        BookingActorJDBCConnector jdbcConnector;
        private String tripID;

        public GetTripSegments(String tripID) {
            jdbcConnector = new BookingActorJDBCConnector();
            this.tripID = tripID;
        }

        public String[] getTripSegments() {
            String segmentsString = jdbcConnector.selectTripSegmentsForTrip(this.tripID);
            if (segmentsString != null ) {
                return segmentsString.split(",");
            } else {
                return new String[]{};
            }
        }
    }

    public static class GetAllAirlineOperators {

        OperatorsTableConnector operatorsTable;

        public GetAllAirlineOperators() {
            operatorsTable = new OperatorsTableConnector();
        }

        public String[] getAllAirlineOperators() {
            ArrayList<String> result = operatorsTable.selectAllAirlineOperators();
            return result.toArray(new String[result.size()]);
        }

    }

    public static class OperatorDoesNotExist {
        public OperatorDoesNotExist() {}
    }

    public static class BookATrip {

        public String from;
        public String to;

        public BookATrip(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }

}
