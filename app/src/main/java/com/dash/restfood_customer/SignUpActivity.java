package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

import static android.telephony.PhoneNumberUtils.isGlobalPhoneNumber;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    //declaring ui components
    private Button btn_signup;
    private EditText et_email;
    private EditText et_password;
    private TextView tv_login;
    private EditText et_fname;
    private EditText et_lname;
    private EditText et_dob;
    private EditText et_phone;
    private DatePickerDialog.OnDateSetListener dateSetListener;


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
        et_lname=(EditText)findViewById(R.id.et_lname);
        et_dob=(EditText)findViewById(R.id.et_dob);
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
        /*databaseReference.child("Users").child("Customers").child(user.getUid()).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }) ;*/


        db.collection("users").document(user.getUid())
                .set(customer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUpActivity.this,"User Updated",Toast.LENGTH_SHORT).show();
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

        if(TextUtils.isEmpty((email))){
            Toast.makeText(this,"Please enter your email",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.matches(emailPattern)){
            Toast.makeText(this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty((password))){
            Toast.makeText(this,"Please enter a password",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty((fname))){
            Toast.makeText(this,"Please enter your First Name",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty((lname))){
            Toast.makeText(this,"Please enter your Last Name",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty((dob))){
            Toast.makeText(this,"Please enter Your DOB",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty((phone))){
            Toast.makeText(this,"Please enter your Contact No.",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isGlobalPhoneNumber(phone)){
            Toast.makeText(this,"Please enter a valid Contact No.",Toast.LENGTH_SHORT).show();
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

        }
    }
}
