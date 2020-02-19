package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dash.restfood_customer.models.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

import static android.telephony.PhoneNumberUtils.isGlobalPhoneNumber;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    //declaring ui components
    private Button btn_signup;
    private EditText et_email;
    private EditText et_password;
    private TextView tv_login;
    private EditText et_fname;
    private EditText et_lname;
    private TextView et_dob;
    private EditText et_phone;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;


    //progressbar
    private ProgressDialog progressDialog;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    /*firebase database

    private DatabaseReference databaseReference;*/

    //firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        FirebaseApp.initializeApp(this);
        firebaseAuth=FirebaseAuth.getInstance();

        //databaseReference=FirebaseDatabase.getInstance().getReference();

        progressDialog=new ProgressDialog(this);

        //initializing ui components
        btn_signup=(Button)findViewById(R.id.btn_signup);
        et_email=(EditText)findViewById(R.id.et_email);
        et_password=(EditText) findViewById(R.id.et_password);
        tv_login=(TextView) findViewById(R.id.tv_login);
        et_fname=(EditText)findViewById(R.id.et_fname);
        et_lname=findViewById(R.id.et_lname);
        et_dob=findViewById(R.id.et_dob);
        et_phone=(EditText)findViewById(R.id.et_phone);

        btn_signup.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        et_dob.setOnClickListener(this);

    }

    private void saveUserInfo() {
        String email=et_email.getText().toString().trim();
        String fname=et_fname.getText().toString().trim();
        String lname=et_lname.getText().toString().trim();
        String dob=et_dob.getText().toString().trim();
        int phone=Integer.parseInt(et_phone.getText().toString().trim());

        FirebaseUser user=firebaseAuth.getCurrentUser();
        Customer customer=new Customer(email,fname,lname,phone,dob);



        db.collection("users").document(user.getUid())
                .set(customer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUpActivity.this,"User Updated",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                        Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void registerUser(){
        if(!validate()){
            return;
        }
        String email=et_email.getText().toString().trim();
        String password=et_password.getText().toString().trim();


        progressDialog.setMessage("Registration in progress");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();

                    saveUserInfo();

                }
                else{
                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    private boolean validate() {
        String email=et_email.getText().toString().trim();
        String fname=et_fname.getText().toString().trim();
        String lname=et_lname.getText().toString().trim();
        String dob=et_dob.getText().toString().trim();
        String phone=et_phone.getText().toString().trim();
        String password=et_password.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String namePattern="[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)";

        int validity=1;

        //get current year
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int year = cal.get(Calendar.YEAR);

        if(TextUtils.isEmpty((password))){
            et_password.requestFocus();
            et_password.setError("Password cannot be empty");
            validity=0;
        }
        if(TextUtils.isEmpty((email))){
            et_email.requestFocus();
            et_email.setError("Email can't be empty");
            validity=0;
        }
        if (!email.matches(emailPattern)){
            et_email.requestFocus();
            et_email.setError("Please enter a valid email");
            validity=0;
        }
        if(TextUtils.isEmpty((phone))){
            et_phone.requestFocus();
            et_phone.setError("Phone No. cannot be empty");
            validity=0;
        }
        if (!isGlobalPhoneNumber(phone) ||  phone.length()>10 || phone.length()<9){
            et_phone.requestFocus();
            et_phone.setError("Invalid Phone number");
            validity=0;
        }
        if(TextUtils.isEmpty((dob)) || dob.length()>10){
            et_dob.requestFocus();
            et_dob.setError("Date of Birth cannot be empty");
            validity=0;
        }
        /*else if(Integer.parseInt(dob.substring(5,9))>(year-2)){
            et_dob.requestFocus();
            et_dob.setError("Invalid Date of Birth");
            validity=0;
        }*/

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


    public void onClick(View view){
        if(view==btn_signup){
            registerUser();
        }

        if(view==tv_login){
            Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        if(view==et_dob){
            et_dob.setError(null);
            calendar= Calendar.getInstance();
            year=calendar.get(Calendar.YEAR);
            month=calendar.get(Calendar.MONTH);
            dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);


            datePickerDialog=new DatePickerDialog(SignUpActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            et_dob.setText(day +"/" + (month+1) + "/" + year);
                        }
                    },year,month,dayOfMonth);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-10000);
            datePickerDialog.show();
        }
    }
}
