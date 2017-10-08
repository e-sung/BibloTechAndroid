package com.esung.biblotechandroid.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.esung.biblotechandroid.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import static com.esung.biblotechandroid.Utility.IntentTag.BOOK_ID;

public class QRScanActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION_ID = 1001;
    private SurfaceView mSurfaceView;
    private BarcodeDetector mBarcodeDetector;
    private CameraSource mCameraSource;
    private boolean mCodeDetected;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION_ID:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED) {
                        return;
                    }
                    try {
                        mCameraSource.start(mSurfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        mSurfaceView = (SurfaceView) findViewById(R.id.sv_surfaceView);
        mBarcodeDetector = new BarcodeDetector.Builder(this).
                setBarcodeFormats(Barcode.QR_CODE).build();
        mCameraSource = new CameraSource.Builder(this, mBarcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(QRScanActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(QRScanActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION_ID
                    );
                    return;
                }
                try {
                    mCameraSource.start(mSurfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCode = detections.getDetectedItems();
                if (qrCode.size() != 0) {
                    mSurfaceView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mCodeDetected == false) {
                                mCodeDetected = true;
                                Intent intent = new Intent(getApplicationContext(), RentalActivity.class);
                                int bookId = Integer.parseInt(qrCode.valueAt(0).displayValue);
                                intent.putExtra(BOOK_ID, bookId);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        mCodeDetected = false;
        super.onResume();
    }
}
