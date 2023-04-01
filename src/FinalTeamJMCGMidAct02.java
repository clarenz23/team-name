import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class FinalTeamJMCGMidAct02 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String filePath = "res/data.csv";
        int[] arrivalTimes = new int[11];
        int[] interarrivalTimes = new int[11];
        int[] serviceTimes = new int[11];

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                arrivalTimes[index] = Integer.parseInt(values[1]);
                interarrivalTimes[index] = Integer.parseInt(values[2]);
                serviceTimes[index] = Integer.parseInt(values[3]);
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Get simulation time from user
        System.out.print("Enter the simulation time (in minutes): ");
        double simulationTime = sc.nextDouble();

        // Initialize statistical accumulators
        int partsProduced = 0;
        int totalQueueWaitTime = 0;
        int partsPassedQueue = 0;
        int longestQueueTime = 0;
        int totalSystemTime = 0;
        int longestSystemTime = 0;
        int queueLengthArea = 0;
        int highestQueueLength = 0;
        int serverBusyArea = 0;

        // Initialize simulation variables
        int clock = 0;
        int[] nextArrivalTime = new int[11];
        for (int i = 0; i < 11; i++) {
            nextArrivalTime[i] = arrivalTimes[i];
        }
        int nextDepartureTime = Integer.MAX_VALUE;
        int numInQueue = 0;

        // Run simulation loop
        while (clock < simulationTime) {
            // Determine next event (arrival or departure)
            int nextEventTime = Math.min(nextDepartureTime, getNextArrivalTime(nextArrivalTime));
            int event = (nextEventTime == nextDepartureTime) ? 2 : 1;

            // @Enrico Castro
            // Update statistical accumulators
            int queueLength = numInQueue;
            totalSystemTime += queueLength + (nextDepartureTime - clock);
            longestSystemTime = Math.max(longestSystemTime, queueLength + (nextDepartureTime - clock));
            queueLengthArea += queueLength * (nextEventTime - clock);
            highestQueueLength = Math.max(highestQueueLength, queueLength);
            serverBusyArea += (event == 2) ? (nextEventTime - clock) : 0;
            if (event == 1 && queueLength == 0) {
                totalQueueWaitTime += 0;
            } else if (event == 1) {
                totalQueueWaitTime += (nextDepartureTime - clock) - serviceTimes[11 - numInQueue];
            } else if (event == 2) {
                partsProduced++;
            }

            // @Joanne Mangsat
            // Perform event
            if (event == 1) { // arrival event
                // TODO: handle arrival event
            } else { // departure event
                // TODO: handle departure event
            }
        }

        // @Danielle Javier
        // Print simulation results
        // TODO: print simulation results
    }

    private static int getNextArrivalTime(int[] nextArrivalTime) {
        int minTime = Integer.MAX_VALUE;
        for (int i = 0; i < 11; i++) {
            if (nextArrivalTime[i] < minTime) {
                minTime = nextArrivalTime[i];
            }
        }
        return minTime;
    }
}
