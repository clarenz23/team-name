package TeamJMCG;

import java.text.DecimalFormat;
import java.util.*;

public class SimulationProcess {
    private final ArrayList<Object[]> nextPart; // Holds each step of the simulation as an array of objects
    private final ArrayList<Part> partsData; // Holds data for each part used in the simulation

    public SimulationProcess(double simulationTime) {
        nextPart = new ArrayList<>();
        partsData = new ArrayList<>();
        partsData.add(new Part(1, 0.00, 2.90));
        partsData.add(new Part(2, 1.73, 1.76));
        partsData.add(new Part(3, 3.08, 3.39));
        partsData.add(new Part(4, 3.79, 4.52));
        partsData.add(new Part(5, 4.41, 4.46));
        partsData.add(new Part(6, 18.69, 4.36));
        partsData.add(new Part(7, 19.39, 2.07));
        partsData.add(new Part(8, 34.91, 3.36));
        partsData.add(new Part(9, 38.06, 2.37));
        partsData.add(new Part(10, 39.82, 5.38));
        partsData.add(new Part(11, 40.82, 0.0));
        simulate(simulationTime);
    }

    private void simulate(double simulationTime) {
        System.out.println("-------Just Finished Event ------------- Variables ---------------------- Attributes ------------------------------------- Statistical Accumulators ------------------");
        System.out.printf("%-12s %-9s %-15s %-8s %-14s %-24s %-20s %-4s %-4s %-6s %-6s %-6s %-6s %-6s %-5s %-5s%n", "Entity No", "Time t",
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


        nextPart.add(new Object[] {0, time, "Init", 0, 0, inQueue, inService,  0, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0.0});
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
                maxTimeInSystem = getTimeInSystem(entityNo, time)[0];
                totalTimeInSystem = getTimeInSystem(entityNo, time)[1];
                maxWaitingTime = getWaitingTime(entityNo, time)[0];
                totalWaitingTime = getWaitingTime(entityNo, time)[1];
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

            nextPart.add(new Object[] {entityNo, time, eventType, qt, bt, inQueue, inService, producedParts, partsPassedQueue, totalWaitingTime,
                    maxWaitingTime, totalTimeInSystem, maxTimeInSystem, areaUnderQueueLengthCurve, maxQueue, areaUnderSystemLengthCurve});
            printRow();

            boolean runClock = true;
            while (runClock) {
                time = roundOff(time + 0.01);
                if (checkArrival(time) || checkDeparture(time)) { runClock = false; }
                if (time > simulationTime) {
                    areaUnderQueueLengthCurve = getAreaQ(time);
                    areaUnderSystemLengthCurve = getAreaB(time);
                    nextPart.add(new Object[] {"-", time, "End", qt, bt, inQueue, inService,  producedParts, partsPassedQueue, totalWaitingTime,
                            maxWaitingTime, totalTimeInSystem, maxTimeInSystem, areaUnderQueueLengthCurve, maxQueue, areaUnderSystemLengthCurve});
                    printRow();
                    printPerformanceMetric(simulationTime, producedParts, totalWaitingTime, areaUnderQueueLengthCurve,
                            maxWaitingTime, totalTimeInSystem, maxTimeInSystem, areaUnderSystemLengthCurve);
                    return;
                }
            }
        }
    }

    // This method prints a single row of the simulation to the console, with specific formatting for each variable.
    private void printRow() {
        System.out.printf("%-12s %-9s %-15s %-8s %-14s %-24s %-20s %-4s %-4s %-6s %-6s %-6s %-6s %-6s %-5s %-5s%n",
                nextPart.get(nextPart.size() - 1)[0], nextPart.get(nextPart.size() - 1)[1], nextPart.get(nextPart.size() - 1)[2],
                nextPart.get(nextPart.size() - 1)[3], nextPart.get(nextPart.size() - 1)[4], nextPart.get(nextPart.size() - 1)[5],
                nextPart.get(nextPart.size() - 1)[6], nextPart.get(nextPart.size() - 1)[7], nextPart.get(nextPart.size() - 1)[8],
                nextPart.get(nextPart.size() - 1)[9], nextPart.get(nextPart.size() - 1)[10], nextPart.get(nextPart.size() - 1)[11],
                nextPart.get(nextPart.size() - 1)[12], nextPart.get(nextPart.size() - 1)[13], nextPart.get(nextPart.size() - 1)[14],
                nextPart.get(nextPart.size() - 1)[15]);
    }

    // This method prints the performance metrics of the simulation
    private void printPerformanceMetric(double simulationTime, int producedParts, double totalWaitingTime, double areaUnderQueueLengthCurve,
                                        double maxWaitingTime, double totalTimeInSystem, double maxTimeInSystem, double areaUnderSystemLengthCurve) {
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("Simulation completed.");
        System.out.println("Simulation time: " + df.format(simulationTime) + "\n");
        System.out.println("Number of parts processed: " + producedParts);
        System.out.println("Average time spent waiting in queue: " + df.format(totalWaitingTime / producedParts) + " mins/part");
        System.out.println("Average number of parts in queue: " + df.format(areaUnderQueueLengthCurve / simulationTime));
        System.out.println("Longest time spent waiting in queue: " + df.format(maxWaitingTime));
        System.out.println("Average time spent in system: " + df.format(totalTimeInSystem / producedParts) + " mins/part");
        System.out.println("Longest time spent in system: " + df.format(maxTimeInSystem));
        System.out.println("Area under queue length curve: " + df.format(areaUnderQueueLengthCurve));
        System.out.println("Area under system length curve: " + df.format(areaUnderSystemLengthCurve));
        System.out.println();
    }

    /**
     * This method retrieves a value in a specific row and column of a list of objects and returns it as a double
     */
    private double getRowValue(int row, int column) {
        return (double) nextPart.get(row)[column];
    }

    /**
     * This method retrieves an integer value from a specified column in a row of the simulation output
     */
    private int getRowValueInt(int row, int column) {
        return (int) nextPart.get(row)[column];
    }

    /**
     * This method calculate the time in system and return values (ΣTS & TS*)
     */
    private double[] getTimeInSystem(int id, double currentTime) {
        double[] out = new double[2];

        for (Object[] row : nextPart) {
            if ((int) row[0] == id) {
                out[0] = roundOff(currentTime-(double) row[1]);
                out[1] = roundOff(out[0] + getRowValue(nextPart.size() - 1, 11));
                return out;
            }
        }

        out[0] = getRowValue(nextPart.size()-1, 11);
        out[1] = getRowValue(nextPart.size()-1, 12);
        return out;
    }

    /**
     * This method calculate waiting time and return values (ΣWQ & WQ*)
     */
    private double[] getWaitingTime(int id, double currentTime) {
        double[] out = new double[2];

        for (Object[] row : nextPart) {
            if ((int) row[0] == id+1) {
                out[0] = roundOff(currentTime-(double) row[1]);
                out[1] = roundOff(out[0] + getRowValue(nextPart.size() - 1, 9));
                return out;
            }
        }

        out[0] = getRowValue(nextPart.size()-1, 10);
        out[1] = getRowValue(nextPart.size()-1, 9);
        return out;
    }

    /**
     * This method calculate area under the queue length curve (∫Q)
     */
    private double getAreaQ(double currentTime) {
        return roundOff((currentTime - getRowValue(nextPart.size()-1,1)) * (getRowValueInt(nextPart.size()-1,3)) + getRowValue(nextPart.size()-1,13));
    }

    /**
     * This method calculate area under the system length curve (∫B).
     */
    private double getAreaB(double currentTime) {
        return roundOff((currentTime - getRowValue(nextPart.size() - 1 , 1)) * (getRowValueInt(nextPart.size() -1, 4))) + getRowValue(nextPart.size() - 1 , 15);
    }

    /**
     * This method calculate maximum queue (Q*)
     */
    private int getMaxQueue(int currentQueueSize) {
        int max = 0;

        for (Object[] row : nextPart) {
            if ((int)row[3] > max) { max = (int)row[3]; }
        }
        if (max < currentQueueSize) { max = currentQueueSize; }

        return max;
    }

    private void seizeResource(int partID, double seizeTime) {
        for (Part machinePart : partsData) {
            if (machinePart.getPartNumber() == partID) {
                machinePart.setSeizeResourceTime(seizeTime);
                break;
            }
        }
    }

    /**
     * This method checks if any part has arrived at the current simulation time
     */
    private boolean checkArrival(double time) {
        for (Part part : partsData) {
            if (time == part.getArrivalTime()) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method returns true if any part has departed from the system at the current time
     */
    private boolean checkDeparture(double time) {
        for (Part part : partsData) {
            if (part.getSeizeResourceTime() != -1 && time == part.getDepartureTime()) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method return the Part object that has arrived according to the current simulation time
     * If no part has arrived, return an empty Part object
     */
    private Part getArrivedPart(double time) {
        for (Part part : partsData) {
            if (time == part.getArrivalTime()) {
                return part;
            }
        }
        return new Part();
    }

    /**
     * This method returns the part that has departed according to the current time,
     * or a new empty part if none has departed
     */
    private Part getDepartedPart(double time) {
        for (Part part : partsData) {
            if (part.getSeizeResourceTime() != -1 && time == part.getDepartureTime()) {
                return part;
            }
        }
        return new Part();
    }

    /**
     * This method round off a double to two digits
     */
    private double roundOff(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedValue = df.format(value);
        return Double.parseDouble(formattedValue);
    }
}
