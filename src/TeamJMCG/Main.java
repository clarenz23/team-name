package TeamJMCG;

import java.util.InputMismatchException;
import java.util.Scanner;

/***
 * Members:
 * Castro, Enrico Nathanielle
 * Garrido, Clarence Angelo Lupin
 * Javier, Danielle Nicole
 * Mangsat, Joanne
 */

/**
 This program is a simulation that accepts a user-inputted simulation time in minutes and can be run repeatedly
 The program uses a Scanner object to get input from the user
 It also uses a do-while loop to repeat the simulation as long as the user wants to continue running it
*/

// This is the main class of the program
public class Main {
    public static void main(String[] args) {

        // Create a Scanner object to get input from the user
        Scanner sc = new Scanner(System.in);

        // Initialize variables for the simulation time and whether the user wants to continue running the simulation
        double simulationTime = 0;
        char continueSim = 'y';

        // Use a do-while loop to repeat the simulation as long as the user wants to continue running it
        do {
            try {
                boolean validInput = false;
                while (!validInput) {
                    System.out.print("Enter the simulation time (minutes): ");
                    if (sc.hasNextDouble()) {
                        simulationTime = sc.nextDouble();
                        if (simulationTime <= 0) {
                            System.out.println("Error: Simulation time must be greater than 0.");
                        } else {
                            validInput = true;
                        }
                    } else {
                        System.out.println("Error: Invalid input. Please enter a valid number.");
                        sc.next(); // Discard non-numeric input
                    }
                }

                // Create an instance of the Simulation class and run it
                new SimulationProcess(simulationTime);

                // Ask the user if they want to run another simulation
                System.out.print("Do you want to run another simulation? (y/n): ");
                continueSim = sc.next().charAt(0);
            } catch (Exception e) {
                // If there's an error, print an error message and repeat the loop
                System.out.println("Error: " + e.getMessage());
                continueSim = 'y'; // Repeat loop if there's an error
                sc.next(); // Discard input to prevent infinite loop
            }
        } while (continueSim == 'y');

        // Close the Scanner object to avoid resource leak
        sc.close();
    }
}
