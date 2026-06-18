package traffic.model;

public class
Intersection {
    private int id;
    private String name;
    private int x;
    private int y;
    private int carsWaiting;
    private int emergencyLevel;
    private int greenTime;
    private String signalStatus;


    public Intersection(int id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.carsWaiting = 0;
        this.emergencyLevel = 0;
        this.greenTime = 0;
        this.signalStatus = "RED";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCarsWaiting() {
        return carsWaiting;
    }

    public void setCarsWaiting(int carsWaiting) {
        if (carsWaiting < 0) {
            carsWaiting = 0;
        }
        this.carsWaiting = carsWaiting;
    }

    public int getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(int emergencyLevel) {
        if (emergencyLevel < 0) {
            emergencyLevel = 0;
        }
        if (emergencyLevel > 2) {
            emergencyLevel = 2;
        }
        this.emergencyLevel = emergencyLevel;
    }

    public int getGreenTime() {
        return greenTime;
    }

    public void setGreenTime(int greenTime) {
        if (greenTime < 0) {
            greenTime = 0;
        }
        this.greenTime = greenTime;
    }

    public String getSignalStatus() {
        return signalStatus;
    }

    public void setSignalStatus(String signalStatus) {
        this.signalStatus = signalStatus;
    }

    public String getEmergencyText() {
        if (emergencyLevel == 2) {
            return "Ambulance";
        }
        if (emergencyLevel == 1) {
            return "Accident";
        }
        return "Normal";
    }

    public String toString() {
        return id + " - " + name;
    }
}
