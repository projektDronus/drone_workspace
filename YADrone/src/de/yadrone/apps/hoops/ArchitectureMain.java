package de.yadrone.apps.hoops;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;

import java.util.Scanner;

public class ArchitectureMain {
    public static void main(String[] args) {

        System.out.println("Starter...");

        IARDrone drone = null;
        TestBot bot = new TestBot();
        Scanner scanner = new Scanner(System.in);

        try {
            // Forbindelse blev oprettet
            drone = new ARDrone();

            drone.start();
            drone.addExceptionListener(new IExceptionListener() {
                public void exeptionOccurred(ARDroneException exc) {
                    exc.printStackTrace();
                }
            });


            do {
            } while(true);


        } catch (Exception exc) {
            //Forbindelse blev ikke oprettet
            exc.printStackTrace();
        } finally {
            if (drone != null) {
                drone.stop();
            }
            System.exit(0);
        }
    }
}
