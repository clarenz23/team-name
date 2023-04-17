package TeamJMCG;

import java.text.DecimalFormat;

public class Part {
    private final int partNumber;
    private final double arrivalTime;
    private final double serviceTime;
    private double seizeResourceTime;

    public Part(int partNumber, double arrivalTime, double serviceTime) {
        this.partNumber = partNumber;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.seizeResourceTime = -1;
    }

    /**
     Default constructor for a Part object.
     Initializes the part number, arrival time, and service time to -1.
     */
    public Part() {
        this.partNumber = -1;
        this.arrivalTime = -1;
        this.serviceTime = -1;
    }

    /**
     Gets the time at which the resource was seized by the part.
     */
    public double getSeizeResourceTime() {
        return seizeResourceTime;
    }

    /**
     Sets the time at which the resource was seized by the part.
     */
    public void setSeizeResourceTime(double seizeResourceTime) {
        this.seizeResourceTime = seizeResourceTime;
    }

    /**
     Gets the time at which the part will depart from the system.
     */
    public double getDepartureTime() {
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedValue = df.format(seizeResourceTime + serviceTime);
        return Double.parseDouble(formattedValue);
    }

    /**
     Gets the part number.
     */
    public int getPartNumber() {
        return partNumber;
    }

    /**
     Gets the arrival time of the part.
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /**
     Returns a string representation of the arrival time.
     */
    public String toString() {
        return Double.toString(arrivalTime);
    }
}
