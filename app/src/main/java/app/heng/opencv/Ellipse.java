package app.heng.opencv;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heng on 20/11/2558.
 */
public class Ellipse extends Activity {
    private ImageView img;
    private Mat src = new Mat();

    private Mat imgage = new Mat();
    private Mat src_gray;
    private int thresh = 73;
    //private int thresh = 100;

    private int points = 50;
    private int max_thresh = 255;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ellipse);


        Bundle bundle = getIntent().getExtras();
        String nameLatestPhoto = bundle.getString("MyPhoto");
        Log.i("name of latest photo: ", nameLatestPhoto);

        //เชื่อม obj
        img = (ImageView) findViewById(R.id.imageView);

        Bitmap myBitmap = BitmapFactory.decodeFile(nameLatestPhoto);
        //ถอดรหัสไฟล์ภาพ จาก Uri
        //ถอดเสร็จให้เก็บในตัวแปรสำหรับเก็บภาพ Bitmap
        //img.setImageBitmap(myBitmap); // set ภาพให้ image view เป็นภาพแบบ bitmap


        // แปลงภาพเป็น Gray และ blur

        Mat source = new Mat(myBitmap.getWidth(), myBitmap.getHeight(), CvType.CV_8UC1);


        Utils.bitmapToMat(myBitmap, source);

        Imgproc.cvtColor(source, source, Imgproc.COLOR_RGB2GRAY);
        //Imgproc.blur(source, source, new Size(3, 3));

        Mat threshold_output = new Mat();
        Mat edge = new Mat();
        int lowThreshold = 60;
        int ratio = 3;
        final List<MatOfPoint> contours = new ArrayList<>();
        final MatOfInt4 hierarchy = new MatOfInt4();

        //Imgproc.GaussianBlur(source, source, new Size(5, 5), 0); //ค่าสุดท้าย 15

        //Detect Canny, edges Find contours, Find the rotated rectangles and ellipses for each contour, Draw contours, rotated rectangles and ellipses

        //canny   //threshold
        Imgproc.Canny(source, edge, lowThreshold, lowThreshold * ratio, 3,true);
        edge.convertTo(threshold_output, CvType.CV_8U);


        //Find contours
        Imgproc.findContours(threshold_output, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));


        // Find the rotated rectangles and ellipses for each contour
        final List<RotatedRect> minRect = new ArrayList<RotatedRect>(contours.size());
        final List<RotatedRect> minEllipse = new ArrayList<RotatedRect>(contours.size());


        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint2f tempMatOfPoint2f = new MatOfPoint2f(contours.get(i).toArray());
            minRect.add(Imgproc.minAreaRect(tempMatOfPoint2f));
            if (contours.size() > 93) {
                minEllipse.add(Imgproc.fitEllipse(tempMatOfPoint2f));
            }
        }

        //Draw contours + rotated rects + ellipses
        Mat drawing = Mat.zeros(threshold_output.size(), CvType.CV_8UC3);

        for (int i = 0; i < contours.size(); i++) {

            //contours
            Imgproc.drawContours(drawing, contours, i, new Scalar(0, 255, 0), 1,8,new MatOfInt4(),0,new Point()); //เอารูปไปวาด contours ใส่

            //ellipse
          //  Core.ellipse(drawing, minEllipse.get(i), new Scalar(0, 0, 255), 2, 8); //เอา drawing(zero)วาดวงรี

        }



        Utils.matToBitmap(drawing, myBitmap);

        //Utils.matToBitmap(drawing, myBitmap);//-*-

        img.setImageBitmap(myBitmap);
    }



    /*
        //newwww==================================================
        // Detect edges using Threshold
        Utils.bitmapToMat(myBitmap, imgage);
        Imgproc.cvtColor(imgage, imgage, Imgproc.COLOR_BGRA2GRAY, 1);
        Imgproc.threshold(imgage, imgage, thresh, max_thresh, Imgproc.THRESH_BINARY);   //THRESH_TOZERO

        // Find the convex hull object for each contour
        List<MatOfPoint> hull = new ArrayList<>();


        Utils.matToBitmap(imgage, myBitmap);
        */

}
