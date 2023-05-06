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
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import net.sourceforge.tess4j.Tesseract;

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
        
//        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
//        tesseract.setDatapath(tessDataFolder.getAbsolutePath());
        
        try {
//          tesseract.setDatapath("D:/Tess4J/tessdata");
//        	GrayScale
//        	Imgproc.cvtColor(imageOrigin, imageGray, Imgproc.COLOR_RGB2GRAY);
//        	HighGui.imshow("imageGray", imageGray);
//        	Imgcodecs.imwrite("D:\\\\scaledImage8.jpg", imageGray);
//        	
        	String imagePath = "D:\\\\testImages\\tess_handwriting_test1.jpg";
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
            
                
            // amended 23-05-07
            Mat dst = new Mat(bitPlane.rows(), bitPlane.cols(), bitPlane.type());
//            Mat kernel = Mat.ones(5,5, CvType.CV_32F);
//            Imgproc.morphologyEx(bitPlane, dst, Imgproc.MORPH_OPEN, kernel);
//            
//            Mat dst2 = new Mat(dst.rows(), dst.cols(), dst.type());
            Mat kernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((2*2) + 1, (2*2)+1));
            //Applying erosion on the Image
            Imgproc.erode(bitPlane, dst, kernel2);
            
            //Converting matrix to JavaFX writable image
            Imgcodecs.imwrite("D:\\\\output\\morpho1_1.jpg", dst);
            // the path of your tess data folder
            // inside the extracted file
            String text
                = tesseract.doOCR(new File("D:\\\\output\\morpho1_1.jpg"));
  
            // path of your image file
            System.out.print(text);
            
            //show scaled image on frame
            Imgcodecs.imencode(".jpg", dst, mat); 
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