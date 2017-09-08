package beans;

public class TripBean {
    private String id;
    private String[] segments;

    public TripBean(String id, String[] segments) {
        this.id = id;
        this.segments = segments;
    }

    public String getId() {
        return id;
    }

    public String[] getSegments() {
        return segments;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSegments(String[] segments) {
        this.segments = segments;
    }
}
