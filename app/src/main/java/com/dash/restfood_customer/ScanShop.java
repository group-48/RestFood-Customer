package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ScanShop extends AppCompatActivity {

    private static final String TAG = "SCanShop";
    private TextView tv_code;
    private SurfaceView surfaceView;
    private QREader qrEader;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference ref=db.collection("shop");

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_shop);

        session = new Session(this);

        if(session.getMenu()==1){
            Intent intent=new Intent(ScanShop.this, CategoryList.class);
            intent.putExtra("shop", session.getShop());
            intent.putExtra("id",session.getShop());
            intent.putExtra("Browse","False");
            //Toast.makeText(this,session.getMenu(),Toast.LENGTH_LONG).show();
            Toast.makeText(this,session.getShop(),Toast.LENGTH_LONG).show();
            Toast.makeText(this,session.getBrowse(),Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        }


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
                        ref.document(data).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "Document exists!");
                                        Intent intent=new Intent(ScanShop.this, CategoryList.class);
                                        intent.putExtra("shop", data);
                                        intent.putExtra("id",data);
                                        intent.putExtra("Browse","False");
                                        session.setMenu(1);
                                        session.setShop(data);
                                        session.setBrowse("False");
                                        startActivity(intent);
                                    } else {
                                        Log.d(TAG, "Document does not exist!");
                                    }
                                } else {
                                    Log.d(TAG, "Failed with: ", task.getException());
                                }
                            }
                        });
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
