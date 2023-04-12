import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class RedoMidAct02 {

    private static void statisticalAccumulators() {
    }

    private static void attributes() {
    }

    private static void variables() {
    }

    private static void justFinishedEvent() {
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {

            // Get simulation time from user
            System.out.print("Enter the simulation time (in minutes): ");
            try {
                double simulationTime = sc.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // consume the invalid input
            }
        }

        // Start of Simulation
        System.out.printf("%4s  %7s  %7s  %9s  %9s  %9s  %9s%n",
                "Part", "Arrival", "Interarr", "Wait Time", "Departure", "Service", "System");

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
                    e.printStackTrace();
                    return;
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
