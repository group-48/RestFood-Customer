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
import android.widget.TextView;
import android.widget.Toast;

import com.dash.restfood_customer.InternetConfig.InternetConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {

    private Button btn_login;
    //private Button btn_login;
    private EditText et_email;
    private EditText et_password;
    private TextView tv_signup;
    private TextView tv_forgot;

    FirebaseAuth firebaseAuth;
    FirebaseUser user=firebaseAuth.getInstance().getCurrentUser();
    private Session session;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();

        getSupportActionBar().hide();
        session = new Session(this);
        session.setMenu(0);

        btn_login=(Button) findViewById(R.id.btn_login);
        et_email=(EditText)findViewById(R.id.et_email);
        et_password=(EditText)findViewById(R.id.et_password);
        tv_signup=(TextView)findViewById(R.id.tv_signup);
        tv_forgot=(TextView)findViewById(R.id.tv_forgot);

        btn_login.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);

        progressDialog=new ProgressDialog(this);
        //Toast.makeText(this,user.getUid(),Toast.LENGTH_LONG).show();

    }

    private void signInUser(){
        String email = et_email.getText().toString().trim();
        String password=et_password.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(TextUtils.isEmpty((email)) ||!email.matches(emailPattern)){
            et_email.requestFocus();
            et_email.setError("Please enter a valid email");
            progressDialog.hide();
            return;
        }
        if(TextUtils.isEmpty((password))){
            et_password.requestFocus();
            et_password.setError("Please enter your password");
            progressDialog.hide();
            return;
        }


        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    updateUI(user);
                }
                else{
                    progressDialog.hide();
                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }) ;
    }


    public void onClick(View view){
        if(InternetConfig.isConnectedToInternet(getBaseContext())){
            if(view==btn_login){
                progressDialog.setMessage("Verifying user");
                progressDialog.show();
                signInUser();

            }

            if(view==tv_signup){
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
            if(view==tv_forgot){
                Intent intent=new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(intent);
            }
        }
        else{
            Toast.makeText(this,"Please Check your Internet Connection",Toast.LENGTH_SHORT).show();

        }

    }

    private void updateUI(FirebaseUser user){
        progressDialog.hide();
        String username=user.getEmail();
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("userName",username);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
