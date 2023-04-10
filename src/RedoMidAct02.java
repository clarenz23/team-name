import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class RedoMidAct02 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

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
            return;
        }

        // Get simulation time from user
        double simulationTime;
        while (true) {
            try {
                System.out.print("Enter the simulation time (in minutes): ");
                simulationTime = sc.nextDouble();
                break;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // consume the invalid input
            }
        }

        justFinishedEvent();
        variables();
        attributes();
        statisticalAccumulators();

    }

    private static void statisticalAccumulators() {
    }

    private static void attributes() {
    }

    private static void variables() {
    }

    private static void justFinishedEvent() {
    }
}
