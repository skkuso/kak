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
 * Created by Heng on 22/11/2558.
 */
public class DrawContour extends Activity {

    private ImageView img;
    private Mat src = new Mat();

    private Mat imgage = new Mat();
    private Mat mMaskMat = new Mat();
    private Mat mDilatedMat = new Mat();
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


        //Mat source = new Mat(myBitmap.getWidth(), myBitmap.getHeight(), CvType.CV_8UC1);

        Utils.bitmapToMat(myBitmap, src);

        //1.Color space conversion :
        Imgproc.cvtColor(src, imgage, Imgproc.COLOR_RGB2HSV, 4);

        //2. Image Processing :
        Scalar lowerThreshold = new Scalar ( 120, 100, 100 ); // Blue color – lower hsv values
        Scalar upperThreshold = new Scalar ( 179, 255, 255 ); // Blue color – higher hsv values
        Core.inRange(imgage, lowerThreshold, upperThreshold, mMaskMat);

        //3.Performing morphological operations :
        Imgproc.dilate ( mMaskMat, mDilatedMat, new Mat() );

        //4. Finding contours :
        final List<MatOfPoint> contours = new ArrayList<>();
        final Mat hierarchy = new Mat();
        Imgproc.findContours ( mDilatedMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE );

        //5. Drawing the contours :
        for ( int i=0; i < contours.size(); i++ )
        {
            //if(contours[i].size()>100)  // Minimum size allowed for consideration
           // {
                Imgproc.drawContours ( src, contours, i, new Scalar(0,255,0), 5);

           // }
        }

        //แสดงตัวหนังสือขึ้นบน mat ชื่อ src
        //แสดงข้อความ contours.size()
        //แสดงจุดไหนของกล้อง = กึ่งกลางหน้าจอ
        //ใช้ฟอนต์ตัวที่เท่าไหร่
        //ขนาดตัวหนังสือ
        //สีตัวหนังสือ = ขาว
        //ความหนา = 2
        Core.putText(src, "" + contours.size(), new Point(src.cols() / 2, src.rows() / 2), 20, 1, new Scalar(255, 255, 255, 255), 2);


        Utils.matToBitmap(src, myBitmap);

        img.setImageBitmap(myBitmap);

    }



    /*
    Imgproc.cvtColor(src, src, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.Canny(src, src, 50, 200);

        final List<MatOfPoint> contours = new ArrayList<>();
        final Mat hierarchy = new Mat();


        //Find contours
        Imgproc.findContours(src, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        //contours
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            Imgproc.drawContours(src, contours, contourIdx, new Scalar(0, 255, 0), 1);
        }
        //Imgproc.drawContours(source, contours, -1, new Scalar(0, 255, 0), 3);

    * */


}
