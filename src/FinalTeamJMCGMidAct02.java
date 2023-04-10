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

            int queueLength = numInQueue;
            totalSystemTime += queueLength + (nextDepartureTime - clock);
            longestSystemTime = Math.max(longestSystemTime, queueLength + (nextEventTime - clock));
            queueLengthArea += queueLength * (nextEventTime - clock);
            highestQueueLength = Math.max(highestQueueLength, queueLength);
            serverBusyArea += (nextEventTime - clock) * (11 - numInQueue);

            // Update clock
            clock = nextEventTime;

            if (event == 1 && queueLength == 0) {
                totalQueueWaitTime += 0;
            } else if (event == 1) {
                totalQueueWaitTime += (clock - nextArrivalTime[getNextArrivalTime(nextArrivalTime)]);
            } else if (event == 2) {
                partsProduced++;
            }

            if (event == 1) { // Arrival event
                // Update statistical accumulators
                partsProduced++;
                if (numInQueue > 0) {
                    totalQueueWaitTime += (clock - nextArrivalTime[0]);
                    partsPassedQueue++;
                    longestQueueTime = Math.max(longestQueueTime, (clock - nextArrivalTime[0]));
                    // Shift all remaining arrival times forward
                    for (int i = 1; i <= numInQueue; i++) {
                        nextArrivalTime[i - 1] = nextArrivalTime[i];
                    }
                    numInQueue--;
                }
                // Schedule next arrival
                int interarrivalTime = interarrivalTimes[partsProduced % 11];
                nextArrivalTime[numInQueue] = clock + interarrivalTime;
                numInQueue++;

            } else { // Departure event
                // Update statistical accumulators
                if (numInQueue > 0) {
                    totalQueueWaitTime += (clock - nextArrivalTime[0]);
                    partsPassedQueue++;
                    longestQueueTime = Math.max(longestQueueTime, (clock - nextArrivalTime[0]));
                    // Shift all remaining arrival times forward
                    for (int i = 1; i <= numInQueue; i++) {
                        nextArrivalTime[i - 1] = nextArrivalTime[i];
                    }
                    numInQueue--;
                    // Schedule next departure
                    int serviceTime = serviceTimes[partsProduced % 11];
                    nextDepartureTime = clock + serviceTime;
                    serverBusyArea += serviceTime;
                } else {
                    // No parts waiting in queue, server becomes idle
                    nextDepartureTime = Integer.MAX_VALUE;
                }
            }
        }

        // Compute and print simulation results
        double avgQueueWaitTime = (double) totalQueueWaitTime / partsPassedQueue;
        double avgSystemTime = (double) totalSystemTime / partsProduced;
        double avgQueueLength = (double) queueLengthArea / simulationTime;
        double avgServerUtilization = (double) serverBusyArea / simulationTime;
        System.out.println("Simulation Results:");
        System.out.printf("Total Parts Produced: %d\n", partsProduced);
        System.out.printf("Average Queue Wait Time: %.2f minutes\n", avgQueueWaitTime);
        System.out.printf("Longest Queue Time: %d minutes\n", longestQueueTime);
        System.out.printf("Average System Time: %.2f minutes\n", avgSystemTime);
        System.out.printf("Longest System Time: %d minutes\n", longestSystemTime);
        System.out.printf("Average Queue Length: %.2f\n", avgQueueLength);
        System.out.printf("Highest Queue Length: %d\n", highestQueueLength);
        System.out.printf("Average Server Utilization: %.2f%%\n", avgServerUtilization * 100);
    }

    private static int getNextArrivalTime(int[] nextArrivalTime) {
        int minTime = Integer.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < 11; i++) {
            if (nextArrivalTime[i] < minTime) {
                minTime = nextArrivalTime[i];
                minIndex = i;
            }
        }
        return minIndex;
    }
}


