package de.yadrone.apps.controlcenter;

import de.yadrone.base.ARDrone;
import de.yadrone.base.video.ImageListener;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class YADroneControlCenter
{	
	private ARDrone ardrone=null;
	
	public YADroneControlCenter(){
		initialize();
	}
	
	private void initialize(){
		try
		{
			ardrone = new ARDrone();
			System.out.println("Connect drone controller");
			ardrone.start();

//			ardrone.getVideoManager().addImageListener(new ImageListener() {
//				private File outputfile;
//				private int count = 0;
//				@Override
//				public void imageUpdated(BufferedImage image) {
//					System.out.println("Modtog frame");
//					outputfile = new File("video/"+count+".jpg");
//					try {
//						ImageIO.write(image, "jpg", outputfile);
//					} catch (Exception e){
//						e.printStackTrace();
//					}
//					count ++;
//				}
//			});

			
			new CCFrame(ardrone);
		}
		catch(Exception exc)
		{
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