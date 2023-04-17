package TeamJMCG;

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
        char continueSim;

        // Use a do-while loop to repeat the simulation as long as the user wants to continue running it
        do {
            try {
                // Use a while loop to get valid input for the simulation time
                while (true) {
                    System.out.print("Enter the simulation time (minutes): ");
                    simulationTime = sc.nextDouble();
                    if (simulationTime <= 0) {
                        throw new Exception("Simulation time must be greater than 0.");
                    }
                    break;
                }
                new Simulation(simulationTime);
                // Ask the user if they want to run another simulation
                System.out.print("Do you want to run another simulation? (y/n): ");
                continueSim = sc.next().charAt(0);
            } catch (Exception e) {
                // If there's an error (e.g. user inputs an invalid simulation time), print an error message and end the program
                System.out.println(e.getMessage());
                continueSim = 'n'; // End program if there's an error
            }
        } while (continueSim == 'y');
        // Close the Scanner object to avoid resource leak
        sc.close();
    }
}


