package com.globant.javacvTest;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvFlip;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvLoad;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static org.bytedeco.javacpp.opencv_core.LINE_AA;

import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

public class FaceRecognition {
  public static void main(String[] args) throws java.lang.Exception {
    CvHaarClassifierCascade faceClassifier =
        loadHaarClassifier("C:/opencv/sources/data/haarcascades/haarcascade_frontalface_alt.xml");
    IplImage grabbedImage = cvLoadImage("C:\\Users\\m.gigena\\Pictures\\Camera Roll\\Smile_3.jpg");

    IplImage mirrorImage = grabbedImage.clone();
    IplImage grayImage = IplImage.create(mirrorImage.width(), mirrorImage.height(), IPL_DEPTH_8U, 1);

    CvMemStorage faceStorage = CvMemStorage.create();

    CanvasFrame frame = new CanvasFrame("Object Detection Demo", 1);

    while (true) {
      cvClearMemStorage(faceStorage);

      cvFlip(grabbedImage, mirrorImage, 1);

      cvCvtColor(mirrorImage, grayImage, COLOR_BGR2GRAY);

      findAndMarkObjects(faceClassifier, faceStorage, CvScalar.GREEN, grayImage, mirrorImage);
      OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
      frame.showImage(converter.convert(mirrorImage));
    }
  }

  public static void findAndMarkObjects(CvHaarClassifierCascade classifier, CvMemStorage storage, CvScalar color,
      IplImage inImage, IplImage outImage) throws java.lang.Exception {
    CvSeq faces = opencv_objdetect.cvHaarDetectObjects(inImage, classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
    int totalFaces = faces.total();
    CvRect r = null;
    for (int i = 0; i < totalFaces; i++) {
      r = new CvRect(cvGetSeqElem(faces, i));
      int x = r.x(), y = r.y(), w = r.width(), h = r.height();
      opencv_imgproc.cvRectangle(outImage,cvPoint(x,y),cvPoint(x+w,y+h), color, 1, LINE_AA, 0);
    }
    r.close();
  }

  public static CvHaarClassifierCascade loadHaarClassifier(String classifierName) {
    CvHaarClassifierCascade classifier = new CvHaarClassifierCascade(cvLoad(classifierName));
    if (classifier.isNull()) {
      System.err.println("Error handling classifier file \"" + classifier + "\".");
      System.exit(1);
    }
    return classifier;
  }
}
