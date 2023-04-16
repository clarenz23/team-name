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
    private static int currentCustomerIndex = 0;
    private static String eventType;

    private static void init() {
        currentTime = 0;
        queue = new LinkedList<>();
        isBusy = false;
    }

    private static void scheduleArrival(double timeT) {
        double arrivalTime = CUSTOMER_DATA[currentCustomerIndex][1];
        //double serviceTime = CUSTOMER_DATA[currentCustomerIndex][3];
        int customerNo = (int) CUSTOMER_DATA[currentCustomerIndex][0];
        queue.add(customerNo);

        //CUSTOMER_DATA[currentCustomerIndex][0] = arrivalTime;

        if (customerNo == 1) {
            timeT = arrivalTime;
        }

        if (currentCustomerIndex < NUM_CUSTOMERS - 1) {
            currentCustomerIndex++;
            CUSTOMER_DATA[currentCustomerIndex][2] = CUSTOMER_DATA[currentCustomerIndex][2];
            CUSTOMER_DATA[currentCustomerIndex][3] = CUSTOMER_DATA[currentCustomerIndex][3];
        } else {
            currentCustomerIndex++;
            CUSTOMER_DATA[currentCustomerIndex][2] = Math.random();
            CUSTOMER_DATA[currentCustomerIndex][3] = Math.random();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double simulationTime = 0;
        char continueSim = 'n';

        do {
            try {
                while (true) {
                    System.out.print("Enter the simulation time (minutes): ");
                    simulationTime = sc.nextDouble();
                    if (simulationTime <= 0) {
                        throw new Exception("Simulation time must be greater than 0.");
                    }
                    break;
                }
                // Rest of simulation code goes here
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

                scheduleArrival(timeT); // Schedule the first arrival

                while (currentTime < simulationTime && (isBusy || !queue.isEmpty())) {

                    double nextEventTime = 0;

                    if (isBusy && !queue.isEmpty()) {
                        nextEventTime = currentTime + CUSTOMER_DATA[queue.peek()][3];
                    } else if (isBusy) {
                        nextEventTime = Double.POSITIVE_INFINITY;
                    } else if (!queue.isEmpty()) {
                        nextEventTime = currentTime + CUSTOMER_DATA[queue.peek()][2];
                    }

                    if (nextEventTime > simulationTime) {
                        nextEventTime = simulationTime;
                    }

                    double timeUntilNextEvent = nextEventTime - currentTime;
                    areaUnderQueueLengthCurve += entityInQueue * timeUntilNextEvent;
                    areaUnderSystemLengthCurve += (entityNo + (isBusy ? 1 : 0)) * timeUntilNextEvent;
                    currentTime = nextEventTime;

                    if (currentTime >= CUSTOMER_DATA[0][1] && producedParts < NUM_CUSTOMERS) {
                        scheduleArrival(timeT);
                    }

                    int customerNo = queue.remove();
                    double arrivalTime = CUSTOMER_DATA[customerNo][1];
                    double serviceTime = CUSTOMER_DATA[customerNo][3];
                    double departureTime = currentTime + serviceTime;
                    double waitingTime = currentTime - arrivalTime;

                    // setting time t and event type
                    if (departureTime <= CUSTOMER_DATA[customerNo][1]) {
                        timeT = departureTime;
                        eventType = "Dep";
                        producedParts++;
                        totalWaitingTime += waitingTime;
                        maxWaitingTime = Math.max(maxWaitingTime, waitingTime);
                        totalTimeInSystem += serviceTime + waitingTime;
                        maxTimeInSystem = Math.max(maxTimeInSystem, serviceTime + waitingTime);

                    } else if (departureTime > CUSTOMER_DATA[customerNo][1]) {
                        timeT = CUSTOMER_DATA[customerNo][1];
                        eventType = "Arr";
                    }

                    // update number in queue, resource status
                    if (queue.isEmpty()) {
                        entityInQueue = 0;
                        resource = 0;
                    } else {
                        entityInQueue = queue.size();
                        resource = 1;
                        inQueue = currentTime;
                    }

                    //producedParts++;
                    partsPassedQueue++;
                    double timeOfLastEvent = 0;
                    areaUnderQueueLengthCurve += entityInQueue * (currentTime - timeOfLastEvent);
                    maxQueue = Math.max(maxQueue, entityInQueue);
                    areaUnderSystemLengthCurve += (entityInQueue + 1) * (currentTime - timeOfLastEvent);

                    System.out.printf("%-10d %-8.2f %-16s %-7d %-8d %-24.2f %-28.2f %-4d %-4d %-6.2f %-6.2f %-6.2f %-6.2f %-6.2f %-5d %-5.2f%n",
                            customerNo, timeT, eventType, entityInQueue, resource, inQueue, inService, producedParts, partsPassedQueue,
                            totalWaitingTime, maxWaitingTime, totalTimeInSystem, maxTimeInSystem, areaUnderQueueLengthCurve, maxQueue, areaUnderSystemLengthCurve);

                    isBusy = true;
                    CUSTOMER_DATA[customerNo][2] = CUSTOMER_DATA[customerNo][3];
                    currentTime = departureTime; // update the current time

                    entityInQueue += queue.size();
                }

                System.out.println("Simulation completed.\n");
                System.out.println("Simulation time: " + simulationTime + "\n");
                System.out.println("Number of parts processed: " + producedParts);
                System.out.println("Average time spent waiting in queue: " + (totalWaitingTime / producedParts) + " mins/part");
                System.out.println("Average number of parts in queue: " + (areaUnderQueueLengthCurve) / currentTime);
                System.out.println("Longest time spent waiting in queue: " + maxWaitingTime);
                System.out.println("Average time spent in system: " + (totalTimeInSystem / producedParts) + " mins/part");
                System.out.println("Longest time spent in system: " + maxTimeInSystem);
                System.out.println("Area under queue length curve: " + areaUnderQueueLengthCurve);
                System.out.println("Area under system length curve: " + areaUnderSystemLengthCurve);
                System.out.println();
                System.out.print("Do you want to run another simulation? (y/n): ");
                continueSim = sc.next().charAt(0);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continueSim = 'n'; // End program if there's an error
            }
        } while (continueSim == 'y');
        sc.close();
    }
}

