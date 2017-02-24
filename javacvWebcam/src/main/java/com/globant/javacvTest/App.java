package com.globant.javacvTest;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.opencv.imgproc.Imgproc;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) throws Exception{
    OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
    OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
    grabber.start();
    IplImage grabbedImage = converter.convert(grabber.grab());
    IplImage imgGray= cvCreateImage(cvGetSize(grabbedImage),IPL_DEPTH_8U,1);
    IplImage imgHsv= cvCreateImage(cvGetSize(grabbedImage),IPL_DEPTH_8U,3);
    
    CanvasFrame canvasFrame = new CanvasFrame("Cam");
    CanvasFrame canvasFrame1 = new CanvasFrame("Gray Image");
    CanvasFrame canvasFrame2 = new CanvasFrame("HSV Image");
    canvasFrame.setCanvasSize(grabbedImage.width(), grabbedImage.height());
    canvasFrame1.setCanvasSize(grabbedImage.width(), grabbedImage.height());
    canvasFrame2.setCanvasSize(grabbedImage.width(), grabbedImage.height());
    
    while(canvasFrame.isVisible()&&(grabbedImage!=null)){
      cvCvtColor(grabbedImage,imgGray,Imgproc.COLOR_BGR2GRAY);
      cvCvtColor(grabbedImage,imgHsv,Imgproc.COLOR_BGR2HSV);
      
      canvasFrame.showImage(converter.convert(grabbedImage));
      canvasFrame1.showImage(converter.convert(imgGray));
      canvasFrame2.showImage(converter.convert(imgHsv));
      grabbedImage = converter.convert(grabber.grab());
    }
    grabber.stop();
    canvasFrame.dispose();
  }
}
