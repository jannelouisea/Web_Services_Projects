package beans;

public class FlightBean {

    private String flightCode;
    private Integer availableSeats;

    public FlightBean(String flightCode, Integer initNumOfAvailableSeats) {
        this.flightCode = flightCode;
        availableSeats = initNumOfAvailableSeats;
    }

    public boolean hasSeats() {
        return availableSeats > 0 ? true : false;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void reserveASeat() {
        availableSeats--;
    }

}
