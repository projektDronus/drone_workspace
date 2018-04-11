package de.yadrone.apps.hoops;


import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;


public class ImageDrawMachine {
    static double[][] runningBiggestCircles = new double[10][3];
    static int run = 0;
    static int width = 640;
    static int hight = 360;

    static Mat frame;
    static Mat gray;
    static Mat circles;
    //static BufferedImage responseImage;
    public static BufferedImage makecircles(BufferedImage image) {
        frame = toMatrix(image);
        gray = new Mat();
        circles = new Mat();

        // Lav billede grayscale
        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_RGB2GRAY);
        // Blurbillede for at undgå falskt positive cirkler
        Imgproc.GaussianBlur(gray, gray, new Size(9,9), 2, 2);

        // Find cirkler
        int minRadius = 100;
        int maxRadius = 360;
        Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, 2, minRadius, 120, 90, minRadius, maxRadius);

        // Tegn cirkler (og udskriv)
        double[] data;
        int radius;
        Point pt = new Point();

        // Kun største cirkel
        if (0<circles.cols()) {
            data = circles.get(0, 0);
            pt.x = data[0];
            pt.y = data[1];
            radius = (int) Math.round(data[2]);

            // udskriv log
            //System.out.println("Circle");
            //System.out.println("  center      : (" + pt.x + "; " + pt.y + ")");
            //System.out.println("  radius      : " + radius);

            // centrum
            //Imgproc.circle(gray, pt, 3, new Scalar(0, 255, 0), -1, 8, 0);
            // outline
            Imgproc.circle(frame, pt, radius, new Scalar(0, 0, 255), 2, 8, 0);

            run = (run+1)%10;
            runningBiggestCircles[run] = data;

//            double[] avg_center = new double[2];
//            avg_center[0] = 0;
//            avg_center[1] = 0;
//            int avg_radius = 0;
//            for (int i = 0; i< runningBiggestCircles.length; i++){
//                avg_center[0]+=runningBiggestCircles[i][0];
//                avg_center[1]+=runningBiggestCircles[i][1];
//                avg_radius+=runningBiggestCircles[i][2];
//            }
//            avg_center[0]/=runningBiggestCircles.length;
//            avg_center[1]/=runningBiggestCircles.length;
//            avg_radius/=runningBiggestCircles.length;

            double[] avgc = avgCircle(runningBiggestCircles, 0.05);
            if (avgc!=null){
                pt.x = avgc[0];
                pt.y = avgc[1];

                // udskriv log
                //System.out.println("avgircle");
                //System.out.println("  center      : (" + pt.x + "; " + pt.y + ")");
                //System.out.println("  radius      : " + avgc[2]);

                // Tegn
                Imgproc.circle(frame, pt, (int) avgc[2], new Scalar(255, 0, 0), 4, 8, 0);
                Imgproc.circle(frame, pt, 3, new Scalar(0, 255, 0), -1, 8, 0);
            }
        }

        return fromMatrix(frame);
    }

    private static double[] avgCircle(double[][] runningBiggestCircles, double x) {
        // Array 7x3, varierer de mere end x pct
        double xmaxdiff = (width*x);
        double ymaxdiff = (hight*x);
        double sumr, sumx, sumy;
        sumr = sumx = sumy = 0;
        boolean rbool,xbool,ybool;
        rbool = xbool = ybool = false;

        for(int i = 0; i<runningBiggestCircles.length;i++){
            sumr = (sumr+runningBiggestCircles[i][2]);
        }


        //Udregninger for radius
        double ravg = (sumr/runningBiggestCircles.length);
        double rmaxdiff = (ravg*x);

        for (int i = 0; i < runningBiggestCircles.length ; i++) {
            if ((ravg-rmaxdiff) < runningBiggestCircles[i][2] && runningBiggestCircles[i][2] < (ravg+rmaxdiff)){
                rbool=true;
            } else {
                rbool=false;
            }
        }

        //Udregninger for x
        for(int i = 0; i<runningBiggestCircles.length;i++){
            sumx = (sumx+runningBiggestCircles[i][0]);
        }

        double xavg = (sumx/runningBiggestCircles.length);

        for (int i = 0; i < runningBiggestCircles.length ; i++) {
            if ((xavg-xmaxdiff) < runningBiggestCircles[i][0] && runningBiggestCircles[i][0] < (xavg+xmaxdiff)){
                xbool=true;
            } else {
                xbool=false;
            }
        }


        //Udregninger for y
        for(int i = 0; i<runningBiggestCircles.length;i++){
            sumy = (sumy+runningBiggestCircles[i][1]);
        }

        double yavg = (sumy/runningBiggestCircles.length);
        for (int i = 0; i < runningBiggestCircles.length ; i++) {
            if ((yavg-ymaxdiff) < runningBiggestCircles[i][1] && runningBiggestCircles[i][1] < (yavg+ymaxdiff)){
                ybool=true;
            } else {
                ybool=false;
            }
        }

        if (ybool && xbool && rbool){
            return new double[]{xavg,yavg,ravg};
        }
        return null;
    }


    // Tyvstjålet fra https://github.com/Trivivium/CDIO-4/blob/master/src/cdio/utilities/ImageUtils.java
    private static BufferedImage fromMatrix(Mat matrix) {
        BufferedImage image;


        if(matrix.channels() > 1) {
            image = new BufferedImage(matrix.width(), matrix.height(), BufferedImage.TYPE_3BYTE_BGR);
        }
        else {
            image = new BufferedImage(matrix.width(), matrix.height(), BufferedImage.TYPE_BYTE_GRAY);
        }

        final byte[] source = new byte[matrix.width() * matrix.height() * matrix.channels()];
        final byte[] dest = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        matrix.get(0, 0, source);
        System.arraycopy(source, 0, dest, 0, source.length);

        return image;
    }
    // Tyvstjålet fra https://github.com/Trivivium/CDIO-4/blob/master/src/cdio/utilities/ImageUtils.java
    private static Mat toMatrix(BufferedImage image) {
        Mat mat  = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        mat.put(0, 0, data);

        return mat;
    }
}
