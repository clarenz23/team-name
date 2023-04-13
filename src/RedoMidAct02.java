import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class RedoMidAct02 {
    // Column 0: Customer ID, Column 1: Arrival Time, Column 2: Service Time
    private static final double[][] CUSTOMER_DATA = {
            {1, 0.00, 1.73, 2.90},
            {2, 1.73, 1.35, 1.76},
            {3, 3.08, 0.71, 3.39},
            {4, 3.79, 0.62, 4.52},
            {5, 4.41, 14.28, 4.46},
            {6, 18.69, 0.70, 4.36},
            {7, 19.39, 15.52, 2.07},
            {8, 34.91, 3.15, 3.36},
            {9, 38.06, 1.76, 2.37},
            {10, 39.82, 1.00, 5.38},
            {11, 40.82, Math.random(), Math.random()}
    };
    private static final int NUM_CUSTOMERS = CUSTOMER_DATA.length;
    private static double currenTime;
    private static Queue<Integer> queue;
    private static boolean isBusy;

    private static void init() {
        currenTime = 0;
        queue = new LinkedList<>();
        isBusy = false;
    }
    private static void statisticalAccumulators(int producedParts, int partsPassedQueue, double totalWaitingTime,
                                                double maxWaitingTime, double totalTimeInSystem, double maxTimeInSystem,
                                                double areaUnderQueueLengthCurve, int maxQueue, double areaUnderSystemLengthCurve) {

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
        double totalTimeInSystem = 0;
        double maxTimeInSystem = 0;
        double areaUnderQueueLengthCurve = 0;
        int maxQueue = 0;
        double areaUnderSystemLengthCurve = 0;

        init();

        /*
        justFinishedEvent(entityNo, timeT, eventType);
        variables(entityInQueue, resource);
        attributes(inQueue, inService);
        statisticalAccumulators(producedParts, partsPassedQueue, totalWaitingTime, maxWaitingTime, totalTimeInSystem,
                maxTimeInSystem, areaUnderQueueLengthCurve, maxQueue, areaUnderSystemLengthCurve);
         */


        System.out.printf("%-10d %-6s %-14d %-6d %-8d %-24d %-28d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d%n",
                entityNo, timeT, eventType, entityInQueue, resource, inQueue, inService, producedParts, partsPassedQueue,
                totalWaitingTime, maxWaitingTime, totalTimeInSystem, maxTimeInSystem, areaUnderQueueLengthCurve, maxQueue, areaUnderQueueLengthCurve);


        System.out.println("Simulation completed.\n");
        System.out.println("Simulation time: " + simulationTime + "\n");
        System.out.println("Number of parts processed: " + producedParts);
        System.out.println("Average time spent waiting in queue: " + (totalWaitingTime / producedParts));
        System.out.println("Average number of parts in queue: " );
        System.out.println("Longest time spent waiting in queue: " );
        System.out.println("Average time spent in system: " + (totalTimeInSystem / producedParts));
        System.out.println("Longest time spent in system: " );
        System.out.println("Area under queue length curve: " + areaUnderQueueLengthCurve);
        System.out.println("Area under system length curve: " + areaUnderSystemLengthCurve);
    }
}
