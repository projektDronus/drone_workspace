package de.yadrone.apps.hoops;

import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TestController implements EventHandler<KeyEvent> {
    private IARDrone drone;
    private CommandManager cmd;
    //private boolean droneBotOn = false;
    private boolean shiftflag = false;
    public TestController(IARDrone drone) {
        this.drone = drone;
        this.cmd = drone.getCommandManager();
    }

    @Override
    public void handle(KeyEvent event) {
//        if (event.getCode() == KeyCode.B)
//            droneBotOn = !droneBotOn;

//        if (droneBotOn){
//            System.out.println("AI til");
//        } else {
//            System.out.println("AI fra");
//        }

        System.out.println(event.getCode().getName());
        if (event.getCode() == KeyCode.ENTER) {
            autoControl();
            
        } else {
            control(event.getCode());
        }
        cmd.hover().doFor(actionTime/2);
    }

    private long actionTime = 1000/10;
    private int speed = 10;
    private void control(KeyCode code) {
        switch (code){
            case BACK_SPACE:
                System.out.println("let");
                cmd.takeOff();
                break;
            case T:
                cmd.landing();
                break;
            case S:
                cmd.stop();
                break;
            case LEFT:
                if (shiftflag) {
                    cmd.spinLeft(speed).doFor(actionTime);
                } else {
                    cmd.goLeft(speed).doFor(actionTime);
                }
                break;
            case RIGHT:
                if (shiftflag) {
                    cmd.spinRight(speed).doFor(actionTime);
                } else {
                    cmd.goRight(speed).doFor(actionTime);
                }
                break;
            case UP:
                if (shiftflag) {
                    cmd.up(speed).doFor(actionTime);
                } else {
                    cmd.forward(speed).doFor(actionTime);
                }
                break;
            case DOWN:
                if (shiftflag) {
                    cmd.down(speed).doFor(actionTime);
                } else {
                    cmd.backward(speed).doFor(actionTime);
                }
                break;
            case R:
                cmd.spinRight(speed).doFor(actionTime);
                break;
            case L:
                cmd.spinLeft(speed).doFor(actionTime);
                break;
            case U:
                cmd.up(speed).doFor(actionTime);
                break;
            case D:
                cmd.down(speed).doFor(actionTime);
                break;
            case E:
                drone.reset();
                break;
            case MINUS: // Plus
                speed++;
                System.out.println(drone.getSpeed());
                break;
            case EQUALS: // knap til højre for plus
                speed--;
                System.out.println(drone.getSpeed());
                break;
            case SHIFT:
                shiftflag = !shiftflag;
                System.out.println(shiftflag);
                break;
            case H:
                cmd.hover().doFor(actionTime);
        }
    }

    private void autoControl(){
        System.out.println("Gør noget...");
    }
}
