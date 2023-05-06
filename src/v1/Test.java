package v1;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

public class Test {
    public static void main(String[] args)
    {
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Tesseract tesseract = new Tesseract();
//        String filename = "D:\\\\tess_handwriting_test1.jpg";
//        Mat imageOrigin = Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);
//        Mat imageGray = new Mat();
        MatOfByte mat = new MatOfByte();
//       
        
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        tesseract.setDatapath(tessDataFolder.getAbsolutePath());
        
        try {
//          tesseract.setDatapath("D:/Tess4J/tessdata");
//        	GrayScale
//        	Imgproc.cvtColor(imageOrigin, imageGray, Imgproc.COLOR_RGB2GRAY);
//        	HighGui.imshow("imageGray", imageGray);
//        	Imgcodecs.imwrite("D:\\\\scaledImage8.jpg", imageGray);
//        	
        	String imagePath = "D:\\\\testImages\\testImage19.jpg";
            Mat image = Imgcodecs.imread(imagePath);

            // Convert the image to grayscale
            Mat grayImage = new Mat();
            Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
            System.out.println(grayImage.channels());
            // Bit-plane slicing
//            for (int i = 0; i < 8; i++) {
                Mat bitPlane = new Mat();
                Core.extractChannel(grayImage, bitPlane, 0);
                Imgproc.threshold(bitPlane, bitPlane, 127, 255, Imgproc.THRESH_BINARY);
                Imgcodecs.imwrite("D:\\output\\bpimage0.jpg", bitPlane);
//            }
            // the path of your tess data folder
            // inside the extracted file
            String text
                = tesseract.doOCR(new File("D:\\output\\bpimage0.jpg"));
  
            // path of your image file
            System.out.print(text);
            
            //show scaled image on frame
            Imgcodecs.imencode(".jpg", bitPlane, mat); 
            byte[] byteArray = mat.toArray(); 
            InputStream in = new ByteArrayInputStream(byteArray); 
            BufferedImage buf;
    		try {
    			buf = ImageIO.read(in);
    			JFrame fr = new JFrame(); 
    			fr.getContentPane().add(new JLabel(new ImageIcon(buf))); 
                fr.pack();
                fr.setVisible(true);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
           
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}