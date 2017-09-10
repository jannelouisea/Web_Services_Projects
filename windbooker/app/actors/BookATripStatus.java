package actors;

public enum BookATripStatus {
    INVALID_LOCATIONS("Hello"),
    NO_PATHS_AVAILABLE("There");

    private String status;

    BookATripStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

}
