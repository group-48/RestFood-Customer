package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {

    private Button btn_login;
    private EditText et_email;
    private EditText et_password;
    private TextView tv_signup;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();

        btn_login=(Button) findViewById(R.id.btn_login);
        et_email=(EditText)findViewById(R.id.et_email);
        et_password=(EditText)findViewById(R.id.et_password);
        tv_signup=(TextView)findViewById(R.id.tv_signup);

        btn_login.setOnClickListener(this);
        tv_signup.setOnClickListener(this);

    }

    private void signInUser(){
        String email = et_email.getText().toString().trim();
        String password=et_password.getText().toString().trim();

        if(TextUtils.isEmpty((email))){
            Toast.makeText(this,"Please enter your email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty((password))){
            Toast.makeText(this,"Please enter a password",Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"successful",Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    Toast.makeText(LoginActivity.this,"failed"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }) ;
    }

    public void onClick(View view){
        if(view==btn_login){
            signInUser();
        }

        if(view==tv_signup){
            Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(intent);
        }
    }
}
