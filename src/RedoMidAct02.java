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
    private static double[] serviceTimes = new double[11];


    private static void statisticalAccumulators() {
        System.out.printf("%-4d %-4f %-4d %-4d %-4f %-4d %-4f %-4d %-4f%n", numPartsProduced, totalWaitingTime, numPartsProcessed,
                maxTimeInQueue, totalSystemTime, maxTimeInSystem, areaUnderQLength, highestQLength, areaUnderServerBusy);
    }

    private static void attributes() {
        System.out.printf("%-24s %-29s %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4d %-4f %-4d %-4f%n",
                "", "", numPartsInQueue, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, areaUnderQLength, numPartsInQueue, areaUnderServerBusy);
    }

    private static void variables() {
        System.out.printf("%-10s %-6.1f %-14s %-6d %-8d %-24s %-28s %-4d %-4d %-4d %-4d %-4d %-4d %-4f %-4d %-4f%n",
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
        totalSystemTime += serviceTimes[numPartsProduced - 1];
        maxTimeInQueue = Math.max(maxTimeInQueue, (int) (currentTime - arrivalTimes[numPartsProduced - 1]));
        maxTimeInSystem = Math.max(maxTimeInSystem, (int) (currentTime - arrivalTimes[numPartsProduced - 1]) + (int)serviceTimes[numPartsProduced - 1]);
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
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header row
            String header = br.readLine();

            // Read data
            for (int i = 0; i < 11; i++) {
                String line = br.readLine();
                if (line == null) {
                    throw new RuntimeException("Not enough data in input file");
                }
                String[] values = line.split(",");
                if (values.length != 4) {
                    throw new RuntimeException("Invalid data format in input file");
                }
                try {
                    arrivalTimes[i] = (int) Double.parseDouble(values[1]);
                    interarrivalTimes[i] = (int) Double.parseDouble(values[2]);
                    serviceTimes[i] = (int) Double.parseDouble(values[3]);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid data format in input file");
                }
            }

            // Make sure there's no extra data in the input file
            if (br.readLine() != null) {
                throw new RuntimeException("Too much data in input file");
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            System.exit(1);
        } catch (RuntimeException e) {
            System.err.println("Error parsing input file: " + e.getMessage());
            System.exit(1);
        }
    }
}
