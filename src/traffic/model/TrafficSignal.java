package traffic.model;

public class TrafficSignal {
    private int intersectionId;
    private String status;
    private int greenSeconds;

    public TrafficSignal(int intersectionId) {
        this.intersectionId = intersectionId;
        this.status = "RED";
        this.greenSeconds = 0;
    }


    public int getIntersectionId() {
        return intersectionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getGreenSeconds() {
        return greenSeconds;
    }

    public void setGreenSeconds(int greenSeconds) {
        if (greenSeconds < 0) {
            greenSeconds = 0;
        }
        this.greenSeconds = greenSeconds;
    }
}
