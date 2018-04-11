package de.yadrone.apps.controlcenter;

import de.yadrone.base.ARDrone;
import de.yadrone.base.video.ImageListener;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class YADroneControlCenter {
	private ARDrone ardrone=null;
	
	public YADroneControlCenter(){
		initialize();
	}
	
	private void initialize(){
		try {
			ardrone = new ARDrone();
			System.out.println("Connect drone controller");
			ardrone.start();

			
			new CCFrame(ardrone);
		} catch(Exception exc) {
			exc.printStackTrace();
			
			if (ardrone != null)
				ardrone.stop();
			System.exit(-1);
		}
	}
		
	public static void main(String args[]){
		new YADroneControlCenter();
	}
}