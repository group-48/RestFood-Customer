package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dash.restfood_customer.models.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

import static android.telephony.PhoneNumberUtils.isGlobalPhoneNumber;

public class ProfileActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_fname;
    private EditText et_lname;
    private TextView et_DOB;
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
    int flag=0;

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
        if(v==et_DOB && flag==1){
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
        //make fields editable
        if(v==btn_edit && flag==0){
            flag=1;
            et_fname.setEnabled(true);
            et_lname.setEnabled(true);
            et_DOB.setEnabled(true);
            et_phone.setEnabled(true);
            btn_edit.setText("Save");
        }
        //update
        else if(v==btn_edit && flag==1){
            if(!validate()){
                return;
            }
            updateUser();

        }
    }

    private boolean validate() {
        String fname=et_fname.getText().toString().trim();
        String lname=et_lname.getText().toString().trim();
        String dob=et_DOB.getText().toString().trim();
        String phone=et_phone.getText().toString().trim();

        String namePattern="[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)";

        int validity=1;

        //get current year
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int year = cal.get(Calendar.YEAR);

        if(TextUtils.isEmpty((phone))){
            et_phone.requestFocus();
            et_phone.setError("Phone No. cannot be empty");
            validity=0;
        }
        if (!isGlobalPhoneNumber(phone) || phone.length()>10){
            et_phone.requestFocus();
            et_phone.setError("Invalid Phone number");
            validity=0;
        }
        if(TextUtils.isEmpty((dob)) || dob.length()>10){
            et_DOB.requestFocus();
            et_DOB.setError("Date of Birth cannot be empty");
            validity=0;
        }
        if(!lname.matches(namePattern)){
            et_lname.requestFocus();
            et_lname.setError("Invalid Name");
            validity=0;
        }
        if(TextUtils.isEmpty((lname))){
            et_lname.requestFocus();
            et_lname.setError("Name cannot be empty");
            validity=0;
        }
        if(!fname.matches(namePattern)){
            et_fname.requestFocus();
            et_fname.setError("Invalid Name");
            validity=0;
        }


        if(validity==0){
            return false;
        }

        return true;
    }

    private void updateUser() {
        String fname=et_fname.getText().toString().trim();
        String lname=et_lname.getText().toString().trim();
        String dob=et_DOB.getText().toString().trim();
        int phone=Integer.parseInt(et_phone.getText().toString().trim());

        Customer customer=new Customer(user.getEmail(),fname,lname,phone,dob);

        DocumentReference documentReference=db.collection("users").document(user.getUid());
        documentReference.set(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ProfileActivity.this,"User updated Successfully",Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }

        });
    }
}
