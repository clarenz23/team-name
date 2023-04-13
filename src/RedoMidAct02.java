import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class RedoMidAct02 {
    private static final int NUM_CUSTOMERS = 11; // Number of customers to simulate
    private static final double[] ARRIVAL_TIMES = {0.00, 1.73, 3.08, 3.79, 4.41, 18.69, 19.39, 34.91, 38.06, 39.82, 40.82}; // Arrival times for each customer
    private static final double[] INTERARRIVAL_TIMES = {1.73, 1.35, 0.71, 0.62, 14.28, 0.70, 15.52, 3.15, 1.76, 1.00, 0.00}; // Interarrival times for each customer
    private static final double[] SERVICE_TIMES = {2.90, 1.76, 3.39, 4.52, 4.46, 4.36, 2.07, 3.36, 2.37, 5.38, 0.00}; // Service times for each customer
    private static double currenTime;
    private static Queue<Integer> queue;
    private static boolean isBusy;

    private static void init() {
        currenTime = 0;
        queue = new LinkedList<>();
        isBusy = false;
    }
    private static void statisticalAccumulators(int producedParts, int partsPassedQueue, double totalWaitingTime,
                                                double maxWaitingTime, double totalTimeInSystem, double maxServiceTime,
                                                double areaUnderQueueLengthCurve, int maxQueue, double areaUnderSystemLengthCurve) {
        
        System.out.println("Simulation completed.\n");
        System.out.println("Number of parts processed: " + producedParts);
        System.out.println("Average time spent waiting in queue: " + (totalWaitingTime / producedParts));
        System.out.println("Average number of parts in queue: " );
        System.out.println("Longest time spent waiting in queue: " );
        System.out.println("Average time spent in system: " + (totalTimeInSystem / producedParts));
        System.out.println("Longest time spent in system: " );
        System.out.println("Area under queue length curve: " + areaUnderQueueLengthCurve);
        System.out.println("Area under system length curve: " + areaUnderSystemLengthCurve);
    }

    private static void attributes(double inQueue, double inService) {

    }

    private static void variables(int entityInQueue, int resource) {

    }

    private static void justFinishedEvent(int entityNo, double timeT, String eventType) {

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Get simulation time from user
        System.out.print("Enter the simulation time (in minutes): ");
        double simulationTime = sc.nextDouble();
        sc.close();

        // Start of Simulation
        System.out.println("Just Finished Event ------------- Variables ---------------------- Attributes -------------------------------- Statistical Accumulators --------------");
        System.out.printf("%-10s %-6s %-14s %-6s %-8s %-24s %-28s %-4s %-4s %-4s %-4s %-4s %-4s %-4s %-4s %-4s %-4s%n",
                "Entity No", "Time t", "Event Type", "Q(t)", "B(t)", "Arrival Time in Queue", "Arrival Time in Service", "P", "N", "ΣW", "Q", "WQ*", "ΣTS", "TS*", "∫Q", "Q*", "∫B");

        //just finished event
        int entityNo = 0;
        double timeT = 0;
        String eventType = "";

        //variables
        int entityInQueue = 0;
        int resource = 0;

        //attributes
        double inQueue = 0;
        double inService = 0;

        // statistical accumulators
        int producedParts = 0;
        int partsPassedQueue = 0;
        double totalWaitingTime = 0;
        double maxWaitingTime = 0;
        double totalTimeInService = 0;
        double maxServiceTime = 0;
        double areaUnderQueueLengthCurve = 0;
        int maxQueue = 0;
        double areaUnderSystemLengthCurve = 0;


        justFinishedEvent(entityNo, timeT, eventType);
        variables(entityInQueue, resource);
        attributes(inQueue, inService);
        statisticalAccumulators(producedParts, partsPassedQueue, totalWaitingTime, maxWaitingTime, totalTimeInService,
                maxServiceTime, areaUnderQueueLengthCurve, maxQueue, areaUnderSystemLengthCurve);

    }

    private static void printStats() {
    }
}
