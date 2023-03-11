import java.util.Scanner;

public class SingleChannelQueueingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please choose the simulation termination option!");
        System.out.println("1. Based on number of customers");
        System.out.println("2. Based on number of minutes");
        System.out.print("Enter your choice (1 or 2): ");
        int option = scanner.nextInt();

        int value = 0;
        if (option == 1) {
            System.out.print("Enter the number of customers to simulate: ");
            value = scanner.nextInt();
        } else if (option == 2) {
            System.out.print("Enter the number of minutes to simulate: ");
            value = scanner.nextInt();
        } else {
            System.out.println("Invalid option selected. Terminating simulation.");
            System.exit(0);
        }

        System.out.println("\nSimulation will terminate after " + value +
                (option == 1 ? " customers have been served." : " minutes have elapsed."));

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

        // Simulation table
        while ((option == 1 && customerNumber <= value) || (option == 2 && customerSpendsInSystem < value)) {
            // Generate random values for interarrival time and service time
            interarrivalTime = (int) (Math.random() * 8) + 1;
            serviceTime = (int) (Math.random() * 5) + 1;

            // Calculate values for simulation table
            arrivalTime = arrivalTime + interarrivalTime;
            timeServiceBegins = Math.max(arrivalTime, timeServiceEnds);
            waitingTime = timeServiceBegins - arrivalTime;
            timeServiceEnds = timeServiceBegins + serviceTime;
            customerSpendsInSystem = customerSpendsInSystem + waitingTime + serviceTime;
            idleTimeOfServer = idleTimeOfServer + (timeServiceBegins - arrivalTime);

            // Print simulation table row
            System.out.printf("%-12d | %-26d | %-19d | %-19d | %-19d | %-12d | %-17d | %-25d | %-29d\n",
                    customerNumber, interarrivalTime, arrivalTime, serviceTime, timeServiceBegins,
                    waitingTime, timeServiceEnds, customerSpendsInSystem, idleTimeOfServer);

            // Increment customer number
            customerNumber++;
        }
    }
}
