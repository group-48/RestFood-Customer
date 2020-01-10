package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

import android.Manifest;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ScanShop extends AppCompatActivity {

    private TextView tv_code;
    private SurfaceView surfaceView;
    private QREader qrEader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_shop);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        setupCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(ScanShop.this,"Enable camera permission to procees",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();


    }

    private void setupCamera() {
        tv_code=findViewById(R.id.tv_code);
        final ToggleButton btn_toggle=findViewById(R.id.btn_toggle);

        btn_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qrEader.isCameraRunning()){
                    btn_toggle.setChecked(false);
                    qrEader.stop();
                }
                else{
                    btn_toggle.setChecked(true);
                    qrEader.start();
                }

            }
        });

        surfaceView=findViewById(R.id.cameraView);
        setUpQReader();
    }

    private void setUpQReader() {
         qrEader=new QREader.Builder(this, surfaceView, new QRDataListener() {
             @Override
             public void onDetected(final String data) {
                tv_code.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_code.setText(data);
                    }
                });
             }
         }).facing(QREader.BACK_CAM)
            .enableAutofocus(true)
            .height(surfaceView.getHeight())
            .width(surfaceView.getWidth())
            .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if(qrEader!=null){
                            qrEader.initAndStart(surfaceView);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(ScanShop.this,"Enable camera permission to procees",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if(qrEader!=null){
                            qrEader.releaseAndCleanup();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(ScanShop.this,"Enable camera permission to procees",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

    }
}
