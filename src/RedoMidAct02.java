import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class RedoMidAct02 {

    private static double simulationTime;
    private static double currentTime;
    private static int numPartsProduced;
    private static int numPartsInQueue = 1;  // initialize to 1 instead of 0
    private static int numPartsProcessed;
    private static int maxQueueLength;
    private static int maxTimeInQueue;
    private static int maxTimeInSystem;
    private static double totalWaitingTime;
    private static double totalSystemTime;
    private static double areaUnderQLength;
    private static int highestQLength;
    private static double areaUnderServerBusy;
    private static int[] arrivalTimes = new int[11];
    private static int[] interarrivalTimes = new int[11];
    private static int[] serviceTimes = new int[11];

    private static void statisticalAccumulators() {
        System.out.printf("%-4d %-8.2f %-4d %-4d %-8.2f %-4d %-8.2f %-4d %-8.2f%n", numPartsProduced, totalWaitingTime, numPartsProcessed,
                maxTimeInQueue, totalSystemTime, maxTimeInSystem, areaUnderQLength, highestQLength, areaUnderServerBusy);
    }

    private static void attributes() {
        System.out.printf("%-24s %-28s %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d%n",
                "", "", numPartsInQueue, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, areaUnderQLength, numPartsInQueue, areaUnderServerBusy);
    }

    private static void variables() {
        System.out.printf("%-10s %-6.2f %-14s %-6d %-8d %-24s %-28s %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d%n",
                "", currentTime, "", numPartsInQueue, 0, "", "", 0, 0, 0, 0, 0, 0, 0, areaUnderQLength, numPartsInQueue, areaUnderServerBusy);
    }

    private static void justFinishedEvent() {
        // Increment time
        currentTime += interarrivalTimes[numPartsProduced];

        // Update statistical accumulators
        numPartsProduced++;
        totalWaitingTime += Math.max(0, currentTime - arrivalTimes[numPartsProduced - 1]);
        numPartsInQueue--;
        numPartsProcessed++;
        totalSystemTime += serviceTimes[numPartsProduced];
        maxTimeInQueue = Math.max(maxTimeInQueue, (int) (currentTime - arrivalTimes[numPartsProduced - 1]));
        maxTimeInSystem = Math.max(maxTimeInSystem, (int) (currentTime - arrivalTimes[numPartsProduced - 1]) + serviceTimes[numPartsProduced - 1]);
        areaUnderQLength += numPartsInQueue;
        highestQLength = Math.max(highestQLength, numPartsInQueue);
        areaUnderServerBusy += (numPartsInQueue > 0 ? 1 : 0) * (interarrivalTimes[numPartsProduced - 1] + serviceTimes[numPartsProduced - 1]);

        // Print current state of the system
        statisticalAccumulators();
        attributes();
        variables();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            // Get simulation time from user
            System.out.print("Enter the simulation time (in minutes): ");
            try {
                simulationTime = sc.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // consume the invalid input
            }
        }

        // Start of Simulation
        System.out.println("Just Finished Event ------------- Variables ---------------------- Attributes -------------------------------- Statistical Accumulators --------------");
        System.out.printf("%-10s %-6s %-14s %-6s %-8s %-24s %-28s %-4s %-4s %-4s %-4s %-4s %-4s %-4s %-4s %-4s %-4s%n",
                "Entity No", "Time t", "Event Type", "Q(t)", "B(t)", "Arrival Time in Queue", "Arrival Time in Service", "P", "N", "ΣW", "Q", "WQ*", "ΣTS", "TS*", "∫Q", "Q*", "∫B");


        justFinishedEvent();
        variables();
        attributes();
        statisticalAccumulators();

        String filePath = "res/data.csv";
        int[] arrivalTimes = new int[11];
        int[] interarrivalTimes = new int[11];
        int[] serviceTimes = new int[11];

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header row
            String header = br.readLine();
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                try {
                    arrivalTimes[index] = (int) Double.parseDouble(values[1]);
                    interarrivalTimes[index] = (int) Double.parseDouble(values[2]);
                    serviceTimes[index] = (int) Double.parseDouble(values[3]);
                } catch (NumberFormatException e) {
                    System.err.printf("Error reading line %d of CSV file: invalid number format. Using default values.%n", index + 1);
                    arrivalTimes[index] = 0;
                    interarrivalTimes[index] = 0;
                    serviceTimes[index] = 0;
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
