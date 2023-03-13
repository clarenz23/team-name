import java.util.Scanner;

public class SingleChannelQueueingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean endProgram = false;
        while (!endProgram) {
            System.out.println("""
                        Please choose the simulation termination option!
                        \t1. Based on number of customers
                        \t2. Based on number of minutes
                        \t3. End program""");
            System.out.print("Enter your choice (1, 2, or 3): ");
            int option = scanner.nextInt();

            int value = 0;
            if (option == 1) {
                System.out.print("Enter the number of customers to simulate: ");
                value = scanner.nextInt();
            } else if (option == 2) {
                System.out.print("Enter the number of minutes to simulate: ");
                value = scanner.nextInt();
            } else if (option == 3) {
                endProgram = true;
            } else {
                System.out.println("Invalid option selected.");
                continue;
            }

            if (endProgram) {
                break;
            }

        System.out.println("\nSimulation table:\n");
        System.out.println("CUSTOMER NO. | INTERARRIVAL TIME (MINS) | ARRIVAL TIME (MINS) | SERVICE TIME (MINS) | TIME SERVICE BEGINS | WAITING TIME | TIME SERVICE ENDS | CUSTOMER SPENDS IN SYSTEM | IDLE TIME OF SERVER (MINS)");

        // Simulation variables
        int customerNumber = 1;
        int interarrivalTime = 0;
        int arrivalTime = 0;
        int serviceTime = 0;
        int timeServiceBegins = 0;
        int waitingTime = 0;
        int timeServiceEnds = 0;
        int customerSpendsInSystem = 0;
        int idleTimeOfServer = 0;
        int totalWaitingTime = 0;
        int totalQueueTime = 0;
        int totalServiceTime = 0;
        int totalCustomerSpends = 0;
        int totalInterarrivalTime = 0;
        int numCustomersInQueue = 0;
        int numOfWaitingCustomers = 0;
        int totalIdleTimeOfServer = 0;

        // Simulation table
        while ((option == 1 && customerNumber <= value) || (option == 2 && customerSpendsInSystem < value)) {
            // Generate interarrival time based on distribution
            if (customerNumber == 1){
                interarrivalTime = 0;
                arrivalTime = 0;
            } else {
                double rand1 = Math.random();
                if (rand1 < 0.125) {
                    interarrivalTime = 1;
                } else if (rand1 < 0.250) {
                    interarrivalTime = 2;
                } else if (rand1 < 0.375) {
                    interarrivalTime = 3;
                } else if (rand1 < 0.500) {
                    interarrivalTime = 4;
                } else if (rand1 < 0.625) {
                    interarrivalTime = 5;
                } else if (rand1 < 0.750) {
                    interarrivalTime = 6;
                } else if (rand1 < 0.875) {
                    interarrivalTime = 7;
                } else {
                    interarrivalTime = 8;
                }
            }

            //Generate service time based on distribution
            double rand2 = Math.random();
            if (rand2 < 0.15) {
                serviceTime = 1;
            } else if (rand2 < 0.45) {
                serviceTime = 2;
            } else if (rand2 < 0.70) {
                serviceTime = 3;
            } else if (rand2 < 0.90) {
                serviceTime = 4;
            } else {
                serviceTime = 5;
            }

            totalServiceTime += serviceTime;

            // Calculate values for simulation table
            arrivalTime = arrivalTime + interarrivalTime;
            timeServiceBegins = Math.max(arrivalTime, timeServiceEnds);
            waitingTime = timeServiceBegins - arrivalTime;
            timeServiceEnds = timeServiceBegins + serviceTime;
            customerSpendsInSystem = timeServiceEnds - arrivalTime;
            idleTimeOfServer = idleTimeOfServer + (timeServiceBegins - arrivalTime);

            totalWaitingTime += waitingTime;
            if (waitingTime > 0) {
                numOfWaitingCustomers++;
            }

            totalIdleTimeOfServer += idleTimeOfServer;
            totalInterarrivalTime += interarrivalTime;
            totalCustomerSpends += customerSpendsInSystem;

            // Print simulation table row
            System.out.printf("%-12d | %-26d | %-19d | %-19d | %-19d | %-12d | %-17d | %-25d | %-29d\n",
                    customerNumber, interarrivalTime, arrivalTime, serviceTime, timeServiceBegins,
                    waitingTime, timeServiceEnds, customerSpendsInSystem, idleTimeOfServer);

            // Increment customer number
            customerNumber++;
            numCustomersInQueue--;
            }

            // Calculate performance metrics
            double avgWaitingTime = (double) totalWaitingTime / (customerNumber);
            String formatAvgWaitingTime = String.format("%.2f", avgWaitingTime);
            double probCustomerWaits = (double) numOfWaitingCustomers / customerNumber;
            String formatProbCustomerWaits = String.format("%.2f", probCustomerWaits);
            double propIdleTime = (double) totalIdleTimeOfServer / timeServiceEnds;
            String formatPropIdleTime = String.format("%.2f", propIdleTime);
            double avgServiceTime = (double) totalServiceTime / customerNumber;
            String formatAvgServiceTime = String.format("%.2f", avgServiceTime);
            double avgInterarrivalTime = (double) totalInterarrivalTime / (customerNumber - 1);
            String formatAvgInterarrivalTime = String.format("%.2f", avgInterarrivalTime);
            double avgQueueWaitTime = (double) totalWaitingTime / numOfWaitingCustomers;
            String formatAvgQueueWaitTime = String.format("%.2f", avgQueueWaitTime);
            double avgCustomerSpends = (double) totalCustomerSpends/ customerNumber;
            String formatAvgCustomerSpends = String.format("%.2f", avgCustomerSpends);

            // Display performance metrics
            System.out.println("\nPerformance Metrics:");
            System.out.println("Average waiting time for a customer: " + formatAvgWaitingTime + " minutes");
            System.out.println("Probability that a customer has to wait in the queue: " + formatProbCustomerWaits);
            System.out.println("Proportion of idle time of the server: " + formatPropIdleTime);
            System.out.println("Average service time: " + formatAvgServiceTime + " minutes");
            System.out.println("Average time between arrivals: " + formatAvgInterarrivalTime + " minutes");
            System.out.println("Average waiting time for those who wait in queue: " + formatAvgQueueWaitTime + " minutes");
            System.out.println("Average time a customer spends in the system: " + formatAvgCustomerSpends + " minutes");
            System.out.printf("\nSimulation was terminated after %d %s.\n",
                    value, (option == 1) ? "customers have been served" : "minutes have elapsed");
            System.out.println();

        }

    }
}
