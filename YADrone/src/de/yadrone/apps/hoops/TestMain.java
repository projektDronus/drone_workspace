package de.yadrone.apps.hoops;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.exception.ARDroneException;
import de.yadrone.base.exception.IExceptionListener;
import de.yadrone.base.video.ImageListener;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import org.opencv.core.Core;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public class TestMain extends Application{
    public static void main(String[] args){
        // load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

    private static void countdown(int n) throws InterruptedException {
        for (int i = n; i > 0; i--) {
            System.out.println(i);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        primaryStage.setTitle("billede");
        Scene primaryScene = new Scene(root, 640, 500);
        primaryStage.setScene(primaryScene);
        primaryStage.show();

        System.out.println("Starter...");

        Button button = (Button) primaryScene.lookup("#button");
        ImageView imgview = (ImageView) primaryScene.lookup("#imageview");

        IARDrone drone = null;
        try {
            // Forbindelse blev oprettet
            drone = new ARDrone();

            TestController controller = new TestController(drone);
            primaryScene.setOnKeyPressed(controller);
            primaryScene.setOnKeyReleased(controller);


            drone.start();

            // Prøv med et større billede
            drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);
            drone.getVideoManager().reinitialize();

            drone.addExceptionListener(new IExceptionListener() {
                public void exeptionOccurred(ARDroneException exc) {
                    exc.printStackTrace();
                }
            });

            drone.getVideoManager().addImageListener(new ImageListener() {
                int count = 0;
                BufferedImage drawImage;
                @Override
                public void imageUpdated(BufferedImage image) {
                    drawImage = ImageDrawMachine.makecircles(image);
                    imgview.setImage(SwingFXUtils.toFXImage(drawImage, null));
                    count++;
                    //System.out.println(count);
                }
            });

        } catch (Exception exc) {
            //Forbindelse blev ikke oprettet
            exc.printStackTrace();
        } finally {
//            if (drone != null) {
//                drone.stop();
//            }
            //System.exit(0);
        }
    }
}
