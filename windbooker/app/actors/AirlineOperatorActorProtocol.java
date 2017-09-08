package actors;

public class AirlineOperatorActorProtocol {

    /**
     *
     */
    public static class GetAllFlights { public GetAllFlights() {} }

    /**
     *
     */
    public static class GetAvailableSeatsForFlight {

        private String flightCode;

        public GetAvailableSeatsForFlight(String flightCode) {
            this.flightCode = flightCode;
        }

        public String getFlightCode() {
            return this.flightCode;
        }
    }
}
