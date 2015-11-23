package app.heng.opencv;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ColorBlobDetectionActivity extends Activity implements OnTouchListener, CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";

    private boolean mIsColorSelected = false;
    private Mat mRgba;
    private Scalar mBlobColorRgba;
    private Scalar mBlobColorHsv;
    private ColorBlobDetector mDetector;
    private Mat mSpectrum;
    private Size SPECTRUM_SIZE;
    private Scalar CONTOUR_COLOR;
    // private CameraBridgeViewBase mOpenCvCameraView;
    // private MyCameraView mOpenCvCameraView;
    private Tutorial3View mOpenCvCameraView;

    private int th1=0;
    private int th2=255;

    private Button ButtonGoToSecond;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(ColorBlobDetectionActivity.this);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public ColorBlobDetectionActivity() { //construct??
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.color_blob_detection_surface_view);


        ButtonGoToSecond = (Button) findViewById(R.id.button2);

        //mOpenCvCameraView = (MyCameraView) findViewById(R.id.color_blob_detection_activity_surface_view);//ทำให้ ออบรู้จักกัน
        mOpenCvCameraView = (Tutorial3View) findViewById(R.id.color_blob_detection_activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);



        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        th1 = progress;
        Core.putText(mRgba, "" + th1, new Point(mRgba.cols() / 2, mRgba.rows() / 2), 3, 1, new Scalar(255, 255, 255, 255), 2);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
});

        SeekBar seekBar2 = (SeekBar)findViewById(R.id.seekBar2);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                th2=progress;
                Core.putText(mRgba, "" + th2, new Point(mRgba.cols() / 2, mRgba.rows() / 2), 3, 1, new Scalar(255, 255, 255, 255), 2);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        ButtonGoToSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create wbc_photo directory
                File wbcDirectory = new File(Environment.getExternalStorageDirectory() + "/wbc_photo/");
                wbcDirectory.mkdirs();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                String currentDateandTime = sdf.format(new Date());
                final String fileName = Environment.getExternalStorageDirectory().getPath() +
                        "/wbc_photo/wbc_photo_" + currentDateandTime + ".jpg";
                mOpenCvCameraView.takePicture(fileName);  //Take Photo================================

                //    mOpenCvCameraView.disableView();

                Log.i(TAG, "pass");
                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //ขณะที่เวลาลดลงทำอะไร
                    }

                    @Override
                    public void onFinish() {
                        //นับถอยหลังเสร็จแล้ว จะทำอะไรต่อ

                        //สั่งแสดงไฟล์รูปภาพที่แกลอรี่
                        File newPhotoFile = new File(fileName);
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(newPhotoFile));
                        sendBroadcast(intent);

                        //เอาไว้ส่ง ไปหน้าต่อไปนะ ====================================================edit
                        Intent i = new Intent(getApplicationContext(), Ellipse.class); //ประกาศ i ให้ส่งค่าที่ได้ไป หน้า Second
                        i.putExtra("MyPhoto", fileName); //ให้ i ส่งชื่อที่อยู่ไฟล์ และก็ให้มันเก็บไว้ที่ MyPhoto

                     //  mOpenCvCameraView.disableView();//ปิดกล้อง
                        startActivity(i);

                    }
                }.start();

            }


        });


    }

    @Override
    public void onPause() //ฟังก์ชัน onPause ทำงานเมื่อแอปฯ ถูกปิดหรือย่อไว้ชั่วคราว
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView(); //หยุดการทำงานถ้า ฟังก์ชันที่ถูกเรียกใช้ไม่ว่าง
    }

    @Override
    public void onResume() //ฟังก์ชัน onResume ทำงานเมื่อแอปฯ ถูกเปิดหรือกลับมาทำงานอีกครั้ง
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);//เริ่มต้นของ opencv ด้วยเวอชัน และทำการเชื่อมข้อมูล
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) { //เรียกกล้องขึ้นมาใ้
        mRgba = new Mat(height, width, CvType.CV_8UC4);  //width - - ความกว้างของภาพที่จะถูกส่งไป
        mDetector = new ColorBlobDetector();
        mSpectrum = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
        SPECTRUM_SIZE = new Size(200, 64);
        CONTOUR_COLOR = new Scalar(255, 0, 0, 255);   //สีของเส้น


    }

    public void onCameraViewStopped() {
        mRgba.release();
    }

    //เมื่อไหร่ที่มีการเคลื่อนไหวเกิดขึ้น (เกี่ยวกับการสัมผัสอุปกรณ์)------------------------
    public boolean onTouch(View v, MotionEvent event) {
        int cols = mRgba.cols();  //get resolution of display
        int rows = mRgba.rows();

        int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;  //get resolution of display
        int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;

        int x = (int) event.getX() - xOffset;  //get resolution of display
        int y = (int) event.getY() - yOffset;

        //ที่ที่เราไปสัมผัสหน้าจอ
        Log.i(TAG, "Touch image coordinates: (" + x + ", " + y + ")");

        //เช็คว่าที่นั่นอยู่ในขอบเขตของหน้าจอมั้ย?
        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;

        Rect touchedRect = new Rect();

        //ดูให้แน่ใจว่ามันมีอยู่ ๔ อันมั้ย?--------------------------------
        touchedRect.x = (x > 4) ? x - 4 : 0;
        touchedRect.y = (y > 4) ? y - 4 : 0;


        // If  x+4 < cols then ?"" else :""
        touchedRect.width = (x + 4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
        touchedRect.height = (y + 4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;

        Mat touchedRegionRgba = mRgba.submat(touchedRect);//นี่ไง ที่สัมผัส

        //แปลง new Mat ไปเป็น HSV
        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width * touchedRect.height; //-----------------------------==================
        for (int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;

        //converts scalar to hsv to RGB
        mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);

        Log.i(TAG, "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")"); //===========================show color ===============================================

        mDetector.setHsvColor(mBlobColorHsv); // เรียกฟังก์ชันมาจาก ColorBlobDeteceor มา----------------------

        //ปรับขนาดรูปภาพ
        Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE);

        mIsColorSelected = true;

        // Release all mats
        touchedRegionRgba.release();
        touchedRegionHsv.release();

        return false; // don't need subsequent touch events
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();  // mRbga = input frame with color
        MatOfPoint2f approxCurve = new MatOfPoint2f();

        if (mIsColorSelected) {
            mDetector.process(mRgba);
            final List<MatOfPoint> contours = mDetector.getContours();  //contour info is ready in detector
            //Log.e(TAG, "Contours count: " + contours.size());


            //แสดงตัวหนังสือขึ้นบน mat ชื่อ mRgba
            //แสดงข้อความ contours.size()
            //แสดงจุดไหนของกล้อง = กึ่งกลางหน้าจอ
            //ใช้ฟอนต์ตัวที่เท่าไหร่
            //ขนาดตัวหนังสือ
            //สีตัวหนังสือ = ขาว
            //ความหนา = 2
            Core.putText(mRgba, "" + contours.size(), new Point(mRgba.cols() / 2, mRgba.rows() / 2), 3, 1, new Scalar(255, 255, 255, 255), 2);





            //original code
            //Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);   //draw contour around detected area

            //green contous and size=5
            Imgproc.drawContours(mRgba, contours, -1, new Scalar(0, 255, 0), 3);

            //mOpenCvCameraView.enableFpsMeter();


//          Producing spectrum

            Mat colorLabel = mRgba.submat(4, 68, 4, 68);
            colorLabel.setTo(mBlobColorRgba);
            Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
            mSpectrum.copyTo(spectrumLabel); //เป็นการคัดลอกบางส่วนของ spectrumLabel ไปที่ mSpectrum
        }

        //hengKak(mRgba);

        return mRgba;
    }


    //final conversion
    private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }

    private void hengKak(Mat source){

        Imgproc.cvtColor(source, source, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(source, source, new Size(3, 3));

        Mat threshold_output = new Mat();
        Mat edge = new Mat();
        final List<MatOfPoint> contours = new ArrayList<>();
        final MatOfInt4 hierarchy = new MatOfInt4();



        //Imgproc.GaussianBlur(source, source, new Size(5, 5), 0); //ค่าสุดท้าย 15


        //Detect Canny, edges Find contours, Find the rotated rectangles and ellipses for each contour, Draw contours, rotated rectangles and ellipses


        //canny   //threshold

        //Imgproc.threshold(src, src, thresh, max_thresh, Imgproc.THRESH_TOZERO);   //THRESH_TOZERO
        Imgproc.Canny(source, edge, th1, th2,3,true);
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
            //Core.ellipse(drawing, minEllipse.get(i), new Scalar(0, 255, 0), 2, 8); //เอา drawing(zero)วาดวงรี

        }

        mRgba = drawing;
    }


}
