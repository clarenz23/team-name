import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class RedoMidAct02 {
    // Column 0: Customer ID, Column 1: Arrival Time, Column 2: Interarrival, Column 3: Service Time
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
    private static double currentTime;
    private static Queue<Integer> queue;
    private static boolean isBusy;

    private static void init() {
        currentTime = 0;
        queue = new LinkedList<>();
        isBusy = false;
    }

    private static void scheduleArrival() {
        double interarrivalTime = CUSTOMER_DATA[0][2];
        double arrivalTime = currentTime + interarrivalTime;
        int customerNo = (int) CUSTOMER_DATA[0][0];
        queue.add(customerNo);

        CUSTOMER_DATA[0][1] = arrivalTime;
        CUSTOMER_DATA[0][2] = CUSTOMER_DATA[customerNo][2];
        CUSTOMER_DATA[0][3] = CUSTOMER_DATA[1][3];
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Get simulation time from user
        System.out.print("Enter the simulation time (in minutes): ");
        double simulationTime = sc.nextDouble();
        sc.close();

        // Start of Simulation
        System.out.println("-----Just Finished Event ------------- Variables ---------------------- Attributes ------------------------------------- Statistical Accumulators ------------------");
        System.out.printf("%-10s %-8s %-16s %-7s %-8s %-24s %-28s %-4s %-4s %-6s %-6s %-6s %-6s %-6s %-5s %-5s%n",
                "Entity No", "Time t", "Event Type", "Q(t)", "B(t)", "Arrival Time in Queue", "Arrival Time in Service", "P", "N", "ΣWQ", "WQ*", "ΣTS", "TS*", "∫Q", "Q*", "∫B");

        //just finished event
        int entityNo = 0;
        double timeT = 0;
        String eventType = "Init";

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

        System.out.printf("%-10d %-8.2f %-16s %-7d %-8d %-24.2f %-28.2f %-4d %-4d %-6.2f %-6.2f %-6.2f %-6.2f %-6.2f %-5d %-5.2f%n",
                entityNo, timeT, eventType, entityInQueue, resource, inQueue, inService, producedParts, partsPassedQueue,
                totalWaitingTime, maxWaitingTime, totalTimeInSystem, maxTimeInSystem, areaUnderQueueLengthCurve, maxQueue, areaUnderSystemLengthCurve);

        scheduleArrival(); // Schedule the first arrival

        System.out.println("Simulation completed.\n");
        System.out.println("Simulation time: " + simulationTime + "\n");
        System.out.println("Number of parts processed: " + producedParts);
        System.out.println("Average time spent waiting in queue: " + (totalWaitingTime / producedParts));
        System.out.println("Average number of parts in queue: "); // formula tba
        System.out.println("Longest time spent waiting in queue: " + maxWaitingTime);
        System.out.println("Average time spent in system: " + (totalTimeInSystem / producedParts));
        System.out.println("Longest time spent in system: " + maxTimeInSystem);
        System.out.println("Area under queue length curve: " + areaUnderQueueLengthCurve);
        System.out.println("Area under system length curve: " + areaUnderSystemLengthCurve);
    }
}
