package app.heng.opencv;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.net.URI;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.Utils;


/**
 * Created by Heng on 9/11/2558.
 */
public class Second extends Activity {

    private ImageView img;
    private Mat src = new Mat();
    private Mat source = new Mat();
    private Mat imgage = new Mat();
    private MatOfPoint2f test = new MatOfPoint2f();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        //Bundle bundle = getIntent().getExtras();
        //int value = bundle.getInt("MyV"); เผื่อไว้ส่งอะไรที่มีประโยชน์มากกว่านี้

        Bundle bundle = getIntent().getExtras();
        String nameLatestPhoto = bundle.getString("MyPhoto");
        Log.i("name of latest photo: ", nameLatestPhoto);

        //เชื่อม obj
        img = (ImageView)findViewById(R.id.imageView);

            Bitmap myBitmap = BitmapFactory.decodeFile(nameLatestPhoto);
        //ถอดรหัสไฟล์ภาพ จาก Uri
        //ถอดเสร็จให้เก็บในตัวแปรสำหรับเก็บภาพ Bitmap
          //  img.setImageBitmap(myBitmap); // set ภาพให้ image view เป็นภาพแบบ bitmap



        double alpha = 0.2;
        int beta = 100;
        Utils.bitmapToMat(myBitmap, source);
        source.convertTo(source, -1, alpha, beta);  //สว่าง
        //Imgproc.equalizeHist(source, source);  //ความเข้มแสง
        //Imgproc.GaussianBlur(source, source, new Size(0,0), 15); //ความคมชัด
        Utils.matToBitmap(source, myBitmap);


        //threshold-----

       // Utils.bitmapToMat(myBitmap, imgage);

       // Imgproc.cvtColor(imgage, imgage, Imgproc.COLOR_BGRA2GRAY, 1);
       // Imgproc.threshold(imgage, imgage, 177, 255, Imgproc.THRESH_OTSU); //THRESH_TOZERO // 90,255 ถ่ายรูป ไม่ตรง ได้รูปที่ต้องหาร


      //  Utils.matToBitmap(imgage, myBitmap);

        //fit Ellipse========================================================

        //Imgproc.fitEllipse(test);
        //Mat circles;
        //Imgproc.HoughCircles(source,circles,4,3);


        //=================================================================

        img.setImageBitmap(myBitmap);

        //เพิ่มความสว่างให้กับรูปภาพ
        //Mat destination = new Mat(myBitmap.rows(),myBitmap.cols();

        /*
        double alpha = 2;
        int beta = 50;
        Utils.bitmapToMat(myBitmap, source);
        source.convertTo(source, -1, alpha, beta);
        Imgproc.equalizeHist(source, destination);
        Utils.matToBitmap(source, myBitmap);

        //canny
        Utils.bitmapToMat(myBitmap, src);

        Imgproc.Canny(src, src, 25, 45); // 30, 150 ได้นิดหน่อย  30, 35 ก็ได้นะ 30, 177 ได้มาแต่จุด รอบๆ 30, 45 โอเคนะ
        //35, 45 ก็ได้นะ วงชัดละ 30,15 ไม่ค่อยโอ 25, 50 ก็ใช้ได้นะ  300, 600, 5, true เห็นวงชัดขึ้น ของเม็ดแดงด้วยนะ แต่ขาวก็ยังเข้มกว่า

        Utils.matToBitmap(src, myBitmap);
        img.setImageBitmap(myBitmap);
        */


    }

}
