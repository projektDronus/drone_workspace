package de.yadrone.apps.hoops;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class HoopMain {
    public static void main(String[] args){
        System.out.println("Starter...");

        IARDrone drone = null;
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

            System.out.println("Klar til takeoff: ");
            scanner.next();

            countdown(3);
            //drone.getCommandManager().waitFor(3000);
            drone.getCommandManager().takeOff();
            drone.hover();
            drone.getCommandManager().waitFor(15000);
            countdown(15);
            drone.getCommandManager().landing();

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

    private static void countdown(int n) throws InterruptedException {
        for (int i = n; i > 0; i--) {
            System.out.println(i);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
