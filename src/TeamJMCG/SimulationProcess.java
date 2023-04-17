package TeamJMCG;

import java.text.DecimalFormat;
import java.util.*;

public class SimulationProcess {
    private final ArrayList<Object[]> REPLICATION; // Holds each row of the simulation
    private final ArrayList<Part> MACHINE_PARTS; // Holds each machine part for the simulation

    public SimulationProcess(double simulationTime) {
        REPLICATION = new ArrayList<>();
        MACHINE_PARTS = new ArrayList<>();
        MACHINE_PARTS.add(new Part(1, 0.00, 2.90));
        MACHINE_PARTS.add(new Part(2, 1.73, 1.76));
        MACHINE_PARTS.add(new Part(3, 3.08, 3.39));
        MACHINE_PARTS.add(new Part(4, 3.79, 4.52));
        MACHINE_PARTS.add(new Part(5, 4.41, 4.46));
        MACHINE_PARTS.add(new Part(6, 18.69, 4.36));
        MACHINE_PARTS.add(new Part(7, 19.39, 2.07));
        MACHINE_PARTS.add(new Part(8, 34.91, 3.36));
        MACHINE_PARTS.add(new Part(9, 38.06, 2.37));
        MACHINE_PARTS.add(new Part(10, 39.82, 5.38));
        MACHINE_PARTS.add(new Part(11, 40.82, 0.0));
        simulate(simulationTime);
    }

    private void simulate(double simulationTime) {
        System.out.println("-----Just Finished Event ------------- Variables ---------------------- Attributes ------------------------------------- Statistical Accumulators ------------------");
        // adjust spaces between variable as needed
        System.out.printf("|%6s |%6s |%8s |%6s |%6s |%20s |%20s |%6s |%6s |%6s |%6s |%6s |%6s |%6s |%6s |%6s |\n", "Entity No", "Time t",
                "Event Type", "Q(t)", "B(t)", "In Queue", "In Service", "P", "N", "ΣWQ", "WQ*", "ΣTS", "TS*", "∫Q", "Q*", "∫B");

        // just finished event
        double time = 0;
        int entityNo;
        String eventType;

        // variables
        int qt;
        int bt = 0;

        // attributes
        Queue<Part> inQueue = new LinkedList<>();
        Queue<Part> inService = new LinkedList<>();

        // statistical accumulator
        int producedParts = 0;
        int partsPassedQueue = 0;
        double totalWaitingTime = 0;
        double maxWaitingTime = 0;
        double totalTimeInSystem = 0;
        double maxTimeInSystem = 0;
        double areaUnderQueueLengthCurve;
        int maxQueue;
        double areaUnderSystemLengthCurve;


        REPLICATION.add(new Object[] {0, time, "Init", 0, 0, inQueue, inService,  0, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0.0});
        printRow();

        for (int i = 0; i < 100; i++) {
            if (checkArrival(time)) {
                Part part = getArrivedPart(time);
                entityNo = part.getPartNumber();
                eventType = "Arr";
                if (bt == 1) {
                    inQueue.add(part);
                } else {
                    inService.add(part);
                    seizeResource(part.getPartNumber(), time);
                    partsPassedQueue++;
                }
            } else {
                Part part = getDepartedPart(time);
                entityNo = part.getPartNumber();
                eventType = "Dep";
                if (!inService.isEmpty()) {
                    inService.remove();
                }
                producedParts++;
                maxTimeInSystem = getTS(entityNo, time)[0];
                totalTimeInSystem = getTS(entityNo, time)[1];
                maxWaitingTime = getWQ(entityNo, time)[0];
                totalWaitingTime = getWQ(entityNo, time)[1];
                if (!inQueue.isEmpty()) {
                    partsPassedQueue++;
                    seizeResource(inQueue.peek().getPartNumber(), time);
                    inService.add(inQueue.remove());
                }
            }
            qt = inQueue.size();
            bt = inService.size();
            maxQueue = getMaxQueue(qt);
            areaUnderQueueLengthCurve = getAreaQ(time);
            areaUnderSystemLengthCurve = getAreaB(time);

            REPLICATION.add(new Object[] {entityNo, time, eventType, qt, bt, inQueue, inService, producedParts, partsPassedQueue, totalWaitingTime,
                    maxWaitingTime, totalTimeInSystem, maxTimeInSystem, areaUnderQueueLengthCurve, maxQueue, areaUnderSystemLengthCurve});
            printRow();

            boolean runClock = true;
            while (runClock) {
                time = roundOff(time + 0.01);
                if (checkArrival(time) || checkDeparture(time)) { runClock = false; }
                if (time > simulationTime) {
                    areaUnderQueueLengthCurve = getAreaQ(time);
                    areaUnderSystemLengthCurve = getAreaB(time);
                    REPLICATION.add(new Object[] {"-", time, "End", qt, bt, inQueue, inService,  producedParts, partsPassedQueue, totalWaitingTime,
                            maxWaitingTime, totalTimeInSystem, maxTimeInSystem, areaUnderQueueLengthCurve, maxQueue, areaUnderSystemLengthCurve});
                    printRow();
                    return;
                }
            }
        }
    }

    private void printRow() {
        // adjust spaces between variables as needed
        System.out.printf("|%6s |%6s |%8s |%6s |%6s |%20s |%20s |%6s |%6s |%6s |%6s |%6s |%6s |%6s |%6s |%6s |\n",
                REPLICATION.get(REPLICATION.size() - 1)[0], REPLICATION.get(REPLICATION.size() - 1)[1], REPLICATION.get(REPLICATION.size() - 1)[2],
                REPLICATION.get(REPLICATION.size() - 1)[3], REPLICATION.get(REPLICATION.size() - 1)[4], REPLICATION.get(REPLICATION.size() - 1)[5],
                REPLICATION.get(REPLICATION.size() - 1)[6], REPLICATION.get(REPLICATION.size() - 1)[7], REPLICATION.get(REPLICATION.size() - 1)[8],
                REPLICATION.get(REPLICATION.size() - 1)[9], REPLICATION.get(REPLICATION.size() - 1)[10], REPLICATION.get(REPLICATION.size() - 1)[11],
                REPLICATION.get(REPLICATION.size() - 1)[12], REPLICATION.get(REPLICATION.size() - 1)[13], REPLICATION.get(REPLICATION.size() - 1)[14],
                REPLICATION.get(REPLICATION.size() - 1)[15]);
    }

    /**
     * Retrieves a value in a row as a double.
     */
    private double getRowValue(int row, int column) {
        return (double) REPLICATION.get(row)[column];
    }

    /**
     * Retrieves a value in a row as an int.
     */
    private int getRowValueInt(int row, int column) {
        return (int) REPLICATION.get(row)[column];
    }

    /**
     * Calculate and return time in system values (ΣTS & TS*).
     */
    private double[] getTS(int id, double currentTime) {
        double[] out = new double[2];

        for (Object[] row : REPLICATION) {
            if ((int) row[0] == id) {
                out[0] = roundOff(currentTime-(double) row[1]);
                out[1] = roundOff(out[0] + getRowValue(REPLICATION.size() - 1, 11));
                return out;
            }
        }

        out[0] = getRowValue(REPLICATION.size()-1, 11);
        out[1] = getRowValue(REPLICATION.size()-1, 12);
        return out;
    }

    /**
     * Calculate and return waiting time values (ΣWQ & WQ*).
     */
    private double[] getWQ(int id, double currentTime) {
        double[] out = new double[2];

        for (Object[] row : REPLICATION) {
            if ((int) row[0] == id+1) {
                out[0] = roundOff(currentTime-(double) row[1]);
                out[1] = roundOff(out[0] + getRowValue(REPLICATION.size() - 1, 9));
                return out;
            }
        }

        out[0] = getRowValue(REPLICATION.size()-1, 10);
        out[1] = getRowValue(REPLICATION.size()-1, 9);
        return out;
    }

    /**
     * Calculate area under the queue length curve (∫Q)
     */
    private double getAreaQ(double currentTime) {
        return roundOff((currentTime - getRowValue(REPLICATION.size()-1,1)) * (getRowValueInt(REPLICATION.size()-1,3)) + getRowValue(REPLICATION.size()-1,13));
    }

    /**
     * Calculate area under the system length curve (∫B).
     */
    private double getAreaB(double currentTime) {
        return roundOff((currentTime - getRowValue(REPLICATION.size() - 1 , 1)) * (getRowValueInt(REPLICATION.size() -1, 4))) + getRowValue(REPLICATION.size() - 1 , 15);
    }

    /**
     * Calculate maximum queue (Q*)
     */
    private int getMaxQueue(int currentQueueSize) {
        int max = 0;

        for (Object[] row : REPLICATION) {
            if ((int)row[3] > max) { max = (int)row[3]; }
        }
        if (max < currentQueueSize) { max = currentQueueSize; }

        return max;
    }

    private void seizeResource(int partID, double seizeTime) {
        for (Part machinePart : MACHINE_PARTS) {
            if (machinePart.getPartNumber() == partID) {
                machinePart.setSeizeResourceTime(seizeTime);
                break;
            }
        }
    }

    /**
     * Returns true if any part has arrived according to the current time
     */
    private boolean checkArrival(double time) {
        for (Part part : MACHINE_PARTS) {
            if (time == part.getArrivalTime()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if any part has departed according to the current time
     */
    private boolean checkDeparture(double time) {
        for (Part part : MACHINE_PARTS) {
            if (part.getSeizeResourceTime() != -1 && time == part.getDepartureTime()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the part that has arrived according to the current time
     */
    private Part getArrivedPart(double time) {
        for (Part part : MACHINE_PARTS) {
            if (time == part.getArrivalTime()) {
                return part;
            }
        }
        return new Part();
    }

    /**
     * Return the part that has departed according to the current time
     */
    private Part getDepartedPart(double time) {
        for (Part part : MACHINE_PARTS) {
            if (part.getSeizeResourceTime() != -1 && time == part.getDepartureTime()) {
                return part;
            }
        }
        return new Part();
    }

    /**
     * Round off a double to two digits.
     */
    private double roundOff(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedValue = df.format(value);
        return Double.parseDouble(formattedValue);
    }
}

class Part {
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
