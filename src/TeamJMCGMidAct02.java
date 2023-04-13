import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class TeamJMCGMidAct02 {

    // Define a two-dimensional array of customer data
    // Each row represents a customer and each column represent a data point
    // Column 0: Customer ID, Column 1: Arrival Time, Column 2: Interarrival Time, Column 3: Service Time
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
    private static double time;
    private static Queue<Integer> queue;
    private static boolean isBusy;

    private static void init() {
        time = 0;
        queue = new LinkedList<>();
        isBusy = false;
    }

    private static void scheduleArrival() {
        double interarrivalTime = CUSTOMER_DATA[0][2];
        double arrivalTime = time + interarrivalTime;
        int customerNo = (int) CUSTOMER_DATA[0][0];
        queue.add(customerNo);
        System.out.printf("%4d  %7.2f  %7.2f  %9s  %9.2f  %9s  %9s%n",
                customerNo, time, interarrivalTime, "-", arrivalTime, "-", "-");
        CUSTOMER_DATA[0][1] = arrivalTime;
        CUSTOMER_DATA[0][2] = CUSTOMER_DATA[customerNo][2];
        CUSTOMER_DATA[0][3] = CUSTOMER_DATA[1][3];
    }

    private static void processPart() {
        if (!queue.isEmpty()) {
            int customerNo = queue.remove();
            double serviceTime = CUSTOMER_DATA[customerNo][3];
            double departureTime = time + serviceTime;
            double waitingTime = time - CUSTOMER_DATA[customerNo][1];
            System.out.printf("%4d  %7.2f  %7s  %9.2f  %9s  %9.2f  %9.2f%n",
                    customerNo, time, "-", CUSTOMER_DATA[customerNo][1], waitingTime, departureTime, serviceTime);
            isBusy = true;
            CUSTOMER_DATA[customerNo][2] = CUSTOMER_DATA[customerNo][3];
        }
        else {
            isBusy = false;
        }
    }



    // Print the statistical accumulators
    private static void printStats(int numParts, double totalWaitTime, int numPartsInQueue,
                                   double longestQueueTime, double totalTimeInSystem, double longestSystemTime,
                                   double areaUnderQueueLengthCurve, double areaUnderSystemLengthCurve) {
        System.out.println("Simulation completed.\n");
        System.out.println("Number of parts processed: " + numParts);
        System.out.println("Average time spent waiting in queue: " + (totalWaitTime / numParts));
        System.out.println("Average number of parts in queue: " + (areaUnderQueueLengthCurve / time));
        System.out.println("Longest time spent waiting in queue: " + longestQueueTime);
        System.out.println("Average time spent in system: " + (totalTimeInSystem / numParts));
        System.out.println("Longest time spent in system: " + longestSystemTime);
        System.out.println("Area under queue length curve: " + areaUnderQueueLengthCurve);
        System.out.println("Area under system length curve: " + areaUnderSystemLengthCurve);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the simulation time (minutes): ");
        double simTime = sc.nextDouble();


        init(); // Initialize the simulation
        System.out.printf("%4s  %7s  %7s  %9s  %9s  %9s  %9s%n",
                "Part", "Arrival", "Interarr", "Wait Time", "Departure", "Service", "System");

        scheduleArrival(); // Schedule the first arrival

        // Run the simulation until all parts have been processed
        int numParts = 0;
        double totalWaitTime = 0;
        int numPartsInQueue = 0;
        double longestQueueTime = 0;
        double totalTimeInSystem = 0;
        double longestSystemTime = 0;
        double areaUnderQueueLengthCurve = 0;
        double areaUnderSystemLengthCurve = 0;


        while (time < simTime && numParts < NUM_CUSTOMERS) {
            double nextEventTime;

            if (isBusy && !queue.isEmpty()) {
                nextEventTime = time + CUSTOMER_DATA[queue.peek()][3];
            } else if (isBusy) {
                nextEventTime = Double.POSITIVE_INFINITY;
            } else if (!queue.isEmpty()) {
                nextEventTime = time + CUSTOMER_DATA[queue.peek()][2];
            } else {
                break;
            }

            if (numParts == NUM_CUSTOMERS) {
                break;
            }

            if (nextEventTime > simTime) {
                nextEventTime = simTime;
            }

            double timeUntilNextEvent = nextEventTime - time;
            areaUnderQueueLengthCurve += numPartsInQueue * timeUntilNextEvent;
            areaUnderSystemLengthCurve += (numPartsInQueue + (isBusy ? 1 : 0)) * timeUntilNextEvent;
            time = nextEventTime;

            if (time >= CUSTOMER_DATA[0][1] && numParts < NUM_CUSTOMERS) {
                scheduleArrival();
            }

            if (time >= CUSTOMER_DATA[queue.peek()][1] && !isBusy) {
                processPart();
                numParts++;
                double waitingTime = time - CUSTOMER_DATA[queue.peek()][1];
                totalWaitTime += waitingTime;
                numPartsInQueue--;
                longestQueueTime = Math.max(longestQueueTime, waitingTime);
                double timeInSystem = time - CUSTOMER_DATA[queue.peek()][1] + CUSTOMER_DATA[queue.peek()][3];
                totalTimeInSystem += timeInSystem;
                longestSystemTime = Math.max(longestSystemTime, timeInSystem);
                isBusy = false;
            }

            numPartsInQueue += queue.size();
            sc.close();
        }

        printStats(numParts, totalWaitTime, numPartsInQueue, longestQueueTime, totalTimeInSystem,
                longestSystemTime, areaUnderQueueLengthCurve, areaUnderSystemLengthCurve);
    }
}