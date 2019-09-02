package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    //declaring ui components
    private Button btn_signup;
    private EditText et_email;
    private EditText et_password;
    private TextView tv_login;

    //progressbar
    private ProgressDialog progressDialog;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseApp.initializeApp(this);
        firebaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);

        //initializing ui components
        btn_signup=(Button)findViewById(R.id.btn_signup);
        et_email=(EditText)findViewById(R.id.et_email);
        et_password=(EditText) findViewById(R.id.et_password);
        tv_login=(TextView) findViewById(R.id.tv_login);


        btn_signup.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    private void registerUser(){
        String email=et_email.getText().toString().trim();
        String password=et_password.getText().toString().trim();

        if(TextUtils.isEmpty((email))){
            Toast.makeText(this,"Please enter your email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty((password))){
            Toast.makeText(this,"Please enter a password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Reistration in progress");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this,"successful",Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    Toast.makeText(SignUpActivity.this,"failed"+e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    public void onClick(View view){
        if(view==btn_signup){
            registerUser();
        }

        if(view==tv_login){
            Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }
}
