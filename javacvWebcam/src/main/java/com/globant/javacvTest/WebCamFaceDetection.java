package com.globant.javacvTest;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;

import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class WebCamFaceDetection {
  public static void main(String[] args) throws Exception {
    CvHaarClassifierCascade faceClassifier =
        FaceRecognition.loadHaarClassifier("C:/opencv/sources/data/haarcascades/haarcascade_frontalface_alt.xml");
    OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
    OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
    grabber.start();
    IplImage grabbedImage = converter.convert(grabber.grab());
    IplImage imgGray = cvCreateImage(cvGetSize(grabbedImage), IPL_DEPTH_8U, 1);

    CvMemStorage faceStorage = CvMemStorage.create();
    CanvasFrame canvasFrame = new CanvasFrame("Cam");
    canvasFrame.setCanvasSize(grabbedImage.width(), grabbedImage.height());

    while (canvasFrame.isVisible() && (grabbedImage != null)) {
      FaceRecognition.findAndMarkObjects(faceClassifier, faceStorage, CvScalar.GREEN, imgGray, grabbedImage);
      canvasFrame.showImage(converter.convert(grabbedImage));
      grabbedImage = converter.convert(grabber.grab());
    }
    grabber.stop();
    canvasFrame.dispose();
  }
}
