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
 * Created by Heng on 21/11/2558.
 */
public class FitEllipse extends Activity {
    private ImageView img;
    private Mat src = new Mat();
    private Mat srcPrev = new Mat();

    private Mat imgage = new Mat();
    private Mat src_gray;
    //private int thresh = 73;
    private int thresh = 100;

    //private int points = 50;//===========================
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
        Imgproc.GaussianBlur(source, source, new Size(5, 5), 0); //ค่าสุดท้าย 15
        Utils.matToBitmap(source, myBitmap);


        Utils.bitmapToMat(myBitmap, src);

        Mat diff = new Mat(src.height(), src.width(), CvType.CV_8UC1);  //// change to the size of your image


        if (srcPrev != null) {
            Core.absdiff(src, srcPrev, diff);
            // Do some noise reduction
            Imgproc.threshold(diff, diff, 64, 255, Imgproc.THRESH_BINARY);

            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Mat mHierarchy = new Mat(0, 0, CvType.CV_8U);
            Imgproc.findContours(diff, contours, mHierarchy,
                    Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            if (contours.size() > 0) {
                MatOfPoint2f allPoints = new MatOfPoint2f();
                // allPoints.push_back(new
                // MatOfPoint2f(contours.get(0).toArray()));
                // Bumble through all the contours
                for (MatOfPoint matOfPoint : contours) {
                    MatOfPoint2f points = new MatOfPoint2f(matOfPoint.toArray());
                    allPoints.push_back(points);
                    // Draw a box around this specific area
                    RotatedRect box = Imgproc.minAreaRect(points);
                    Core.rectangle(src, box.boundingRect().tl(), box
                            .boundingRect().br(), new Scalar(180, 0, 0));
                }
                // Draw a box around the whole lot
                RotatedRect box = Imgproc.minAreaRect(allPoints);
                Core.rectangle(src, box.boundingRect().tl(), box
                        .boundingRect().br(), new Scalar(0, 0, 250));
            }

            srcPrev.release();
            diff.release();

        }

        srcPrev = src;


        Utils.matToBitmap(src, myBitmap);


        img.setImageBitmap(myBitmap);

    }
}
