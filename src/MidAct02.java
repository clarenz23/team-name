import java.util.Scanner;

public class MidAct02 {
    private static final int NUM_CUSTOMERS = 11; // Number of customers to simulate
    private static final double[] ARRIVAL_TIMES = {0.2, 0.4, 0.6, 0.8, 1.0}; // Arrival times for each customer
    private static final double[] SERVICE_TIMES = {0.3, 0.5, 0.2, 0.4, 0.1}; // Service times for each customer

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter simulation time: ");
        double simulationTime = scanner.nextDouble();
        scanner.close();

        double currentTime = 0; // Current simulation time
        double totalWaitingTime = 0; // Total waiting time
        double totalQueueLength = 0; // Total queue length
        int numCustomersServed = 0; // Number of customers served

        for (int i = 0; i < NUM_CUSTOMERS; i++) {
            // Update arrival times
            if (i == 0) {
                currentTime += ARRIVAL_TIMES[i];
            }

            // Process the next customer
            if (i == 0 || SERVICE_TIMES[i - 1] <= currentTime - ARRIVAL_TIMES[i]) {
                currentTime += SERVICE_TIMES[i];
                totalWaitingTime += 0; // No waiting time for the first customer
                numCustomersServed++;
            } else {
                double departureTime = currentTime + SERVICE_TIMES[i];
                totalWaitingTime += Math.max(0, ARRIVAL_TIMES[i] - currentTime);
                currentTime = departureTime;
                numCustomersServed++;
            }

            totalQueueLength += i; // Update queue length

            // Check if simulation time is reached
            if (currentTime >= simulationTime) {
                break;
            }
        }

        // Calculate and display statistics
        double averageWaitingTime = totalWaitingTime / NUM_CUSTOMERS;
        double averageQueueLength = totalQueueLength / NUM_CUSTOMERS;
        System.out.println("Simulation results:");
        System.out.println("Number of customers served: " + NUM_CUSTOMERS);
        System.out.println("Average waiting time: " + averageWaitingTime);
        System.out.println("Average queue length: " + averageQueueLength);
    }
}
