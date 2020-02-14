package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class ProfileActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_fname;
    private EditText et_lname;
    private EditText et_DOB;
    private EditText et_phone;
    private Button btn_edit;
    private TextView et_name;

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private ProgressDialog progressDialog;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_profile, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        et_fname=findViewById(R.id.et_fname);
        et_name=findViewById(R.id.et_name);
        et_lname=findViewById(R.id.et_lname);
        et_DOB=findViewById(R.id.et_dob);
        et_phone=findViewById(R.id.et_phone);
        btn_edit=findViewById(R.id.btn_edit);

        progressDialog=new ProgressDialog(this);

        btn_edit.setOnClickListener(this);
        et_DOB.setOnClickListener(this);
        getUserData();


    }

    private void getUserData() {
        String uid=user.getUid();
        progressDialog.setMessage("Getting user Profile info");
        progressDialog.show();
        DocumentReference documentReference=db.collection("users").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document=task.getResult();
                if (document.exists()) {
                    Log.d("Document", "DocumentSnapshot data: " + document.getData());
                    //customer=new Customer(document.get("email").toString(),document.get("fName").toString(),document.get("lName").toString(),(Integer) (document.get("phone")),document.get("DOB").toString());
                    et_DOB.setText(document.get("DOB").toString());
                    et_lname.setText(document.get("lName").toString());
                    et_fname.setText(document.get("fName").toString());
                    et_phone.setText(document.get("phone").toString());
                    et_name.setText(document.get("fName").toString()+" "+document.get("lName").toString());
                    progressDialog.hide();
                } else {
                    Log.d("Document", "No such document");
                }
            }

        });
    }

    @Override
    public void onClick(View v) {
        if(v==et_DOB){
            calendar=Calendar.getInstance();
            year=calendar.get(Calendar.YEAR);
            month=calendar.get(Calendar.MONTH);
            dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);


            datePickerDialog=new DatePickerDialog(ProfileActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            et_DOB.setText(day +"/" + (month+1) + "/" + year);
                        }
                    },year,month,dayOfMonth);
            datePickerDialog.show();
        }
    }
}
